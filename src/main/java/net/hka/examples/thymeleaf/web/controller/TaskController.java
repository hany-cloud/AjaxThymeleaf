package net.hka.examples.thymeleaf.web.controller;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.hka.examples.thymeleaf.business.service.TaskService;
import net.hka.examples.thymeleaf.dto.model.BaseModel;
import net.hka.examples.thymeleaf.dto.model.TaskDto;
import net.hka.examples.thymeleaf.error.exception.TaskNotFoundException;
import net.hka.examples.thymeleaf.web.support.AjaxUtils;

/**
 * Standard Layout System with Fragment Expressions usage example
 * with AJAX implementation.
 * Provides CRUD operations through AJAX for task items.
 */
@Controller
@Secured("ROLE_USER")
class TaskController {

	// views
	private static final String TASK_LIST_VIEW_NAME = "task/task-list";
	private static final String TASK_DETAILS_VIEW_NAME = "task/task-details";
	private static final String TASK_FORM_VIEW_NAME = "task/task-form";
	
	// fragments
	private static final String TASK_LIST_FRAGMENT = "taskListTableFragment";
	private static final String TASK_FORM_FRAGMENT = "taskFormFragment";
	
	// link signup messages by message resource
	private static final String FORM_CONTAINS_ERR_MESSAGE = "form.contains.errors";
	//private static final String DATA_SUBMITTED_SUCCESS_MESSAGE = "data.submitted.success";
	
	private final TaskService taskService;

	TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	/*
	 * adding this model attribute to handle active menu - check the header.html file
	 */
	@ModelAttribute("module")
	String module() {
		return "tasks";
	}
	
	/*
	 * adding this model attribute to get current time stamp
	 */
	@ModelAttribute("currentTimeStamp")
	String currentTimeStamp() {
		return new Date().toString();
	}

	/**
     * Renders the HTML view that displays the information of all
     * task items found from the database.
     * @param model The model that contains the attributes which are
     *              required to render the HTML view.
     * @param requestedWith This is a header parameter for AJAX request 
     *                      with key value pairs X-Requested-With: XMLHttpRequest             
     * @return  The name of the rendered HTML view.
     */
	@RequestMapping(value = "task", method = RequestMethod.GET)
	String tasks(Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		
		// fetch all tasks
		model.addAttribute("tasks", taskService.findAll());
		
		// return back to the same view 
		if (AjaxUtils.isAjaxRequest(requestedWith)) {
			return TASK_LIST_VIEW_NAME.concat(" :: ").concat(TASK_LIST_FRAGMENT);
		}
		return TASK_LIST_VIEW_NAME;
	}
	
	/**
     * Renders the HTML view that displays the view for inserting new task.
     * @param model The model that contains the attributes which are
     *              required to render the HTML view.
     * @return      The name of the rendered HTML view.
     */
	@RequestMapping(value = "task/new", method = RequestMethod.GET)
	String newTask(Model model) {
		model.addAttribute("task", new TaskDto());
		return TASK_FORM_VIEW_NAME;
	}
	
	/**
     * Renders the HTML edit view that displays the information of the
     * requested task item that is ready for editing.
     * @param id    The id of the requested task item.
     * @param model The model that contains the attributes which are
     *              required to render the HTML view.
     * @return      The name of the rendered HTML view.
     * @throws net.petrikainulainen.springmvctest.junit5.todo.TaskNotFoundException if the requested task item isn't found from the database.
     */
	@RequestMapping(value = "task/edit/{id}", method = RequestMethod.GET)
	String editTask(@PathVariable("id") Long id, Model model) {
		
		// fetch task by task id and add task attribute to the model
		this.prepareTask(id, model);
		
		// open the view 
		return TASK_FORM_VIEW_NAME;
	}
	
	/**
     * Renders the HTML view that displays the information of the
     * requested task item.
     * @param id    The id of the requested task item.
     * @param model The model that contains the attributes which are
     *              required to render the HTML view.
     * @return      The name of the rendered HTML view.
     * @throws net.petrikainulainen.springmvctest.junit5.todo.TaskNotFoundException if the requested task item isn't found from the database.
     */
	@RequestMapping(value = "task/{id}", method = RequestMethod.GET)
	String viewTask(@PathVariable("id") Long id, Model model) {
		// fetch task by task id and add task attribute to the model
		this.prepareTask(id, model);
		
		// open the view
		return TASK_DETAILS_VIEW_NAME;
	}
	
	/**
	 * 
	 * @param taskDto The taskDto object that is need to be saved or updated to the database
	 * @param result General interface that represents binding results for error handling purposes
	 * @param model The model that contains the attributes which are
     *              required to render the HTML view.
	 * @param requestedWith This is a header parameter for AJAX request 
     *                      with key value pairs X-Requested-With: XMLHttpRequest  
	 * @return The name of the rendered HTML view.
	 */
	@RequestMapping(value = {"task/save", "task/edit/save"}, method = RequestMethod.POST)
	String saveUpdateTask(@Valid @ModelAttribute("task") TaskDto taskDto, BindingResult result, 
			/*RedirectAttributes ra,*/ Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		
		// if any errors, re-render before going to save data to database
	    if (result.hasErrors()) {
	    	if (AjaxUtils.isAjaxRequest(requestedWith)) {
				return TASK_FORM_VIEW_NAME.concat(" :: ").concat(TASK_FORM_FRAGMENT);
			}
			return TASK_FORM_VIEW_NAME;
	    }
		
	    // save/update task
	    TaskDto savedTaskDto = taskService.save(taskDto);
	    	    
 		// data didn't saved
 		if(savedTaskDto.getId() == null) {
 			result.reject(FORM_CONTAINS_ERR_MESSAGE, "Form contains errors. Please try again.");		
 		} else { // data is saved
	 		// clear the binded task on the view
 			model.addAttribute("task", new TaskDto());
 		}
 		
 		// return back to the same view
 		if (AjaxUtils.isAjaxRequest(requestedWith)) {
			return TASK_FORM_VIEW_NAME.concat(" :: ").concat(TASK_FORM_FRAGMENT);
		}
 		//return "redirect:/";
		return TASK_FORM_VIEW_NAME;
	}
	
	/**
	 * 
	 * @param id    The id of the requested task item that needs to be deleted.
	 * @param model The model that contains the attributes which are
     *              required to render the HTML view.
	 * @param requestedWith This is a header parameter for AJAX request 
     *                      with key value pairs X-Requested-With: XMLHttpRequest  
	 * @return The name of the rendered HTML view.
	 */
	@RequestMapping(value = "task/{id}", method = RequestMethod.DELETE)
	String deleteTask(@PathVariable("id") Long id, Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		
		// fetch task by task id
		taskService.findById(id)
				.orElseThrow(() -> new TaskNotFoundException(id));
		
		// delete task
		taskService.delete(id);
		
		model.addAttribute("tasks", taskService.findAll());
		
		// return back to the same view
		if (AjaxUtils.isAjaxRequest(requestedWith)) {
			return TASK_LIST_VIEW_NAME.concat(" :: ").concat(TASK_LIST_FRAGMENT);
		}
		//return "redirect:/";
		return TASK_LIST_VIEW_NAME;		
	}
	
	
	private void prepareTask(Long id, Model model) {
		// fetch task by task id
		TaskDto task = taskService.findById(id)
				.orElseThrow(() -> new TaskNotFoundException(id));
		
		// add task attribute to the model
		task.setDueTo(BaseModel.dateFormat(task.getDueTo()));
		model.addAttribute("task", task);
	}
}
