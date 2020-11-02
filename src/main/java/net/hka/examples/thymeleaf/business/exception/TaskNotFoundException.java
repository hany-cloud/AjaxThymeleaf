package net.hka.examples.thymeleaf.business.exception;

@SuppressWarnings("serial")
public class TaskNotFoundException extends RuntimeException {

	public TaskNotFoundException(final Long id) {
		
		super("Could not find task " + id);
	}
}
