package ws.cogito.microservices;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Standalone Microservice Application for Event Ingestion
 * @author jeremydeane
 */
@Configuration
@ImportResource("classpath:camel-route-spring.xml")
public class EventCEPRouterApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventCEPRouterApplication.class, args);
	}
}