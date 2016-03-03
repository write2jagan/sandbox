package it.balyfix.gelf.logger;

import static org.junit.Assert.assertEquals;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.NDC;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Before;
import org.junit.Test;

import it.balyfix.gelf.logger.message.GelfMessage;
import it.balyfix.gelf.test.sender.GelfTestSender;

public class GelfLog4jxmlDefTest {

	private static String LOG_MESSAGE = "Hello world";

	@Before
	public void before() throws Exception {
		LogManager.getLoggerRepository().resetConfiguration();
		GelfTestSender.getMessages().clear();
		DOMConfigurator.configure(getClass().getResource("/log4j.xml"));
		MDC.remove("mdcField1");
	}

	@Test
	public void XmlDefinition() throws Exception {
		Logger logger = Logger.getLogger(getClass());
		NDC.clear();
		NDC.push("ndc message");
		logger.info(LOG_MESSAGE);
		NDC.clear();
		assertEquals(1, GelfTestSender.getMessages().size());
		GelfMessage gelfMessage = GelfTestSender.getMessages().get(0);
		assertEquals(LOG_MESSAGE, gelfMessage.getFullMessage());
		assertEquals(LOG_MESSAGE, gelfMessage.getShortMessage());
		assertEquals("6", gelfMessage.getLevel());
		assertEquals("DEV", gelfMessage.getAdditonalFields().get("environment"));
	}

}