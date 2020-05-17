package net.hka.examples.thymeleaf.error.exception;

@SuppressWarnings("serial")
public class TaskNotFoundException extends RuntimeException {

	public TaskNotFoundException(Long id) {
		super("Could not find task " + id);
	}
}
