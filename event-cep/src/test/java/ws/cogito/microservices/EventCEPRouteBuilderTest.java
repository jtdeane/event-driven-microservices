package ws.cogito.microservices;

import java.util.Arrays;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.jayway.jsonpath.JsonPath;

public class EventCEPRouteBuilderTest extends CamelTestSupport {
	
	static final List<String> patients = Arrays.asList
			("Rick Deckard","Hannibal Chew", "Eldon Tyrell");

	@Override
	protected CamelContext createCamelContext() throws Exception {
		
		CamelContext context = super.createCamelContext();	

		//setup routes to Mock Endpoints
		context.addRoutes(new RouteBuilder() {
			
			public void configure() {
		    	
		    	from("direct:cep").
		    	process(new TrackingIdProcessor()).
		    	process(exchange -> {
		    			
		    			String json = (String) exchange.getIn().getBody();
		    			String patient = JsonPath.read(json,"$.patient.display");
		    			
		    			if (patients.contains(patient)) {   				
		    				exchange.getIn().setHeader("Fraud", "true");
		    			} else {
		    				exchange.getIn().setHeader("Fraud", "false");
		    			}
		    	}).
		    	choice().
					when().simple("${in.header.Fraud} contains 'true'").
						to("mock:fraud").
						log("FRAUD ALERT!!" + "${body}").
					otherwise().
						log("...off into the ether"); 
			}
		});		

		return context;
	}
	
	@Test
	public void testRoutingLogic() throws Exception {
		
		//Set expectations
		MockEndpoint cep = getMockEndpoint("mock:fraud");
		cep.expectedMessageCount(1);		
		
		//send encounters
		String encounter1 = IOUtils.toString(this.getClass().
				getResourceAsStream("encounter1.json"), "UTF-8");
		
		String encounter2 = IOUtils.toString(this.getClass().
				getResourceAsStream("encounter2.json"), "UTF-8");
	
		String encounter3 = IOUtils.toString(this.getClass().
				getResourceAsStream("encounter3.json"), "UTF-8");
		
		template.sendBody("direct:cep", encounter1);
		template.sendBody("direct:cep", encounter2);
		template.sendBody("direct:cep", encounter3);
		
		//verify routing
		cep.assertIsSatisfied();
	}
}
