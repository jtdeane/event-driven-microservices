package ws.cogito.microservices;

import java.util.UUID;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class EventCareManagementRouteBuilderTest extends CamelTestSupport {
	
	@Override
	protected CamelContext createCamelContext() throws Exception {
		
		CamelContext context = super.createCamelContext();	

		//setup routes to Mock Endpoints
		context.addRoutes(new RouteBuilder() {
			
			public void configure() {

		    	from("direct:inpatient").
		    	log(LoggingLevel.DEBUG, "Event ${body}").
		    	process(new TrackingIdProcessor()).
		    	process((exchange) -> {
	    			String uuid = UUID.randomUUID().toString();
	    			exchange.getIn().setHeader("CareManagementID", uuid);
				}).
		    	to("mock:tasks");	    	
			}
		});		

		return context;
	}
	
	@Test
	public void testProcessorLogic() throws Exception {
		
		//Set expectations
		MockEndpoint tasks = getMockEndpoint("mock:tasks");
		tasks.expectedMessageCount(1);
		
		//read and send inpatient encounter
		String encounter2 = IOUtils.toString(this.getClass().
				getResourceAsStream("encounter2.json"), "UTF-8");
		
		template.sendBody("direct:inpatient", encounter2);
		
		//verify processing
		tasks.assertIsSatisfied();
		
		String careManagementId = (String) tasks.getExchanges().get(0).getIn().
				getHeader("CareManagementID");
		
		log.debug("Care Management ID: " + careManagementId);
		
		//Tracking ID should not be null
		assertNotNull(careManagementId);
	}	
}	