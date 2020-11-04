package net.hka.examples.thymeleaf.web.controller;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.hka.examples.thymeleaf.business.dto.TaskDto;
import net.hka.examples.thymeleaf.business.service.TaskService;

/**
 * Layout Dialect usage example.
 */
@Controller
@Secured("ROLE_USER")
class TaskController_LayoutDialect {

    private final TaskService taskService;

    TaskController_LayoutDialect(TaskService taskService) {
        this.taskService = taskService;
    }

    @ModelAttribute("module")
    String module() {
        return "tasks-ld";
    }

    @RequestMapping(value = "task-ld", method = RequestMethod.GET)
    String tasks(Model model) {
    	
        model.addAttribute("tasks", taskService.findAll());
        return "task-ld/task-list";
    }

    @RequestMapping(value = "task-ld/{id}", method = RequestMethod.GET)
    String task(@PathVariable("id") Long id, Model model) {
    	
    	TaskDto task = taskService.findById(id).orElseThrow(EntityNotFoundException::new);
        model.addAttribute("task", task);
        return "task-ld/task";
    }
}
