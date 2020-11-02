package net.hka.common.web.servlet.mvc.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * Global exception handler for the Web application.
 * Render any uncaught exception from any part of the system to the error page.
 * @author Hany Kamal
 */
@ControllerAdvice
public class MvcExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger("ErrorLog");
	
	private static final String ERROR_VIEW_NAME = "error/general";

	/**
	 * Handle exceptions thrown from any part of the system.
	 * @param {@Link Exception} the uncaught exception that is thrown from any part of the system    
	 * @return {@Link ModelAndView} a general error page that is displaying the message of the thrown exception   
	 */
	@ExceptionHandler(value = Exception.class)
	public ModelAndView handleException(final Exception exception) {
		
		ModelAndView modelAndView = new ModelAndView(ERROR_VIEW_NAME);
		modelAndView.addObject("errorMessage", exception.getMessage());
		logger.warn("handleException : {}", exception.getMessage());
		return modelAndView;
	}
	
}