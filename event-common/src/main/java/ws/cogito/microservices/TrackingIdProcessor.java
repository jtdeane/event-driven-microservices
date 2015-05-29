package ws.cogito.microservices;

import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create a Tracking ID if one does not already exist
 * @author jeremydeane
 *
 */
public class TrackingIdProcessor implements Processor {
	
	private static final Logger logger = LoggerFactory.getLogger
			(TrackingIdProcessor.class);	
  
	/**
	 * create Tracking ID if it does not already exist
	 */
	public void process(Exchange exchange) throws Exception {
		
		
		if (exchange.getIn().getHeader("TrackingID") != null) {
			
			logger.info("Existing Tracking ID: " + 
					exchange.getIn().getHeader("TrackingID"));
			
		} else {
			
			String uuid = UUID.randomUUID().toString();
			
			exchange.getIn().setHeader("TrackingID", uuid);
			
			logger.info("Created Tracking ID: " + uuid);
		}
	}
}
