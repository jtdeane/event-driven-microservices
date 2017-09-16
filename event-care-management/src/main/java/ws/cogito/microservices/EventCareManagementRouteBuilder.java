package ws.cogito.microservices;

import java.util.UUID;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


/**
 * Event Care Management Route Builder
 */
@Component
public class EventCareManagementRouteBuilder extends RouteBuilder implements RoutesBuilder {

    public void configure() {
    	
    	/*
    	 * Route errors to DLQ after one retry and one second delay
    	 */
    	errorHandler(deadLetterChannel("activemq:event.care.dlq").
    			maximumRedeliveries(1).redeliveryDelay(1000));
    	
    	/*
    	 * Enrichment - assign Care Management ID and route to task queue
    	 * http://camel.apache.org/maven/camel-2.15.0/camel-core/apidocs/org/apache/camel/Processor.html
    	 */
    	from("activemq:event.inpatient").
    	process(new TrackingIdProcessor()).
    	process((exchange) -> {
			String uuid = UUID.randomUUID().toString();
			exchange.getIn().setHeader("CareManagementID", uuid);
		}).
    	to("activemq:event.care.tasks");   	
    }
}