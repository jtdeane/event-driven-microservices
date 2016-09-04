package ws.cogito.microservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Standalone Microservice Application for Complex Event Processing
 * @author jeremydeane
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@ImportResource("classpath:camel-route-spring.xml")
public class EventCEPRouterApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventCEPRouterApplication.class, args);
	}
}