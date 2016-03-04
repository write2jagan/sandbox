package it.balyfix.gelf.logger;

import org.junit.Assert;
import org.junit.Test;

import it.balyfix.gelf.logger.providers.AMQPProvider;
import it.balyfix.gelf.logger.providers.DefaultSenderProvider;
import it.balyfix.gelf.logger.providers.KafkaProvider;

public class GelfSenderProviderTest {

	@Test
	public void defaultSenderProvider() {
		DefaultSenderProvider defaultSenderProvider = new DefaultSenderProvider();

		String host1 = "tcp://localhost";
		String host2 = "tcp://127.0.0.1";
		String host3 = "udp://localhost";
		String host4 = "udp://127.0.0.1";
		String host5 = "tcp:localhost";
		String host6 = "tcp:127.0.0.1";
		String host7 = "udp:localhost";
		String host8 = "udp:127.0.0.1";

		Assert.assertTrue(defaultSenderProvider.supports(host1));
		Assert.assertTrue(defaultSenderProvider.supports(host2));
		Assert.assertTrue(defaultSenderProvider.supports(host3));
		Assert.assertTrue(defaultSenderProvider.supports(host4));
		Assert.assertTrue(defaultSenderProvider.supports(host5));
		Assert.assertTrue(defaultSenderProvider.supports(host6));
		Assert.assertTrue(defaultSenderProvider.supports(host7));
		Assert.assertTrue(defaultSenderProvider.supports(host8));

		Assert.assertEquals("localhost", defaultSenderProvider.getCanonicalHost(host1));
		Assert.assertEquals("127.0.0.1", defaultSenderProvider.getCanonicalHost(host2));
		Assert.assertEquals("localhost", defaultSenderProvider.getCanonicalHost(host3));
		Assert.assertEquals("127.0.0.1", defaultSenderProvider.getCanonicalHost(host4));
		Assert.assertEquals("localhost", defaultSenderProvider.getCanonicalHost(host5));
		Assert.assertEquals("127.0.0.1", defaultSenderProvider.getCanonicalHost(host6));
		Assert.assertEquals("localhost", defaultSenderProvider.getCanonicalHost(host7));
		Assert.assertEquals("127.0.0.1", defaultSenderProvider.getCanonicalHost(host8));

	}
	
	
	@Test
	public void amqpSenderProvider() {
		AMQPProvider amqpProvider = new AMQPProvider();

		String host1 = "amqp://localhost";
		String host2 = "amqp://127.0.0.1";
		String host3 = "amqp:localhost";
		String host4 = "amqp:127.0.0.1";
		
		Assert.assertTrue(amqpProvider.supports(host1));
		Assert.assertTrue(amqpProvider.supports(host2));
		Assert.assertTrue(amqpProvider.supports(host3));
		Assert.assertTrue(amqpProvider.supports(host4));

		Assert.assertEquals("localhost", amqpProvider.getCanonicalHost(host1));
		Assert.assertEquals("127.0.0.1", amqpProvider.getCanonicalHost(host2));
		Assert.assertEquals("localhost", amqpProvider.getCanonicalHost(host3));
		Assert.assertEquals("127.0.0.1", amqpProvider.getCanonicalHost(host4));

	}

	
	
	@Test
	public void kafkaSenderProvider() {
		KafkaProvider kafkaProvider = new KafkaProvider();

		String host1 = "kafka://localhost";
		String host2 = "kafka://127.0.0.1";
		String host3 = "kafka:localhost";
		String host4 = "kafka:127.0.0.1";
		
		Assert.assertTrue(kafkaProvider.supports(host1));
		Assert.assertTrue(kafkaProvider.supports(host2));
		Assert.assertTrue(kafkaProvider.supports(host3));
		Assert.assertTrue(kafkaProvider.supports(host4));

		Assert.assertEquals("localhost", kafkaProvider.getCanonicalHost(host1));
		Assert.assertEquals("127.0.0.1", kafkaProvider.getCanonicalHost(host2));
		Assert.assertEquals("localhost", kafkaProvider.getCanonicalHost(host3));
		Assert.assertEquals("127.0.0.1", kafkaProvider.getCanonicalHost(host4));

	}

	
	

}
