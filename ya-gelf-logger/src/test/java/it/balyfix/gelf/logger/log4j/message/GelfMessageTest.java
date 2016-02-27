package it.balyfix.gelf.logger.log4j.message;

import java.nio.ByteBuffer;
import java.time.LocalTime;
import java.time.temporal.ChronoField;

import org.junit.Assert;
import org.junit.Test;

import it.balyfix.gelf.logger.message.GelfMessage;

/**
 * @author fbalicchia
 * @version $Id: $
 */
public class GelfMessageTest {

	@Test
	public void shortMessage() {
		GelfMessage gelfMessage = new GelfMessage();
		gelfMessage.setFullMessage("Doing attribute test");
		gelfMessage.setVersion(GelfMessage.GELF_VERSION_1_1);
		ByteBuffer[] udpBuffers = gelfMessage.toUDPBuffers();
		Assert.assertTrue(udpBuffers.length == 1);
		int length = gelfMessage.toTCPBuffer().array().length;
		Assert.assertTrue(length == 138);
	}

	@Test
	public void longMessage() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 100000; i++) {
			sb.append("0123456789 ");
		}
		GelfMessage gelfMessage = new GelfMessage("Problem during payment chekout", sb.toString(),
				LocalTime.now().getLong(ChronoField.MICRO_OF_DAY), "1");
		ByteBuffer[] udpBuffers = gelfMessage.toUDPBuffers();
		ByteBuffer tcpBuffer = gelfMessage.toTCPBuffer();
		int length = tcpBuffer.array().length;
		Assert.assertTrue(length == 1100153);
		Assert.assertSame(2, udpBuffers.length);

	}

}
