package ws.cogito.microservices;

import org.apache.camel.builder.RouteBuilder;


/**
 * Event Ingestion Route Builder
 */
public class EventIngestionRouteBuilder extends RouteBuilder {

    public void configure() {
    	
    	/**
    	 * Route errors to DLQ after one retry and one second delay
    	 */
    	errorHandler(deadLetterChannel("activemq:event.ingestion.dlq").
    			maximumRedeliveries(1).redeliveryDelay(1000));
    	
    	/**
    	 * Content Based Routing - Inpatient-Outpatient Events
    	 */
    	from("activemq:event.ingestion").
    	process(new TrackingIdProcessor()).
    	choice().
    		when().simple("${in.body} contains 'inpatient'").
    			to("activemq:event.inpatient").
    		otherwise().
    			to("activemq:event.outpatient").
			end().
    	to("activemq:event.audit");   	
    }
}