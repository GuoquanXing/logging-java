package com.sap.cc.greeting;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import org.slf4j.*;

@RestController
public class GreetingController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private GreetingService service;

	public GreetingController(GreetingService service) {
		this.service = service;
	}

	@GetMapping("/hello")
	public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		MDC.put("path", "/hello");
		return getGreeting("Hello", name);
	}

	@GetMapping("/howdy")
	public String deprecatedGreeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		MDC.put("path", "/howdy");
		logger.info("Deprecated endpoint used.");
		return getGreeting("Howdy", name);
	}

	private String getGreeting(String greeting, String name) {
		try {
			return service.createGreetingMessage(greeting, name);
		} catch (IllegalArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		} finally {
			MDC.clear();
		}
	}

}
