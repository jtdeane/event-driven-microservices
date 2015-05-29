package ws.cogito.microservices;

import java.util.Arrays;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import com.jayway.jsonpath.JsonPath;


/**
 * Event Complext Event Processing Route Builder
 */
public class EventCEPRouteBuilder extends RouteBuilder {
	
	static final List<String> patients = Arrays.asList
			("Rick Deckard","Hannibal Chew", "Eldon Tyrell");

    public void configure() {
    	
    	/**
    	 * Route errors to DLQ after one retry and one second delay
    	 */
    	errorHandler(deadLetterChannel("activemq:event.care.dlq").
    			maximumRedeliveries(1).redeliveryDelay(1000));
    	
    	/**
    	 * Complex Event Processing (CEP) - Check fradulent paitent list
    	 */
    	from("activemq:event.cep").
    	process(new TrackingIdProcessor()).
    	process(new Processor() {
    		public void process(Exchange exchange) {
    			
    			String json = (String) exchange.getIn().getBody();
    			String patient = JsonPath.read(json,"$.patient.display");
    			
    			if (patients.contains(patient)) {   				
    				exchange.getIn().setHeader("Fraud", "true");
    			} else {
    				exchange.getIn().setHeader("Fraud", "false");
    			}
    		}
    	}).
    	choice().
			when().simple("${in.header.Fraud} contains 'true'").
				to("activemq:topic:event.fraud.alert").
			otherwise().
				log("...off into the ether");   	
    }
}