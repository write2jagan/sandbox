package it.balyfix;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainClass {

	
	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(MainClass.class);

		ApplicationContext appContext = new ClassPathXmlApplicationContext(
				"spring/config/BeanLocations.xml");

		appContext.getBean("messageListenerContainer");

		try {
			logger.info("waiting for messages...");
			while (true)
				Thread.sleep(1000);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
