package ws.cogito.test.saas;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * Mocking a SAAS Endoint for testing Camel Circuit Breaker
 * @author jeremydeane
 */
@RestController
@RequestMapping("/")
public class EventController {
	
	private static final Logger logger = LoggerFactory.getLogger
			(EventController.class);
	
	/**
	 * Process Event
	 * @throws Exception
	 */
	@RequestMapping(value = "event", method=RequestMethod.GET)
	public String handleEvent (HttpServletResponse response) throws Exception {
		
		String message = "Processing Event " + UUID.randomUUID();
		
		logger.debug(message);
		
	    response.setContentType("text/plain");
	    response.setCharacterEncoding("UTF-8");
		
		return message;
	}	
}