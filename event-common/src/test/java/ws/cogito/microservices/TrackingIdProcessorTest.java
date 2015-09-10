package ws.cogito.microservices;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class TrackingIdProcessorTest extends CamelTestSupport {

	@Override
	protected CamelContext createCamelContext() throws Exception {
		
		CamelContext context = super.createCamelContext();	

		//setup routes to Mock Endpoints
		context.addRoutes(new RouteBuilder() {
			
			public void configure() {
		    	
				//test xpath split
		    	from ("direct:event").
		    		process(new TrackingIdProcessor()).
		    	to("mock:order");		    	
			}
		});	
		
		return context;
	}
	
	@Test
	public void testTrackingIdCreateLogic() throws Exception {
		
		//Set expectations
		MockEndpoint event = getMockEndpoint("mock:order");
		event.expectedMessageCount(1);
		
		//event.expectedHeaderReceived(name, value);
		
		//read event
		String json = IOUtils.toString(this.getClass().
				getResourceAsStream("encounter1.json"), "UTF-8");
		
		//execute the test
		template.sendBody("direct:event", json);
		
		String trackingId = (String) event.getExchanges().get(0).getIn().
				getHeader(TrackingIdProcessor.trackingId);
		
		//Tracking ID should not be null
		assertNotNull(trackingId);
		
		//should be one event
		event.assertIsSatisfied();
	}
	
	@Test
	public void testTrackingIdLogicExists() throws Exception {
		
		//Set expectations
		MockEndpoint event = getMockEndpoint("mock:order");
		event.expectedMessageCount(1);
		
		String expectedTrackingId = "Foo12345";
		
		//event.expectedHeaderReceived(name, value);
		
		//read event
		String json = IOUtils.toString(this.getClass().
				getResourceAsStream("encounter1.json"), "UTF-8");
		
		//execute the test
		template.sendBodyAndHeader("direct:event", json, TrackingIdProcessor.trackingId, expectedTrackingId);
		
		event.expectedHeaderReceived(TrackingIdProcessor.trackingId, expectedTrackingId);
		
		//should be one event
		event.assertIsSatisfied();
	}
}