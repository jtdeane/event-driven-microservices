package ws.cogito.microservices;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class EventIngestionRouteBuilderTest extends CamelTestSupport {

	
	@Override
	protected CamelContext createCamelContext() throws Exception {
		
		CamelContext context = super.createCamelContext();	

		//setup routes to Mock Endpoints
		context.addRoutes(new RouteBuilder() {
			
			public void configure() {
		    	
		    	from("direct:ingestion").
		    	log(LoggingLevel.DEBUG, "Order ${body}").
		    	process(new TrackingIdProcessor()).
		    	choice().
		    		when().jsonpath("$[?(@.class==inpatient)]").
		    			to("mock:inpatient").
		    		otherwise().
		    			to("mock:outpatient").
					end().
		    	to("mock:audit");	    	
			}
		});		

		return context;
	}
	
	@Test
	public void testChoiceLogic() throws Exception {
		
		//Set expectations
		MockEndpoint inpatient = getMockEndpoint("mock:inpatient");
		inpatient.expectedMessageCount(1);

		MockEndpoint outpatient = getMockEndpoint("mock:outpatient");
		outpatient.expectedMessageCount(2);
		
		MockEndpoint audit = getMockEndpoint("mock:audit");
		audit.expectedMessageCount(3);	
		
		//ingest encounters
		String encounter1 = IOUtils.toString(this.getClass().
				getResourceAsStream("encounter1.json"), "UTF-8");
		
		String encounter2 = IOUtils.toString(this.getClass().
				getResourceAsStream("encounter2.json"), "UTF-8");
	
		String encounter3 = IOUtils.toString(this.getClass().
				getResourceAsStream("encounter3.json"), "UTF-8");
		
		template.sendBody("direct:ingestion", encounter1);
		template.sendBody("direct:ingestion", encounter2);
		template.sendBody("direct:ingestion", encounter3);
		
		//verify routing
		inpatient.assertIsSatisfied();
		
		String inpatientEncouter = (String) inpatient.getExchanges().get(0).getIn().getBody();
		assertTrue(inpatientEncouter.contains("Roy Batty"));
		
		outpatient.assertIsSatisfied();
		audit.assertIsSatisfied();
	}	
}
