package it.balyfix.gelf.logger;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.NDC;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.After;
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

			@Override
			public Object transformExtendedField(String field, Object object) {
				if (rawExtended) {
					return object;
				} else {
					return super.transformExtendedField(field, object);
				}
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

		gelfAppender.setOriginHost("example.com");
		LoggingEvent event = new LoggingEvent(CLASS_NAME, Logger.getLogger(this.getClass()), 123L, Level.INFO,
				"Test Log", new RuntimeException("Inter"));
		gelfAppender.append(event);
		GelfMessage lastMessage = gelfTestSender.getMessages().get(gelfTestSender.getMessages().size() - 1);
		assertNotNull(lastMessage.getHost());
		gelfAppender.append(event);
		assertEquals("example.com", lastMessage.getHost());
	}

	@Test
	public void handleNullInAppend() {

		LoggingEvent event = new LoggingEvent(CLASS_NAME, Logger.getLogger(this.getClass()), 123L, Level.INFO, "Jonny",
				new RuntimeException("LOL"));
		gelfAppender.append(event);

		assertNotNull(getLastMessage(gelfTestSender).getShortMessage());
		assertNotNull(getLastMessage(gelfTestSender).getFullMessage());
	}

	@Test
	public void handleMDC() {

		gelfAppender.setAddExtendedInformation(true);

		LoggingEvent event = new LoggingEvent(CLASS_NAME, Logger.getLogger(this.getClass()), 123L, Level.INFO, "",
				new RuntimeException("LOL"));
		MDC.put("foo", "bar");

		gelfAppender.append(event);

		assertEquals("bar", getLastMessage(gelfTestSender).getAdditonalFields().get("foo"));
		assertNull(getLastMessage(gelfTestSender).getAdditonalFields().get("non-existent"));
	}

	@Test
	public void handleMDCTransform() {

		gelfAppender.setAddExtendedInformation(true);

		LoggingEvent event = new LoggingEvent(CLASS_NAME, Logger.getLogger(this.getClass()), 123L, Level.INFO, "",
				new RuntimeException("LOL"));
		MDC.put("foo", 200);

		gelfAppender.append(event);

		assertEquals("200", getLastMessage(gelfTestSender).getAdditonalFields().get("foo"));
		assertNull(getLastMessage(gelfTestSender).getAdditonalFields().get("non-existent"));

		rawExtended = true;
		event = new LoggingEvent(CLASS_NAME, Logger.getLogger(this.getClass()), 123L, Level.INFO, "",
				new RuntimeException("LOL"));
		gelfAppender.append(event);

		assertEquals(new Integer(200), getLastMessage(gelfTestSender).getAdditonalFields().get("foo"));
		assertNull(getLastMessage(gelfTestSender).getAdditonalFields().get("non-existent"));

	}

	@Test
	public void handleNDC() {

		gelfAppender.setAddExtendedInformation(true);

		LoggingEvent event = new LoggingEvent(CLASS_NAME, Logger.getLogger(this.getClass()), 123L, Level.INFO,
				"Message", new RuntimeException("LOL"));
		NDC.push("Foobar");
		gelfAppender.append(event);
		assertEquals("Foobar", getLastMessage(gelfTestSender).getAdditonalFields().get("loggerNdc"));
	}

	@Test
	public void disableExtendedInformation() {

		gelfAppender.setAddExtendedInformation(false);

		LoggingEvent event = new LoggingEvent(CLASS_NAME, Logger.getLogger(this.getClass()), 123L, Level.INFO, "",
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

		LoggingEvent event = new LoggingEvent(CLASS_NAME, Logger.getLogger(this.getClass()), 123L, Level.INFO,
				"Message", new RuntimeException("LOL"));

		gelfAppender.append(event);

		assertEquals(getLastMessage(gelfTestSender).getAdditonalFields().get("logger"), CLASS_NAME);
	}

	@Test
	public void testTcpUdpUrls() {

		GelfAppender testGelfAppender = new GelfAppender();
		TestingEH testingEH = new TestingEH();
		testGelfAppender.setErrorHandler(testingEH);
		testGelfAppender.setTargetHost("tcp:www.github.com");
		testGelfAppender.activateOptions();
		assertThat("No errors when using tcp: url", testingEH.getErrorMessage(),
				is(not("Unknown Graylog2 hostname:tcp:www.github.com")));
		testGelfAppender.setTargetHost("udp:www.github.com");
		testGelfAppender.activateOptions();
		assertThat("No errors when using udp: url", testingEH.getErrorMessage(),
				is(not("Unknown Graylog2 hostname:udp:www.github.com")));
		testGelfAppender.setTargetHost("www.github.com");
		testGelfAppender.activateOptions();

		assertThat("No errors when using udp: url", testingEH.getErrorMessage(),
				is(not("Unknown Graylog2 hostname:www.github.com")));
	}

	private class TestingEH implements ErrorHandler {

		private String errorMessage = "";

		public void setLogger(Logger logger) {
		}

		public void error(String s, Exception e, int i) {
			errorMessage = s;
		}

		public void error(String s) {
			errorMessage = s;
		}

		public void error(String s, Exception e, int i, LoggingEvent loggingEvent) {
			errorMessage = s;
		}

		public void setAppender(Appender appender) {
		}

		public void setBackupAppender(Appender appender) {
		}

		public void activateOptions() {
		}

		public String getErrorMessage() {
			return errorMessage;
		}
	}

	private GelfMessage getLastMessage(GelfTestSender gelfTestSender) {
		return gelfTestSender.getMessages().get(gelfTestSender.getMessages().size() - 1);
	}

}
