package ws.cogito.microservices;

import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;


/**
 * Event Care Management Route Builder
 */
public class EventCareManagementRouteBuilder extends RouteBuilder {

    public void configure() {
    	
    	/**
    	 * Route errors to DLQ after one retry and one second delay
    	 */
    	errorHandler(deadLetterChannel("activemq:event.care.dlq").
    			maximumRedeliveries(1).redeliveryDelay(1000));
    	
    	/**
    	 * Enrichment - assign Care Management ID and route to task queue
    	 */
    	from("activemq:event.inpatient").
    	process(new TrackingIdProcessor()).
    	process(new Processor() {
    		public void process(Exchange exchange) {
    			String uuid = UUID.randomUUID().toString();
    			exchange.getIn().setHeader("CareManagementID", uuid);
    		}
    	}).
    	to("activemq:event.care.tasks");   	
    }
}