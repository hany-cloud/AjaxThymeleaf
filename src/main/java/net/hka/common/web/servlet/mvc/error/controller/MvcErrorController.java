package net.hka.common.web.servlet.mvc.error.controller;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * A general MVC controller handles an error page.
 * And mapping all errors to Spring MVC handler method.
 * @author Hany Kamal
 *
 */
@Controller
final class MvcErrorController {
	
	private static final Logger logger = LoggerFactory.getLogger("ErrorLog");
	
	private static final String ERROR_VIEW_NAME = "error/general";
	
	/**
	 * Display an error page.
	 * Map all errors to Spring MVC handler method.
	 * @param request {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @param model {@link Model}
	 * @return string for the general error page name of the View to render, to be resolved by the DispatcherServlet's ViewResolver
	 */
	@RequestMapping("generalError")
	public String generalError(final HttpServletRequest request, final HttpServletResponse response, Model model) {
		
		if(request == null) throw new IllegalArgumentException("The request paremter is null");
		if(response == null) throw new IllegalArgumentException("The response paremter is null");
		if(model == null) throw new IllegalArgumentException("The model paremter is null");
		
		// retrieve some useful information from the request
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
		// String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");
		String exceptionMessage = this.getExceptionMessage(throwable, statusCode);

		String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
		if (requestUri == null) {
			requestUri = "Unknown";
		}

		String message = MessageFormat.format("{0} returned for {1} with message {2}",
			statusCode, requestUri, exceptionMessage
		);
		
		model.addAttribute("errorMessage", message);
		
		logger.warn("generalError : {}", message);
		
        return ERROR_VIEW_NAME;
	}

	private String getExceptionMessage(final Throwable throwable, final Integer statusCode) {
		
		if (throwable != null) {
			return throwable.getCause().getMessage();
		}
		HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
		return httpStatus.getReasonPhrase();
	}
}
