package it.balyfix.gelf.logger;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.MDC;
import org.apache.log4j.NDC;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.After;
import static org.junit.Assert.*;

import java.net.SocketException;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

import it.balyfix.gelf.logger.log4j.GelfAppender;
import it.balyfix.gelf.logger.message.GelfMessage;
import it.balyfix.gelf.logger.sender.GelfSender;
import it.balyfix.gelf.test.sender.GelfTestSender;

public class Log4jAppenderTest {

	private GelfAppender gelfAppender;
	private GelfTestSender gelfTestSender;
	private static final String CLASS_NAME = Log4jAppenderTest.class.getCanonicalName();
	private boolean rawExtended = false;

	@Before
	public void loadEnv() {
		gelfTestSender = new GelfTestSender();

		gelfAppender = new GelfAppender() {
			public GelfSender getGelfSender() {
				return gelfTestSender;
			}

			@Override
			public void append(LoggingEvent event) {
				super.append(event);
			}

		};
	}

	@After
	public void tearDown() {
		if (gelfAppender.isAddExtendedInformation()) {
			NDC.clear();
		}
	}

	@Test
	public void ensureHostnameForMessage() {

		LoggingEvent event = new LoggingEvent(CLASS_NAME, Category.getInstance(Log4jAppenderTest.class), 123L,
				Level.INFO, "Test Log", new RuntimeException("Inter"));
		gelfAppender.append(event);

		GelfMessage lastMessage = gelfTestSender.getMessages().get(gelfTestSender.getMessages().size() - 1);
		assertNotNull(lastMessage.getHost());

		gelfAppender.setOriginHost("example.com");
		gelfAppender.append(event);
		assertEquals("example.com", lastMessage.getHost());
	}

	@Test
	public void handleNullInAppend() {

		LoggingEvent event = new LoggingEvent(CLASS_NAME, Category.getInstance(this.getClass()), 123L, Level.INFO, null,
				new RuntimeException("LOL"));
		gelfAppender.append(event);

		assertNotNull(getLastMessage(gelfTestSender).getShortMessage());
		assertNotNull(getLastMessage(gelfTestSender).getFullMessage());
	}

	@Test
	public void handleMDC() {

		gelfAppender.setAddExtendedInformation(true);

		LoggingEvent event = new LoggingEvent(CLASS_NAME, Category.getInstance(this.getClass()), 123L, Level.INFO, "",
				new RuntimeException("LOL"));
		MDC.put("foo", "bar");

		gelfAppender.append(event);

		assertEquals("bar", getLastMessage(gelfTestSender).getAdditonalFields().get("foo"));
		assertNotNull(getLastMessage(gelfTestSender).getAdditonalFields().get("non-existent"));
	}

	@Test
	public void handleMDCTransform() {

		gelfAppender.setAddExtendedInformation(true);

		LoggingEvent event = new LoggingEvent(CLASS_NAME, Category.getInstance(this.getClass()), 123L, Level.INFO, "",
				new RuntimeException("LOL"));
		MDC.put("foo", 200);

		gelfAppender.append(event);

		assertEquals("200", getLastMessage(gelfTestSender).getAdditonalFields().get("foo"));
		assertNull(getLastMessage(gelfTestSender).getAdditonalFields().get("non-existent"));

		rawExtended = true;
		event = new LoggingEvent(CLASS_NAME, Category.getInstance(this.getClass()), 123L, Level.INFO, "",
				new RuntimeException("LOL"));
		gelfAppender.append(event);

		assertEquals(new Integer(200), getLastMessage(gelfTestSender).getAdditonalFields().get("foo"));
		assertNotNull(getLastMessage(gelfTestSender).getAdditonalFields().get("non-existent"));

	}

	@Test
	public void handleNDC() {

		gelfAppender.setAddExtendedInformation(true);

		LoggingEvent event = new LoggingEvent(CLASS_NAME, Category.getInstance(this.getClass()), 123L, Level.INFO, "Message",
				new RuntimeException("LOL"));
		NDC.push("Foobar");

		gelfAppender.append(event);

		assertEquals("Foobar",getLastMessage(gelfTestSender).getAdditonalFields().get("loggerNdc"));
	}

	@Test
	public void disableExtendedInformation() {

		gelfAppender.setAddExtendedInformation(false);

		LoggingEvent event = new LoggingEvent(CLASS_NAME, Category.getInstance(this.getClass()), 123L, Level.INFO, "",
				new RuntimeException("LOL"));

		MDC.put("foo", "bar");
		NDC.push("Foobar");

		gelfAppender.append(event);

		assertNull(getLastMessage(gelfTestSender).getAdditonalFields().get("loggerNdc"));
		assertNull(getLastMessage(gelfTestSender).getAdditonalFields().get("foo"));
	}

	@Test
	public void checkExtendedInformation() throws UnknownHostException, SocketException {

		gelfAppender.setAddExtendedInformation(true);

		LoggingEvent event = new LoggingEvent(CLASS_NAME, Category.getInstance(Log4jAppenderTest.class), 123L,
				Level.INFO, "Message", new RuntimeException("LOL"));

		gelfAppender.append(event);

		assertEquals(getLastMessage(gelfTestSender).getAdditonalFields().get("logger"), CLASS_NAME);
	}

//	@Test
//	public void testTcpUdpUrls() {
//
//		GelfAppender testGelfAppender = new GelfAppender() {
//
//			@Override
//			protected GelfUDPSender getGelfUDPSender(String udpGraylogHost, int port) throws IOException {
//				return new MockGelfUDPSender(udpGraylogHost, port);
//			}
//
//			@Override
//			protected GelfTCPSender getGelfTCPSender(String tcpGraylogHost, int port) throws IOException {
//				return new MockGelfTCPSender(tcpGraylogHost, port);
//			}
//
//		};
//
//		TestingEH testingEH = new TestingEH();
//		testGelfAppender.setErrorHandler(testingEH);
//
//		testGelfAppender.setGraylogHost("tcp:www.github.com");
//		testGelfAppender.activateOptions();
//
//		assertThat("No errors when using tcp: url", testingEH.getErrorMessage(),
//				is(not("Unknown Graylog2 hostname:tcp:www.github.com")));
//
//		testGelfAppender.setGraylogHost("udp:www.github.com");
//		testGelfAppender.activateOptions();
//
//		assertThat("No errors when using udp: url", testingEH.getErrorMessage(),
//				is(not("Unknown Graylog2 hostname:udp:www.github.com")));
//
//		testGelfAppender.setGraylogHost("www.github.com");
//		testGelfAppender.activateOptions();
//
//		assertThat("No errors when using udp: url", testingEH.getErrorMessage(),
//				is(not("Unknown Graylog2 hostname:www.github.com")));
//	}

	private GelfMessage getLastMessage(GelfTestSender gelfTestSender) {
		return gelfTestSender.getMessages().get(gelfTestSender.getMessages().size() - 1);
	}

}
