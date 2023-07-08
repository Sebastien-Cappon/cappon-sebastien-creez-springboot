package com.safetynet.alerts;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class of the application. It contains only the run method. This project
 * is a SpringBoot Application. This annotation is a merged of
 * <code>@EnableAutoConfiguration</code>, <code>@ComponentScan</code> and
 * <code>@Configuration</code> with their default configuration.
 *
 * @author Sébastien Cappon
 * @version 1.0
 */
@SpringBootApplication
public class AlertsApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(AlertsApplication.class, args);
	}
}