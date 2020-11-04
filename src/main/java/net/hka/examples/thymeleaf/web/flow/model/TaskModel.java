package net.hka.examples.thymeleaf.web.flow.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.validation.ValidationContext;
import org.springframework.stereotype.Component;

import net.hka.common.validation.ModelValidator;
import net.hka.examples.thymeleaf.business.dto.TaskDto;

@SuppressWarnings("serial")
@Component
public class TaskModel implements Serializable {
	
	@Autowired
	private ModelValidator<TaskDto> webFlowValidator;

	private Iterable<TaskDto> tasks;
	private TaskDto task; 
	private String currentDate;
	private boolean showTaskForm;
	private boolean hasErrors;
		
	public TaskModel() {
		
	}
	public TaskModel(Iterable<TaskDto> iterable) {
		
		super();
		this.tasks = iterable;
		this.task = new TaskDto();
		this.getCurrentDate();
		this.showTaskForm = false;
		this.hasErrors = false;
	}

	public boolean isHasErrors() {
		return hasErrors;
	}
	public void setHasErrors(final boolean hasErrors) {
		this.hasErrors = hasErrors;
	}
	public Iterable<TaskDto> getTasks() {
		return tasks == null ? new ArrayList<TaskDto>() : tasks;
	}

	public void setTasks(final Iterable<TaskDto> tasks) {
		this.tasks = tasks;
	}	
	
	
	public boolean isShowTaskForm() {
		return showTaskForm;
	}

	public void setShowTaskForm(final boolean showTaskForm) {
		this.showTaskForm = showTaskForm;
	}

	public String getCurrentDate() {
		currentDate = new Date().toString();
		return currentDate;
	}

	public void setCurrentDate(final String currentDate) {
		this.currentDate = currentDate;
	}
	
	
	public TaskDto getTask() {
		return task;
	}

	public void setTask(final TaskDto task) {
		this.task = task; 
	}
	
	
	
	/*
	 * this is the approach that can be used to validate the task model with web-flow regular transitions, 
	 * but in case of AJAX and partial view rendering this will not helpful and AJAX will fail 
	 * and fragments re-rendering will not work. 
	 */
	public void validateTaskList(ValidationContext context) {
		// to validate the bean against it's annotations   
		webFlowValidator.validateModel(this.getTask(), context);
    }
	
}
