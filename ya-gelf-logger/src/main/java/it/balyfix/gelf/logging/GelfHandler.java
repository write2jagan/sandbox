package it.balyfix.gelf.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.IllegalFormatConversionException;
import java.util.Map;
import java.util.logging.ErrorManager;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

import it.balyfix.gelf.logger.commons.BaseHostHostProvider;
import it.balyfix.gelf.logger.commons.GelfLoggerErrorHandler;
import it.balyfix.gelf.logger.commons.GelfSenderFactory;
import it.balyfix.gelf.logger.message.GelfMessage;
import it.balyfix.gelf.logger.sender.GelfSender;
import it.balyfix.gelf.logger.sender.GelfSenderResult;

public class GelfHandler extends Handler implements GelfLoggerErrorHandler {

	private static final int MAX_SHORT_MESSAGE_LENGTH = 250;

	private String graylogHost;

	private String originHost;

	private int graylogPort;

	private String facility;

	private GelfSender gelfSender;

	private boolean extractStacktrace;

	private Map<String, String> fields;

	public GelfHandler() {
		final LogManager manager = LogManager.getLogManager();
		final String prefix = getClass().getName();

		graylogHost = manager.getProperty(prefix + ".graylogHost");
		final String port = manager.getProperty(prefix + ".graylogPort");
		graylogPort = null == port ? GelfSender.DEFAULT_PORT : Integer.parseInt(port);
		originHost = manager.getProperty(prefix + ".originHost");
		extractStacktrace = "true".equalsIgnoreCase(manager.getProperty(prefix + ".extractStacktrace"));
		int fieldNumber = 0;
		fields = new HashMap<String, String>();
		while (true) {
			final String property = manager.getProperty(prefix + ".additionalField." + fieldNumber);
			if (null == property) {
				break;
			}
			final int index = property.indexOf('=');
			if (-1 != index) {
				fields.put(property.substring(0, index), property.substring(index + 1));
			}

			fieldNumber++;
		}
		facility = manager.getProperty(prefix + ".facility");

		final String level = manager.getProperty(prefix + ".level");
		if (null != level) {
			setLevel(Level.parse(level.trim()));
		} else {
			setLevel(Level.INFO);
		}

		final String filter = manager.getProperty(prefix + ".filter");
		try {
			if (null != filter) {
				final Class clazz = ClassLoader.getSystemClassLoader().loadClass(filter);
				setFilter((Filter) clazz.newInstance());
			}
		} catch (final Exception e) {
			// ignore
		}
		// This only used for testing
		final String testSender = manager.getProperty(prefix + ".graylogTestSenderClass");
		try {
			if (null != testSender) {
				final Class clazz = ClassLoader.getSystemClassLoader().loadClass(testSender);
				gelfSender = (GelfSender) clazz.newInstance();
			}
		} catch (final Exception e) {
			// ignore
		}
	}

	@Override
	public synchronized void flush() {
	}

	private String getOriginHost() {
		if (null == originHost) {
			originHost = getLocalHostName();
		}
		return originHost;
	}

	private String getLocalHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (final UnknownHostException uhe) {
			reportError("Unknown local hostname", uhe, ErrorManager.GENERIC_FAILURE);
		}

		return null;
	}

	@Override
	public synchronized void publish(final LogRecord record) {
		if (!isLoggable(record)) {
			return;
		}
		if (null == gelfSender) {
			gelfSender = GelfSenderFactory.getSender(new BaseHostHostProvider() {

				@Override
				public Integer getPort() {
					return graylogPort;
				}

				@Override
				public String getHost() {
					return graylogHost;
				}

			}, null, this);
		}
		if (null == gelfSender) {
			reportError("Could not send GELF message", null, ErrorManager.WRITE_FAILURE);
		} else {
			GelfSenderResult gelfSenderResult = gelfSender.sendMessage(makeMessage(record));
			if (!GelfSenderResult.OK.equals(gelfSenderResult)) {
				reportError("Error during sending GELF message. Error code: " + gelfSenderResult.getCode() + ".",
						gelfSenderResult.getException(), ErrorManager.WRITE_FAILURE);
			}
		}
	}

	@Override
	public void close() {
		if (null != gelfSender) {
			gelfSender.close();
			gelfSender = null;
		}
	}

	private GelfMessage makeMessage(final LogRecord record) {
		String message = record.getMessage();
		Object[] parameters = record.getParameters();

		if (message == null)
			message = "";
		if (parameters != null && parameters.length > 0) {
			// by default, using {0}, {1}, etc. -> MessageFormat
			message = MessageFormat.format(message, parameters);

			if (message.equals(record.getMessage())) {
				// if the text is the same, assuming this is String.format type
				// log (%s, %d, etc.)
				try {
					message = String.format(message, parameters);
				} catch (IllegalFormatConversionException e) {
					// leaving message as it is to avoid compatibility problems
					message = record.getMessage();
				} catch (NullPointerException e) {
					// ignore
				}
			}
		}

		final String shortMessage;
		if (message.length() > MAX_SHORT_MESSAGE_LENGTH) {
			shortMessage = message.substring(0, MAX_SHORT_MESSAGE_LENGTH - 1);
		} else {
			shortMessage = message;
		}

		if (extractStacktrace) {
			final Throwable thrown = record.getThrown();
			if (null != thrown) {
				final StringWriter sw = new StringWriter();
				thrown.printStackTrace(new PrintWriter(sw));
				message += "\n\r" + sw.toString();
			}
		}

		final GelfMessage gelfMessage = new GelfMessage(shortMessage, message, record.getMillis(),
				String.valueOf(levelToSyslogLevel(record.getLevel())));
		gelfMessage.addField("SourceClassName", record.getSourceClassName());
		gelfMessage.addField("SourceMethodName", record.getSourceMethodName());

		if (null != getOriginHost()) {
			gelfMessage.setHost(getOriginHost());
		}

		if (null != facility) {
			gelfMessage.setFacility(facility);
		}

		if (null != fields) {
			for (final Map.Entry<String, String> entry : fields.entrySet()) {
				gelfMessage.addField(entry.getKey(), entry.getValue());
			}
		}

		return gelfMessage;
	}

	private int levelToSyslogLevel(final Level level) {
		final int syslogLevel;
		if (level.intValue() == Level.SEVERE.intValue()) {
			syslogLevel = 3;
		} else if (level.intValue() == Level.WARNING.intValue()) {
			syslogLevel = 4;
		} else if (level.intValue() == Level.INFO.intValue()) {
			syslogLevel = 6;
		} else {
			syslogLevel = 7;
		}
		return syslogLevel;
	}

	public void setExtractStacktrace(boolean extractStacktrace) {
		this.extractStacktrace = extractStacktrace;
	}

	public void setGraylogPort(int graylogPort) {
		this.graylogPort = graylogPort;
	}

	public void setOriginHost(String originHost) {
		this.originHost = originHost;
	}

	public void setGraylogHost(String graylogHost) {
		this.graylogHost = graylogHost;
	}

	/**
	 * Facility is deprecated if necessary add other info send an addition field
	 * 
	 * @param facility
	 */
	@Deprecated
	public void setFacility(String facility) {
		this.facility = facility;
	}

	public void setAdditionalField(String entry) {
		if (entry == null)
			return;
		final int index = entry.indexOf('=');
		if (-1 != index) {
			String key = entry.substring(0, index);
			String val = entry.substring(index + 1);
			if (key.equals(""))
				return;
			fields.put(key, val);
		}
	}

	public Map<String, String> getFields() {
		return fields;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void printError(String msg, Exception ex) {
		reportError(msg, ex, 0);

	}
}
