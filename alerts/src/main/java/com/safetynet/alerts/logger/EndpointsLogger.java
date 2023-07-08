package com.safetynet.alerts.logger;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import com.safetynet.alerts.constants.UriToIgnore;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * A class that contains two methods for listening the requests passed to the
 * API and the responses returned.
 *
 * @author Sébastien Cappon
 * @version 1.0
 */
public class EndpointsLogger implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(EndpointsLogger.class);

	/**
	 * A method that listens to requests sent to the API and logs them to <code>INFO</code> level
	 *
	 * @return <code>boolean</code>
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String requestURL = java.net.URLDecoder.decode(request.getRequestURL().toString(), StandardCharsets.UTF_8);

		if (!UriToIgnore.uriToIgnore.contains(request.getRequestURI())) {
			if (!request.getParameterMap().isEmpty()) {
				String requestParameters = "?" + request.getParameterMap().entrySet().stream()
						.map(e -> e.getKey() + "=" + String.join(", ", e.getValue())).collect(Collectors.joining(" "));

				logger.info("URL requested : {} {}{}", request.getMethod(), requestURL, requestParameters);
			} else {
				logger.info("Endpoint requested : {} {}", request.getMethod(), requestURL);
			}
		}

		return true;
	}

	/**
	 * A method which listen requests responses returned by the API and log them to
	 * <code>INFO</code> level if the status code is 2xx or <code>ERROR</code> level if the status code is 4xx
	 * or 5xx.
	 *
	 * @return <code>void</code>
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception exception) throws Exception {
		int responseStatus = response.getStatus();

		if (!UriToIgnore.uriToIgnore.contains(request.getRequestURI())) {
			switch (responseStatus) {
			case 200:
				logger.info("Response : Status {} OK", responseStatus);
				break;
			case 201:
				String responseLocation = java.net.URLDecoder.decode(response.getHeader("Location"),
						StandardCharsets.UTF_8);
				logger.info("Response : Status {} Created - Location : {}", responseStatus, responseLocation);
				break;
			case 204:
				logger.info("Response : Status {} No Content - The request is fine, the response is empty.",
						responseStatus);
				break;
			case 400:
				logger.error(
						"Response : Status {} Bad Request - The request is wrong. Please check the body and the parameters.",
						responseStatus);
				break;
			case 404:
				logger.error(
						"Response : Status {} Not Found - The request is wrong. Please check the URL, not the parameters or variables.",
						responseStatus);
				break;
			case 500:
				logger.error(
						"Response : Status {} Internal Server Error - Server side problem, the request is probably fine.",
						responseStatus);
				break;
			default:
				logger.error("Status unknown");
			}
		}
	}
}
