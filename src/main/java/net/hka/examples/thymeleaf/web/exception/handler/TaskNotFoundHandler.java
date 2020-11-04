package net.hka.examples.thymeleaf.web.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import net.hka.examples.thymeleaf.business.exception.TaskNotFoundException;

@ControllerAdvice
public class TaskNotFoundHandler {
	
	@ResponseBody
	@ExceptionHandler(TaskNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String taskNotFoundHandler(final TaskNotFoundException ex) {
		
		return ex.getMessage();
	}
}
