package net.hka.examples.thymeleaf.web.controller.api.v1;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import net.hka.examples.thymeleaf.dto.model.TaskDto;

@Component
public class TaskModelAssembler implements RepresentationModelAssembler<TaskDto, EntityModel<TaskDto>> {
	@SuppressWarnings("deprecation")
	@Override
	public EntityModel<TaskDto> toModel(TaskDto taskDto) {
		
		return new EntityModel<>(taskDto,
			      linkTo(methodOn(TaskRestController.class).one(taskDto.getId())).withSelfRel(),
			      linkTo(methodOn(TaskRestController.class).all()).withRel("tasks"));
				
	}
}
