package net.hka.examples.thymeleaf.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import net.hka.examples.thymeleaf.dto.TaskDto;

/**
 * Creates and sends the HTTP requests which are used
 * to write unit tests for controllers methods which
 * provide CRUD operations for task items.
 */
class TaskRequestBuilder {

    private final MockMvc mockMvc;

    TaskRequestBuilder(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    /**
     * Creates and sends the HTTP requests which gets the
     * HTML document that displays the information of all task items.
     * @return
     * @throws Exception
     */
    ResultActions tasks() throws Exception {
        return mockMvc.perform(get("/task").header("X-Requested-With", ""));
    }

    /**
     * Creates and sends the HTTP request which sets the
     * HTML document that displays the new empty task item.
     * @return
     * @throws Exception
     */
    ResultActions newTask() throws Exception {
        return mockMvc.perform(get("/task/new"));
    }
    
    /**
     * Creates and sends the HTTP request which gets the
     * HTML document that displays the information of the
     * requested task item that is ready to be edited.
     * @param id
     * @return
     * @throws Exception
     */
    ResultActions editTask(Long id) throws Exception {
        return mockMvc.perform(get("/task/edit/{id}", id));
    }
    
    
    /**
     * Creates and sends the HTTP request which gets the
     * HTML document that displays the information of the
     * requested task item.
     * @param id    The id of the requested task item.
     * @return
     * @throws Exception
     */
    ResultActions viewTask(Long id) throws Exception {
        return mockMvc.perform(get("/task/{id}", id));
    }
    
    /**
     * Creates and sends the HTTP post request which sets the
     * HTML document that displays the result after posting 
     * the current task to be saved or updated in the database.
     * @return
     * @throws Exception
     */
    ResultActions saveUpdateTask(TaskDto taskDto) throws Exception {
    	return mockMvc.perform(post("/task/save")
    			.param("title", taskDto.getTitle())
    			.param("text", taskDto.getText())
    			.param("dueTo", taskDto.getDueTo())
    			.header("X-Requested-With", ""));
    	
    }
    
    /**
     * Creates and sends the HTTP  delete request which sets the
     * HTML document that displays the task list after deleting 
     * the current task from the database.
     * @param id The id of the requested task item for deletion.
     * @return
     * @throws Exception
     */
    ResultActions deleteTask(Long id) throws Exception {
        return mockMvc.perform(delete("/task/{id}", id).header("X-Requested-With", ""));
    }
}
