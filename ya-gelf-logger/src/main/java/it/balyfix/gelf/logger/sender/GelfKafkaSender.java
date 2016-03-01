package it.balyfix.gelf.logger.sender;

import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import it.balyfix.gelf.logger.message.GelfMessage;

public class GelfKafkaSender implements GelfSender {

	private String topic;

	private String bootstrap_servers;

	private String clientId;

	private KafkaProducer<String, String> producer;

	public GelfKafkaSender(String topic, String bootstrap_servers, String clientId) {
		this.topic = topic;
		this.bootstrap_servers = bootstrap_servers;
		this.clientId = clientId;
		Properties props = new Properties();
		props.put("bootstrap.servers", bootstrap_servers);
		props.put("client.id", clientId);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		producer = new KafkaProducer<String, String>(props);
	}

	@Override
	public GelfSenderResult sendMessage(GelfMessage message) {
		String messageId = UUID.randomUUID().toString();
		producer.send(new ProducerRecord<String, String>(topic, messageId, message.toJson()));
		return GelfSenderResult.OK;
	}

	@Override
	public void close() {
		if (producer != null) {
			producer.close();
		}

	}

}
