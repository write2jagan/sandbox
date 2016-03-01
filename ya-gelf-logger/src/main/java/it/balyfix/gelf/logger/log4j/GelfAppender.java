package it.balyfix.gelf.logger.log4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;
import org.json.simple.JSONValue;

import it.balyfix.gelf.logger.commons.BaseHostHostProvider;
import it.balyfix.gelf.logger.commons.GelfLoggerErrorHandler;
import it.balyfix.gelf.logger.commons.GelfSenderFactory;
import it.balyfix.gelf.logger.message.GelfMessage;
import it.balyfix.gelf.logger.message.GelfMessageFactory;
import it.balyfix.gelf.logger.message.GelfMessageProvider;
import it.balyfix.gelf.logger.providers.AMQPProvider;
import it.balyfix.gelf.logger.providers.KafkaProvider;
import it.balyfix.gelf.logger.sender.GelfSender;
import it.balyfix.gelf.logger.sender.GelfSenderResult;

public class GelfAppender extends AppenderSkeleton implements GelfMessageProvider, GelfLoggerErrorHandler {

	private String targetHost;

	private String amqpExchangeName;

	private String amqpRoutingKey;

	private int amqpMaxRetries = 0;

	private static String originHost;

	private int targetPort = GelfSender.DEFAULT_PORT;

	private String facility;

	private GelfSender gelfSender;

	private String topic;

	private boolean extractStacktrace;

	private boolean addExtendedInformation;

	private boolean includeLocation = true;

	private Map<String, String> fields;

	public GelfAppender() {
		super();
	}

	@SuppressWarnings("unchecked")
	public void setAdditionalFields(String additionalFields) {
		fields = (Map<String, String>) JSONValue.parse(additionalFields.replaceAll("'", "\""));
	}

	public int getTargetPort() {
		return targetPort;
	}

	public void setTargetPort(int targetPort) {
		this.targetPort = targetPort;
	}

	public String getTargetHost() {
		return targetHost;
	}

	public void setTargetHost(String targetHost) {
		this.targetHost = targetHost;
	}

	public String getAmqpExchangeName() {
		return amqpExchangeName;
	}

	public void setAmqpExchangeName(String amqpExchangeName) {
		this.amqpExchangeName = amqpExchangeName;
	}

	public String getAmqpRoutingKey() {
		return amqpRoutingKey;
	}

	public void setAmqpRoutingKey(String amqpRoutingKey) {
		this.amqpRoutingKey = amqpRoutingKey;
	}

	public int getAmqpMaxRetries() {
		return amqpMaxRetries;
	}

	public void setAmqpMaxRetries(int amqpMaxRetries) {
		this.amqpMaxRetries = amqpMaxRetries;
	}

	public String getFacility() {
		return facility;
	}

	public void setFacility(String facility) {
		this.facility = facility;
	}

	public boolean isExtractStacktrace() {
		return extractStacktrace;
	}

	public void setExtractStacktrace(boolean extractStacktrace) {
		this.extractStacktrace = extractStacktrace;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getOriginHost() {
		if (originHost == null) {
			originHost = getLocalHostName();
		}
		return originHost;
	}

	private String getLocalHostName() {
		String hostName = null;
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			errorHandler.error("Unknown local hostname", e, ErrorCode.GENERIC_FAILURE);
		}

		return hostName;
	}

	public void setOriginHost(String originHost) {
		GelfAppender.originHost = originHost;
	}

	public boolean isAddExtendedInformation() {
		return addExtendedInformation;
	}

	public void setAddExtendedInformation(boolean addExtendedInformation) {
		this.addExtendedInformation = addExtendedInformation;
	}

	public boolean isIncludeLocation() {
		return this.includeLocation;
	}

	public void setIncludeLocation(boolean includeLocation) {
		this.includeLocation = includeLocation;
	}

	public Map<String, String> getFields() {
		if (fields == null) {
			fields = new HashMap<String, String>();
		}
		return Collections.unmodifiableMap(fields);
	}

	public Object transformExtendedField(String field, Object object) {
		if (object != null)
			return object.toString();
		return null;
	}

	@Override
	public void activateOptions() {

		Map<String, Object> extraconfiguration = new HashMap<String, Object>();

		if (isAMQPParameterValid()) {
			extraconfiguration.put(AMQPProvider.AMQP_EXCHANGE_NAME, amqpExchangeName);
			extraconfiguration.put(AMQPProvider.AMQP_ROUTING_KEY, amqpRoutingKey);
			extraconfiguration.put(AMQPProvider.MAX_RETRIES, amqpMaxRetries);
		}

		if (topic != null && !topic.isEmpty()) {
			extraconfiguration.put(KafkaProvider.TOPIC, topic);
		}
		extraconfiguration.put(KafkaProvider.CLIENT_ID, originHost);

		gelfSender = GelfSenderFactory.getSender(new BaseHostHostProvider() {

			@Override
			public Integer getPort() {
				return targetPort;
			}

			@Override
			public String getHost() {
				return targetHost;
			}

		}, extraconfiguration, this);

	}

	@Override
	protected void append(LoggingEvent event) {
		GelfMessage gelfMessage = GelfMessageFactory.makeMessage(layout, event, this);

		if (getGelfSender() == null) {
			errorHandler.error("Could not send GELF message. Gelf Sender is not initialised and equals null");
		} else {
			GelfSenderResult gelfSenderResult = getGelfSender().sendMessage(gelfMessage);
			if (!GelfSenderResult.OK.equals(gelfSenderResult)) {
				errorHandler.error("Error during sending GELF message. Error code: " + gelfSenderResult.getCode() + ".",
						gelfSenderResult.getException(), ErrorCode.WRITE_FAILURE);
			}
		}

	}

	private Boolean isAMQPParameterValid() {

		if (amqpExchangeName != null && amqpExchangeName.length() != 0 && amqpRoutingKey != null
				&& amqpRoutingKey.length() != 0) {
			return true;
		} else if (amqpExchangeName == null || amqpRoutingKey == null) {
			return false;
		} else if (amqpExchangeName.length() == 0 || amqpRoutingKey.length() == 0) {
			errorHandler.error("Please initialize all fields to use amqp ");
		}

		return false;

	}

	public GelfSender getGelfSender() {
		return gelfSender;
	}

	public void close() {
		GelfSender x = this.getGelfSender();
		if (x != null) {
			x.close();
		}
	}

	public boolean requiresLayout() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void printError(String message, Exception e) {
		errorHandler.error(message, e, 0);

	}
}
