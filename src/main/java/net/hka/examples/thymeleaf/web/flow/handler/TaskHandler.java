package net.hka.examples.thymeleaf.web.flow.handler;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.stereotype.Component;

import net.hka.common.validation.ModelValidator;
import net.hka.examples.thymeleaf.web.dto.TaskDto;
import net.hka.examples.thymeleaf.web.flow.model.TaskModel;
import net.hka.examples.thymeleaf.web.service.TaskService;

@Component
public class TaskHandler {
	
	private static final Logger logger = LoggerFactory.getLogger("ErrorLog");
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private ModelValidator<TaskDto> modelValidator;
	
	public TaskModel init() {	
		return new TaskModel(taskService.findAll());
	}
	
	public TaskModel findAll(TaskModel model) {	
		
		model.setTasks(taskService.findAll());

		model.setHasErrors(false);
		
		this.clearTask(model);		
		this.hideTaskForm(model);
		
		return model;
	}
		
	public String setTask(TaskModel model, int taskId) {
		
		String transitionValue = "failure";
		for (TaskDto task : model.getTasks()) {
			if(task.getId() == taskId) {
				model.setTask(task);
				transitionValue = "success";
				return transitionValue;
			}
		}
		
		return transitionValue;
	}
	
	/*public TaskModel addNewTask(TaskModel model) {
		this.clearTask(model);
		this.toggleTaskForm(model);
		return model;
	}*/
	
	public TaskModel saveTask(TaskModel model, MessageContext messages) {
		
		TaskDto taskDto = model.getTask();		
		// to validate the taskDto bean against it's annotations 
		Set<ConstraintViolation<TaskDto>> violations = modelValidator.validateModel(taskDto, messages); //validator.validate(taskDto);	
		model.setHasErrors(violations != null && violations.size() > 0);
		if(!model.isHasErrors()) {
			// saving the task to database
			TaskDto savedTask = taskService.save(taskDto);
			logger.info("Saved Task: " + savedTask.toString());
			
			// refresh the list to view the new added task
			model.setTasks(taskService.findAll()); // here you can simply add the task to the list
			
			messages.addMessage(new MessageBuilder().info().defaultText("Data is saved").build());
			
			this.clearTask(model);			
		}
		
		model.setShowTaskForm(model.isHasErrors());
		
		return model;
	}
		
	public TaskModel deleteTask(TaskModel model, MessageContext messages) {
		
		taskService.delete(model.getTask().getId());
		
		model.setTasks(taskService.findAll());
		
		messages.addMessage(new MessageBuilder().info().defaultText("Data is deleted").build());
		
		model.setHasErrors(false);
		
		this.clearTask(model);		
		this.hideTaskForm(model);
		
		return model;
	}
	
	/*
	 * Helper methods
	 */
	private void clearTask(TaskModel model) {
		model.setTask(new TaskDto());		
	}
	
	private void hideTaskForm(TaskModel model) {
		model.setShowTaskForm(false);
	}
		
	/*private void toggleTaskForm(TaskModel model) {
		model.setShowTaskForm(!model.isShowTaskForm());
	}*/
	
}
