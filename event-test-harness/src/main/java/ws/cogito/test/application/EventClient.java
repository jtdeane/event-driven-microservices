package ws.cogito.test.application;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

/**
 * Event-driven Microservices Article
 * @author jeremydeane
 */
public final class EventClient {

	private static final Logger logger = LoggerFactory.getLogger
			(EventClient.class);

	public static void main(String[] args) {
		
		logger.debug("Loading Spring Application Context from Classpath");
		
		ApplicationContext context = new ClassPathXmlApplicationContext
				("event-client-spring.xml");
		
		//argument determines the encounter json file
		if (args.length > 0) {
			
			switch (args[0]) {
			
			case "Leon":
				
				sendEncounter1(context);
				
				break;
				
			case "Roy":
				
				sendEncounter2(context);
				
				break;
				
			case "Eldon":
				
				sendEncounter3(context);
				
				break;
			}
			
			
		} else {
			
			//no argument passed
			sendEncounter2(context);
		}
	}

	/**
	 * Send an Encounter 1 JSON to the Ingenstion Queue
	 * @param context
	 */
	private static void sendEncounter1(ApplicationContext context) {
		
		JmsTemplate jmsTemplate = (JmsTemplate) context.getBean("jmsQueueTemplate");
		
		/*
		 * http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/jms/core/MessageCreator.html
		 */
		jmsTemplate.send(session -> {
			
			TextMessage message = null;
            
        	message = session.createTextMessage
					(getJSONFromFile("encounter1.json"));
		
			message.setStringProperty("Mime Type", "application/json");
            
            return message;
			
		});
		
		logger.debug("Check: http://localhost:8161/admin/queues.jsp");
	}
	
	/**
	 * Send an Encounter 2 JSON to the Ingenstion Queue
	 * @param context
	 */
	private static void sendEncounter2(ApplicationContext context) {
		
		JmsTemplate jmsTemplate = (JmsTemplate) context.getBean("jmsQueueTemplate");

		/*
		 * http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/jms/core/MessageCreator.html
		 */
		jmsTemplate.send(session -> {
			
			TextMessage message = null;
            
        	message = session.createTextMessage
					(getJSONFromFile("encounter2.json"));
		
			message.setStringProperty("Mime Type", "application/json");
            
            return message;
			
		});
		
		logger.debug("Check: http://localhost:8161/admin/queues.jsp");
	}
	
	/**
	 * Send an Encounter 3 JSON to the Ingenstion Queue
	 * @param context
	 */
	private static void sendEncounter3(ApplicationContext context) {
		
		JmsTemplate jmsTemplate = (JmsTemplate) context.getBean("jmsQueueTemplate");

		/*
		 * http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/jms/core/MessageCreator.html
		 */
		jmsTemplate.send(session -> {
			
			TextMessage message = null;
            
        	message = session.createTextMessage
					(getJSONFromFile("encounter3.json"));
		
			message.setStringProperty("Mime Type", "application/json");
            
            return message;
			
		});
		
		logger.debug("Check: http://localhost:8161/admin/queues.jsp");
	}
	
    /**
     * Helper method for retrieving json from file
     * @param fileName
     * @return String
     */
    private static String getJSONFromFile(String fileName) {
        String json = null;
        StringBuffer text = new StringBuffer();
        String line = null;
        
        try (BufferedReader in = new BufferedReader
        		(new InputStreamReader(EventClient.class
                .getResourceAsStream(fileName)))) {
        	
            while ((line = in.readLine()) != null) {
                text.append(line);
            }

            json = text.toString();
			
		} catch (Exception e) {
			
			logger.debug("Failed to read test file " + fileName + " " + e.toString());
			System.exit(1);
		}

        return json;
    }
}