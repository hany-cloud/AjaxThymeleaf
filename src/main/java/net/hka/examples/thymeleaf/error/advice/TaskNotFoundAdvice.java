package net.hka.examples.thymeleaf.error.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import net.hka.examples.thymeleaf.error.exception.TaskNotFoundException;

@ControllerAdvice
public class TaskNotFoundAdvice {
	@ResponseBody
	@ExceptionHandler(TaskNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String taskNotFoundHandler(TaskNotFoundException ex) {
		return ex.getMessage();
	}
}
