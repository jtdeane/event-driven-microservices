package ws.cogito.microservices;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;


/**
 * Event Ingestion Route Builder
 */
public class EventIngestionRouteBuilder extends RouteBuilder {

    public void configure() {
    	
    	/*
    	 * Route errors to DLQ after one retry and one second delay
    	 */
    	errorHandler(deadLetterChannel("activemq:event.ingestion.dlq").
    			maximumRedeliveries(1).redeliveryDelay(1000));
    	
    	/*
    	 * Content Based Routing V1 - Inpatient-Outpatient Events
    	 * http://camel.apache.org/jsonpath.html
    	 */
    	from("activemq:event.ingestion").
    	process(new TrackingIdProcessor()).
    	choice().
    		when().jsonpath("$[?(@.class==inpatient)]").
    			to("activemq:event.inpatient").
    		otherwise().
    			to("activemq:event.outpatient").
			end().
    	to("activemq:event.audit"); 
    	
    	/*
    	 * Content Based Routing V2 - Inpatient-Outpatient Events with Throttle
    	 * http://camel.apache.org/jsonpath.html
    	 * http://camel.apache.org/throttler.html
    	 * limit 100 requests per second with non-blocking delay using thread pool
    	 
    	from("activemq:event.ingestion").
    	to("seda:audit");
    	
    	from ("seda:audit").	
    	throttle(100).asyncDelayed().
    	process(new TrackingIdProcessor()).
    	choice().
    		when().jsonpath("$[?(@.class==inpatient)]").
    			to("activemq:event.inpatient").
    		otherwise().
    			to("activemq:event.outpatient").
			end().
    	to("activemq:event.audit"); */
    	
    	/*
    	 * Content Based Routing V3 - Inpatient-Outpatient Events with circuit breaker
    	 * http://camel.apache.org/jsonpath.html
    	 * http://camel.apache.org/load-balancer.html
    	 * After two failures circuit breaker goes into Open State; after five
    	 * seconds will try again. In Open State messages go to DLQ.
    	 
    	from("activemq:event.ingestion").
    	process(new TrackingIdProcessor()).
    	choice().
    		when().jsonpath("$[?(@.class==inpatient)]").
    			to("activemq:event.inpatient").
    		otherwise().
    			to("activemq:event.outpatient").
			end().
		to("direct:saas");
    	
    	from("direct:saas").
    	log("Sending SAAS HTTP Request").
    	setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.GET)).
		loadBalance().
		circuitBreaker(2, 5000L, CircuitBreakerOpenException.class).	
    	to("http4://localhost:8080/event");*/
    }
}