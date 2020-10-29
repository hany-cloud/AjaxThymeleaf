package net.hka.examples.thymeleaf.web.rest.api.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.hka.examples.thymeleaf.business.service.TaskService;
import net.hka.examples.thymeleaf.dto.model.TaskDto;
import net.hka.examples.thymeleaf.error.exception.TaskNotFoundException;
import net.hka.examples.thymeleaf.web.rest.api.TaskModelAssembler;

/**
 * Hypermedia-Driven RESTful Web Service to handle all CRUD operations for the task items
 */
@RestController
@RequestMapping("todo/v1/tasks")
@Secured("ROLE_USER")
public class TaskApiController {

	private final TaskService taskService;

	private final TaskModelAssembler assembler;

	TaskApiController(TaskService taskService, TaskModelAssembler assembler) {
		this.taskService = taskService;
		this.assembler = assembler;
	}
	
	// Aggregate root
	@GetMapping
	public CollectionModel<EntityModel<TaskDto>> all() {
		List<EntityModel<TaskDto>> taskDtos = StreamSupport.stream(taskService.findAll().spliterator(), false)
				.map(assembler::toModel).collect(Collectors.toList());

		return new CollectionModel<>(taskDtos, linkTo(methodOn(TaskApiController.class).all()).withSelfRel());
	}
	
	// Single item
	@GetMapping("/{id}")
	public EntityModel<TaskDto> one(@PathVariable Long id) {
		TaskDto taskDto = taskService.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
		return assembler.toModel(taskDto);
	}


	@PostMapping
	ResponseEntity<?> newTask(@RequestBody TaskDto newTaskDto) throws URISyntaxException {
		EntityModel<TaskDto> entityModel = assembler.toModel(taskService.save(newTaskDto));

		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	
	@PutMapping("/{id}")
	ResponseEntity<?> replaceTask(@RequestBody TaskDto newTaskDto, @PathVariable Long id) throws URISyntaxException {

		TaskDto updatedTaskDto = taskService.findById(id).map(taskDto -> {
			taskDto.setTitle(newTaskDto.getTitle());
			taskDto.setText(newTaskDto.getText());
			taskDto.setDueTo(newTaskDto.getDueTo());
			return taskService.save(taskDto); // save
		}).orElseGet(() -> {
			newTaskDto.setId(id);
			return taskService.save(newTaskDto); // update
		});

		EntityModel<TaskDto> entityModel = assembler.toModel(updatedTaskDto);

		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}

	@DeleteMapping("/{id}")
	ResponseEntity<?> deleteTask(@PathVariable Long id) {
		taskService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
