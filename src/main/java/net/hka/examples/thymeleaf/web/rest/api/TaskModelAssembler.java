package net.hka.examples.thymeleaf.web.rest.api;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import net.hka.examples.thymeleaf.dto.TaskDto;
import net.hka.examples.thymeleaf.web.rest.api.controller.TaskApiController;

@Component
public class TaskModelAssembler implements RepresentationModelAssembler<TaskDto, EntityModel<TaskDto>> {
	@Override
	public EntityModel<TaskDto> toModel(TaskDto taskDto) {
		
		return new EntityModel<>(taskDto,
			      linkTo(methodOn(TaskApiController.class).one(taskDto.getId())).withSelfRel(),
			      linkTo(methodOn(TaskApiController.class).all()).withRel("tasks"));
				
	}
}
