package net.hka.examples.thymeleaf.error.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

//import com.google.common.base.Throwables;

/**
 * General error handler for the Web application.
 */
@ControllerAdvice
class ExceptionHandlerAdvice {
	private static final Logger LOGGER = LoggerFactory.getLogger("ErrorLog");

	/**
	 * Handle exceptions thrown by handlers.
	 */
	@org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
	public ModelAndView exception(Exception exception, WebRequest request) {
		ModelAndView modelAndView = new ModelAndView("error/general");
		// Throwable rootCause =
		// exception.getCause();//Throwables.getRootCause(exception);
		modelAndView.addObject("errorMessage", exception.getMessage());
		// LOGGER.error(rootCause.toString(), exception);
		LOGGER.error(exception.getMessage(), exception);
		return modelAndView;
	}
	
}