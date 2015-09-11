package ws.cogito.microservices;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class EventAuditingRouteBuilderTest extends CamelTestSupport {

	
	@Override
	protected CamelContext createCamelContext() throws Exception {
		
		CamelContext context = super.createCamelContext();	

		//setup routes to Mock Endpoints
		context.addRoutes(new RouteBuilder() {
			
			public void configure() {
		    	
		    	from("direct:audit").
		    	process(new TrackingIdProcessor()).
		    	wireTap("mock:cep").
		    	to("file:target/events?fileName=event-${in.header.TrackingID}.json");
			}
		});		

		return context;
	}
	
	@Override
	public void setUp() throws Exception {
		
		super.setUp();

		FileUtils.deleteDirectory(new File ("target/events"));	
	}

	@Test
	public void testRoutingLogic() throws Exception {
		
		//Set expectations
		MockEndpoint cep = getMockEndpoint("mock:cep");
		cep.expectedMessageCount(3);
		
		
		//send encounters
		String encounter1 = IOUtils.toString(this.getClass().
				getResourceAsStream("encounter1.json"), "UTF-8");
		
		String encounter2 = IOUtils.toString(this.getClass().
				getResourceAsStream("encounter2.json"), "UTF-8");
	
		String encounter3 = IOUtils.toString(this.getClass().
				getResourceAsStream("encounter3.json"), "UTF-8");
		
		template.sendBody("direct:audit", encounter1);
		template.sendBody("direct:audit", encounter2);
		template.sendBody("direct:audit", encounter3);
		
		//verify routing
		assertEquals(3, new File("target/events").listFiles().length);
		
		cep.assertIsSatisfied();
	}	
}
