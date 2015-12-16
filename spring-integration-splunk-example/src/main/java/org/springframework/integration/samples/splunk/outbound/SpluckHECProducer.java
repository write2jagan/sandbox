package org.springframework.integration.samples.splunk.outbound;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.samples.splunk.model.OrderSuspectEvent;
import org.springframework.integration.splunk.event.SplunkEvent;
import org.springframework.integration.splunk.outbound.SplunkOutboundChannelAdapter;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

public class SpluckHECProducer {

	private static final String CONFIG = "si-splunk-producer-hecwriter.xml";

	private static final Logger log = LoggerFactory.getLogger(SpluckHECProducer.class);

	public static void main(final String args[]) {
		final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(CONFIG, SpluckHECProducer.class);
		ctx.start();
		final MessageChannel channelRestoutput = ctx.getBean("outputToSplunk", MessageChannel.class);
		
		sendWithRest(channelRestoutput);
		log.info("Done");

	}

	private static void sendWithRest(MessageChannel channel) {
		log.info("Class {}", channel.getClass());
		ConvertToTopicAndPartitionFunction convertToTopicAndPartitionFunction = new ConvertToTopicAndPartitionFunction();
		IntStream.range(1, 3).forEach(i -> {
			channel.send(MessageBuilder.withPayload(convertToTopicAndPartitionFunction.apply(i)).build());
			log.info("Message Sent to splunk" + i);
		});
		log.info("Done");
	}

	
	private static class ConvertToTopicAndPartitionFunction implements Function<Integer, SplunkEvent> {

		Random randomGenerator = new Random();

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SplunkEvent apply(Integer t) {
			OrderSuspectEvent sd = new OrderSuspectEvent();
			sd.setEan(String.valueOf(t * randomGenerator.nextInt(100)));
			sd.setEmailuser("fbalicchia@gmail.com");
			sd.setOrderNumber("21501001010101");
			return sd;
		}

	}

}
