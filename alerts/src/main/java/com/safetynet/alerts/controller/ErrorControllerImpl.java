package com.safetynet.alerts.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

/**
 * A class that implements <code>ErrorController</code>, a native SpringBoot
 * class. It receives hidden requests from Spring to the <code>/error</code>
 * page in order to redirect to the HTML error pages corresponding to the
 * correct status code.
 *
 * @author Sébastien Cappon
 * @version 1.0
 */
@Controller
public class ErrorControllerImpl implements ErrorController {

	/**
	 * A <code>GetMapping</code> method on <code>/error</code> URI which looks at
	 * the <code>ERROR_STATUS_CODE</code> attribute in order to redirect to the
	 * corresponding HTLM error page.
	 * 
	 * @return <code>String</code> which is the filename of the right HTML error
	 *         page.
	 */
	@GetMapping("/error")
	public String handleError(HttpServletRequest request) {
		Object requestStatus = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		if (requestStatus != null) {
			Integer statusCode = Integer.valueOf(requestStatus.toString());

			if (statusCode == HttpStatus.BAD_REQUEST.value()) {
				return "error-400";
			} else if (statusCode == HttpStatus.NOT_FOUND.value()) {
				return "error-404";
			} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				return "error-500";
			}
		}
		return "error";
	}
}
