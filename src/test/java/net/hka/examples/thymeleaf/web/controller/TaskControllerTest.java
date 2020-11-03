package net.hka.examples.thymeleaf.web.controller;

import static net.hka.examples.thymeleaf.config.WebTestConfig.exceptionResolver;
import static net.hka.examples.thymeleaf.config.WebTestConfig.fixedLocaleResolver;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
//import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import net.hka.common.web.model.BaseModel;
import net.hka.examples.thymeleaf.business.exception.TaskNotFoundException;
import net.hka.examples.thymeleaf.business.service.TaskService;
import net.hka.examples.thymeleaf.web.dto.TaskDto;

class TaskControllerTest {

	private static final String TASK_LIST_VIEW_NAME = "task/task-list";
	private static final String TASK_DETAILS_VIEW_NAME = "task/task-details";
	private static final String TASK_FORM_VIEW_NAME = "task/task-form";
	
    private TaskRequestBuilder requestBuilder;
    private TaskService service;
    
    @BeforeEach
    void configureSystemUnderTest() {
        service = mock(TaskService.class);
        
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new TaskController(service))
                .setHandlerExceptionResolvers(exceptionResolver())
                .setLocaleResolver(fixedLocaleResolver())
                //.setViewResolvers(jspViewResolver())
                .build();
        requestBuilder = new TaskRequestBuilder(mockMvc);
    }

    @Nested
    @DisplayName("Render the HTML view that displays the information of all task items")
    class Tasks {

    	/**
         * This test ensures that the status code is 200
         */
        @Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            requestBuilder.tasks().andExpect(status().isOk());
        }

        /**
         * This test ensures that the task list view is the view that is rendered
         */
        @Test
        @DisplayName("Should render the task item list view")
        void shouldRenderTaskItemListView() throws Exception {
            requestBuilder.tasks().andExpect(view().name(TASK_LIST_VIEW_NAME));
        }
        
        @Nested
        @DisplayName("When no task items is found from the database")
        class WhenNoTaskItemsIsFoundFromDatabase {

            @BeforeEach
            void serviceReturnsEmptyList() {
                when(service.findAll()).thenReturn(new ArrayList<>());
            }

            /**
             * This test ensures that the list view displays zero tasks 
             * if no task presents in the database
             */
            @Test
            @DisplayName("Should display zero task items")
            void shouldDisplayZeroTaskItems() throws Exception {
                requestBuilder.tasks().andExpect(model().attribute("tasks", hasSize(0)));
            }
        }

        @Nested
        @DisplayName("When two task items are found from the database")
        class WhenTwoTaskItemsAreFoundFromDatabase {

            private static final long TASK_ONE_ID = 1L;
            private static final String TASK_ONE_TITLE = "First task item";
            private static final String TASK_ONE_TEXT = "Text for first task";
			private static final String TASK_ONE_DUE = "2020-05-01";
            
            private static final long TASK_TWO_ID = 2L;
            private static final String TASK_TWO_TITLE = "Second task item";
            private static final String TASK_TWO_TEXT = "Text for second task";
			private static final String TASK_TWO_DUE = "2021-07-20";

            @BeforeEach
            void serviceReturnsTwoTasks() {
            	TaskDto firstDto = new TaskDto(); 
            	firstDto.setId(TASK_ONE_ID);
            	firstDto.setTitle(TASK_ONE_TITLE);
				firstDto.setText(TASK_ONE_TEXT);
				firstDto.setDueTo(TASK_ONE_DUE);
				
				TaskDto secondDto = new TaskDto();
				secondDto.setId(TASK_TWO_ID);
				secondDto.setTitle(TASK_TWO_TITLE);
				secondDto.setText(TASK_TWO_TEXT);
				secondDto.setDueTo(TASK_TWO_DUE);
                
                when(service.findAll()).thenReturn(Arrays.asList(firstDto, secondDto));
            }

            /**
             * This test ensures that the list view displays the information
             * of the exact 2 items.
             */
            @Test
            @DisplayName("Should display two task items")
            void shouldDisplayTwoTaskItems() throws Exception {
                requestBuilder.tasks().andExpect(model().attribute("tasks", hasSize(2)));
            }

            /**
             * These two tests ensure that the list view displays the information
             * of the found task items but they don't guarantee that task items
             * are displayed in the correct order
             */
            @Test
            @DisplayName("Should display the information of the first task item")
            void shouldDisplayInformationOfFirstTaskItem() throws Exception {
            	requestBuilder.tasks()
                        .andExpect(
                                model().attribute("tasks",
                                hasItem(allOf(
                                        hasProperty("id", equalTo(TASK_ONE_ID)),
                                        hasProperty("title", equalTo(TASK_ONE_TITLE)),
                                        hasProperty("text", equalTo(TASK_ONE_TEXT)),
                                        hasProperty("dueTo", equalTo(BaseModel.dateFormat(TASK_ONE_DUE)))
                                )))
                        );
            }

            @Test
            @DisplayName("Should display the information of the second task item")
            void shouldDisplayInformationOfSecondTaskItem() throws Exception {
                requestBuilder.tasks()
                        .andExpect(
                                model().attribute("tasks",
                                        hasItem(allOf(
                                        		hasProperty("id", equalTo(TASK_TWO_ID)),
                                        		hasProperty("title", equalTo(TASK_TWO_TITLE)),
                                                hasProperty("text", equalTo(TASK_TWO_TEXT)),
                                                hasProperty("dueTo", equalTo(BaseModel.dateFormat(TASK_TWO_DUE)))
                                        )))
                        );
            }

            /**
             * This test ensures that the list view displays the information
             * of the found task items in the correct order.
             */
            @Test
            @DisplayName("Should display the information of the first and second task item in the correct order")
            void shouldDisplayFirstAndSecondTaskItemInCorrectOrder() throws Exception {
                requestBuilder.tasks()
                        .andExpect(
                                model().attribute("tasks",
                                		contains(
                                                allOf(
                                                		hasProperty("id", equalTo(TASK_ONE_ID)),
                                                		hasProperty("title", equalTo(TASK_ONE_TITLE)),
                                                        hasProperty("text", equalTo(TASK_ONE_TEXT)),
                                                        hasProperty("dueTo", equalTo(BaseModel.dateFormat(TASK_ONE_DUE)))
                                                ),
                                                allOf(
                                                		hasProperty("id", equalTo(TASK_TWO_ID)),
                                                		hasProperty("title", equalTo(TASK_TWO_TITLE)),
                                                        hasProperty("text", equalTo(TASK_TWO_TEXT)),
                                                        hasProperty("dueTo", equalTo(BaseModel.dateFormat(TASK_TWO_DUE)))
                                                )
                                        ))
                        );
            }
        }
    }
    
    @Nested
    @DisplayName("Render the HTML view that displays the information of the requested task item that is ready to be edited")
    class EditTask {

        private static final long TASK_ID = 99L;

        @Nested
        @DisplayName("When the requested task item isn't found from the database")
        class WhenRequestedTaskItemIsNotFound {

            @BeforeEach
            void serviceThrowsNotFoundException() {
            	when(service.findById(Mockito.anyLong())).thenThrow(new TaskNotFoundException(TASK_ID));
            }

            /**
             * This test ensures that the status code is 404
             */
            @Test
            @DisplayName("Should return the HTTP status code 404")
            void shouldReturnHttpStatusCodeNotFound() throws Exception {
                requestBuilder.editTask(TASK_ID).andExpect(status().isNotFound());
            }

            /**
             * This test ensures that the 404 error view is the view that is rendered
             */
            @Test
            @DisplayName("Should render the 404 view")
            void shouldRender404View() throws Exception {
                requestBuilder.editTask(TASK_ID).andExpect(view().name("error/404"));
            }
        }
        
        @Nested
        @DisplayName("When the requested task item isn't found from the database by mocking empty optional")
        class WhenRequestedTaskItemIsNotFoundByMockingEmptyOptional {
        	
            @BeforeEach
            void serviceReturnEmptyOptional() {
            	when(service.findById(Mockito.anyLong())).thenReturn(Optional.empty());
            }

            /**
             * This test ensures that the status code is 404
             */
            @Test
            @DisplayName("Should return the HTTP status code 404")
            void shouldReturnHttpStatusCodeNotFound() throws Exception {
                requestBuilder.editTask(TASK_ID).andExpect(status().isNotFound());
            }

            /**
             * This test ensures that the 404 error view is the view that is rendered
             */
            @Test
            @DisplayName("Should render the 404 view")
            void shouldRender404View() throws Exception {
                requestBuilder.editTask(TASK_ID).andExpect(view().name("error/404"));
            }
        }

        @Nested
        @DisplayName("When the requested task item is found from the database")
        class WhenRequestedTaskItemIsFound {
        	
        	private static final String TITLE = "Write example project";
            private static final String TEXT = "Use JUnit 5";
            private static final String DUE = "2020-05-01";
                       
            @BeforeEach
            void serviceReturnsTaskItem() {
        		//Optional<TaskDto> taskDto = Optional.empty();
            	TaskDto taskDto = new TaskDto();
        		taskDto.setId(TASK_ID);
        		taskDto.setTitle(TITLE);
        		taskDto.setText(TEXT);
        		taskDto.setDueTo(DUE);
        		when(service.findById(Mockito.anyLong())).thenReturn(Optional.of(taskDto));
            }

            /**
             * This test ensures that the status code is 200
             */
            @Test
            @DisplayName("Should return the HTTP status code 200")
            void shouldReturnHttpStatusCodeOk() throws Exception {
                requestBuilder.editTask(TASK_ID).andExpect(status().isOk());
            }

            /**
             * This test ensures that the task form view is the view that is rendered
             */
            @Test
            @DisplayName("Should render the edit task item form view")
            void shouldRenderEditTaskItemView() throws Exception {
            	requestBuilder.editTask(TASK_ID).andExpect(view().name(TASK_FORM_VIEW_NAME));
            }

            /**
             * This test ensures that the returned task for edit is the correct task item.
             */
            @Test
            @DisplayName("Should display the information of the correct task item")
            void shouldDisplayInformationOfCorrectTaskItem() throws Exception {
                requestBuilder.editTask(TASK_ID)
                        .andExpect(model().attribute(
                                "task",
                                hasProperty("id", equalTo(TASK_ID))
                        ));
            }

            /**
             * This test ensures that the task form displays the information
             * of the selected task item to be edited.
             */
            @Test
            @DisplayName("Should display the correct title, text, and due to")
            void shouldDisplayCorrectData() throws Exception {
                requestBuilder.editTask(TASK_ID)
                        .andExpect(model().attribute(
                                "task",
                                allOf(
                                        hasProperty("title", equalTo(TITLE)),
                                        hasProperty("text", equalTo(TEXT)),
                                        hasProperty("dueTo", equalTo(BaseModel.dateFormat(DUE)))
                                )
                        ));
            }

        }
    }
    
    @Nested
    @DisplayName("Render the HTML view that displays the view for inserting new task")
    class NewTask {
    	
    	/**
         * This test ensures that the status code is 200
         */
    	@Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            requestBuilder.newTask().andExpect(status().isOk());
        }

    	/**
         * This test ensures that the task form view is the view that is rendered
         */
        @Test
        @DisplayName("Should render the new task form view")
        void shouldRenderNewTaskItemView() throws Exception {
        	requestBuilder.newTask().andExpect(view().name(TASK_FORM_VIEW_NAME));
        }
        
        /**
         * This test ensures that the task form view displays an empty task.
         */
        @Test
        @DisplayName("Should render an empty task attribute")
        void shouldRenderEmptyTaskAttribute() throws Exception {
            requestBuilder.newTask()
            	.andDo(print())
            	.andExpect(model().attribute("task", new TaskDto()))
            	;
        }
    }
    
    @Nested
    @DisplayName("Render the HTML view that displays the information of the requested task item")
    class ViewTask {

        private static final long TASK_ID = 99L;

        @Nested
        @DisplayName("When the requested task item isn't found from the database")
        class WhenRequestedTaskItemIsNotFound {

            @BeforeEach
            void serviceThrowsNotFoundException() {
            	when(service.findById(Mockito.anyLong())).thenThrow(new TaskNotFoundException(TASK_ID));
            }

            /**
             * This test ensures that the status code is 400
             */
            @Test
            @DisplayName("Should return the HTTP status code 404")
            void shouldReturnHttpStatusCodeNotFound() throws Exception {
                requestBuilder.viewTask(TASK_ID).andExpect(status().isNotFound());
            }

            /**
             * This test ensures that the 404 error view is the view that is rendered
             */
            @Test
            @DisplayName("Should render the 404 view")
            void shouldRender404View() throws Exception {
                requestBuilder.viewTask(TASK_ID).andExpect(view().name("error/404"));
            }
        }
        
        @Nested
        @DisplayName("When the requested task item isn't found from the database by mocking empty optional")
        class WhenRequestedTaskItemIsNotFoundByMockingEmptyOptional {
            
        	@BeforeEach
            void serviceReturnEmptyOptional() {
            	when(service.findById(Mockito.anyLong())).thenReturn(Optional.empty());
            }

            /**
             * This test ensures that the status code is 400
             */
            @Test
            @DisplayName("Should return the HTTP status code 404")
            void shouldReturnHttpStatusCodeNotFound() throws Exception {
                requestBuilder.viewTask(TASK_ID).andExpect(status().isNotFound());
            }

            /**
             * This test ensures that the 404 error view is the view that is rendered
             */
            @Test
            @DisplayName("Should render the 404 view")
            void shouldRender404View() throws Exception {
                requestBuilder.viewTask(TASK_ID).andExpect(view().name("error/404"));
            }
        }

        @Nested
        @DisplayName("When the requested task item is found from the database")
        class WhenRequestedTaskItemIsFound {
        	
        	private static final String TITLE = "Write example project";
            private static final String TEXT = "Use JUnit 5";
            private static final String DUE = "2020-05-01";
                       
            @BeforeEach
            void serviceReturnsTaskItem() {
        		//Optional<TaskDto> taskDto = Optional.empty();
            	TaskDto taskDto = new TaskDto();
        		taskDto.setId(TASK_ID);
        		taskDto.setTitle(TITLE);
        		taskDto.setText(TEXT);
        		taskDto.setDueTo(DUE);
        		when(service.findById(Mockito.anyLong())).thenReturn(Optional.of(taskDto));
            }

            /**
             * This test ensures that the status code is 200
             */
            @Test
            @DisplayName("Should return the HTTP status code 200")
            void shouldReturnHttpStatusCodeOk() throws Exception {
                requestBuilder.viewTask(TASK_ID).andExpect(status().isOk());
            }

            /**
             * This test ensures that the task view is the view that is rendered
             */
            @Test
            @DisplayName("Should render the view task item view")
            void shouldRenderViewTaskItemView() throws Exception {
            	requestBuilder.viewTask(TASK_ID).andExpect(view().name(TASK_DETAILS_VIEW_NAME));
            }

            /**
             * This test ensures that the returned task is the correct task item.
             */
            @Test
            @DisplayName("Should display the information of the correct task item")
            void shouldDisplayInformationOfCorrectTaskItem() throws Exception {
                requestBuilder.viewTask(TASK_ID)
                        .andExpect(model().attribute(
                                "task",
                                hasProperty("id", equalTo(TASK_ID))
                        ));
            }

            /**
             * This test ensures that the task view displays the information
             * of the selected task item.
             */
            @Test
            @DisplayName("Should display the correct title, text, and due to")
            void shouldDisplayCorrectData() throws Exception {
                requestBuilder.viewTask(TASK_ID)
                        .andExpect(model().attribute(
                                "task",
                                allOf(
                                        hasProperty("title", equalTo(TITLE)),
                                        hasProperty("text", equalTo(TEXT)),
                                        hasProperty("dueTo", equalTo(BaseModel.dateFormat(DUE)))
                                )
                        ));
            }

        }
    }    

    @Nested
    @DisplayName("Render the HTML view that displays the information after saving or updating task")
    class SaveUpdateTask {
    	
    	private static final long TASK_ID = 99L;
    	private static final String TITLE = "Write example project";
    	private static final String TEXT = "Use JUnit 5";
    	private static final String DUE = "2020-05-01";
    	
    	TaskDto taskDto; 
    	
    	@BeforeEach
        void setupSaveTask() {
    		taskDto = new TaskDto();
			taskDto.setTitle(TITLE);
			taskDto.setText(TEXT);
			taskDto.setDueTo(DUE);    			    		
        }
    	
    	@Nested
        @DisplayName("When the inserted task item has binding or validation errors")
        class WhenBindingOrValidationFailed {
    		
    		/**
             * This test ensures that the task form view displays all possible errors for title field
             */
    		@Test
            @DisplayName("Should render the task form view with all title errors")
            void shouldRenderTaskFormViewWithAllTitleErrors() throws Exception {
        		taskDto.setTitle("");
        		
                requestBuilder.saveUpdateTask(taskDto)
                	.andDo(print())
                	.andExpect(status().isOk())
                	.andExpect(view().name(TASK_FORM_VIEW_NAME))
                	.andExpect(model().attribute("task", taskDto))  
                	.andExpect(model().hasErrors())
                	.andExpect(model().attributeErrorCount("task", 2))
                	.andExpect(model().attributeHasFieldErrors("task", "title"))
                	.andExpect(model().attributeHasFieldErrorCode("task", "title", anyOf(equalTo("NotBlank"), equalTo("Size"))))                	
	            	;
            }
    		
    		/**
             * This test ensures that the task form view displays size error for title field
             */
    		@Test
            @DisplayName("Should render the task form view with error Size for title")
            void shouldRenderTaskFormViewWithSizeErrorsForTitle() throws Exception {
        		taskDto.setTitle("A");
        		
                requestBuilder.saveUpdateTask(taskDto)
                	.andDo(print())
                	.andExpect(status().isOk())
                	.andExpect(view().name(TASK_FORM_VIEW_NAME))
                	.andExpect(model().attribute("task", taskDto))  
                	.andExpect(model().hasErrors())
                	.andExpect(model().attributeErrorCount("task", 1))
                	.andExpect(model().attributeHasFieldErrors("task", "title"))
                	.andExpect(model().attributeHasFieldErrorCode("task", "title", "Size"))
	            	;
            }
    		
    		/**
             * This test ensures that the task form view displays all possible errors for text field
             */
    		@Test
    		@DisplayName("Should render the task form view with all text errors")
            void shouldRenderTaskFormViewWithAllTextErrors() throws Exception {
        		taskDto.setText("");
        		
                requestBuilder.saveUpdateTask(taskDto)
                	.andDo(print())
                	.andExpect(status().isOk())
                	.andExpect(view().name(TASK_FORM_VIEW_NAME))
                	.andExpect(model().attribute("task", taskDto))  
                	.andExpect(model().hasErrors())
                	.andExpect(model().attributeErrorCount("task", 2))
                	.andExpect(model().attributeHasFieldErrors("task", "text"))
                	.andExpect(model().attributeHasFieldErrorCode("task", "text", anyOf(equalTo("NotBlank"), equalTo("Size"))))                	
	            	;
            }
    		
    		/**
             * This test ensures that the task form view displays size error for text field
             */
    		@Test
            @DisplayName("Should render the task form view with errors Size for text")
            void shouldRenderTaskFormViewWithSizeErrorsForText() throws Exception {
        		taskDto.setText("A");
        		
                requestBuilder.saveUpdateTask(taskDto)
                	.andDo(print())
                	.andExpect(status().isOk())
                	.andExpect(view().name(TASK_FORM_VIEW_NAME))
                	.andExpect(model().attribute("task", taskDto))  
                	.andExpect(model().hasErrors())
                	.andExpect(model().attributeErrorCount("task", 1))
                	.andExpect(model().attributeHasFieldErrors("task", "text"))
                	.andExpect(model().attributeHasFieldErrorCode("task", "text", "Size"))
	            	;
            }
    		
    		/**
             * This test ensures that the task form view displays all possible errors 
             */
    		@Test
            @DisplayName("Should render the task form view with all title and text errors")
            void shouldRenderTaskFormViewWithSizeErrorsForTitleAndText() throws Exception {
    			taskDto.setTitle("");
    			taskDto.setText("");
        		
                requestBuilder.saveUpdateTask(taskDto)
                	.andDo(print())
                	.andExpect(status().isOk())
                	.andExpect(view().name(TASK_FORM_VIEW_NAME))
                	.andExpect(model().attribute("task", taskDto))  
                	.andExpect(model().hasErrors())
                	.andExpect(model().attributeErrorCount("task", 4))
                	.andExpect(model().attributeHasFieldErrors("task", "title"))
                	.andExpect(model().attributeHasFieldErrors("task", "text"))
                	.andExpect(model().attributeHasFieldErrorCode("task", "title", anyOf(equalTo("NotBlank"), equalTo("Size"))))
                	.andExpect(model().attributeHasFieldErrorCode("task", "text", anyOf(equalTo("NotBlank"), equalTo("Size"))))
	            	;
            }
    		
//            @Test
//            @DisplayName("Should render the task form view")
//            void shouldRenderTaskFormViewWithBindedErrors() throws Exception {
//                requestBuilder.saveTask()
//                	.andDo(print())
//                	.andExpect(model().attributeExists("task"))
//	            	.andExpect(model().attributeHasErrors("task"))
//	            	.andExpect(view().name(TASK_FORM_VIEW_NAME))
//	            	//.andExpect(content().string(
//	                //        allOf(
//	                //                containsString("<title>Task Form</title>"),
//	                //                containsString("alert alert-dismissable")
//	                //        )))
//	            	;
//            }
        }
    	
    	
    	@Nested
        @DisplayName("When the inserted task item has binding or validation failed")
        class WhenSavingTask {
    		
    		@BeforeEach
            void serviceSaveTask() {
    			TaskDto savedTaskDto = new TaskDto(); 
    			savedTaskDto.setId(TASK_ID);
    			savedTaskDto.setTitle(TITLE);
    			savedTaskDto.setText(TEXT);
    			savedTaskDto.setDueTo(DUE);
            	when(service.save(taskDto)).thenReturn(savedTaskDto);
            }
    		
    		/**
             * This test ensures that the status code is 200
             */
            @Test
            @DisplayName("Should return the HTTP status code 200")
            void shouldReturnHttpStatusCodeOk() throws Exception {
                requestBuilder.saveUpdateTask(taskDto)
                	.andExpect(status().isOk());
            }

            /**
             * This test ensures that the task form view is the view that is rendered
             */
            @Test
            @DisplayName("Should render the task form view")
            void shouldRenderTaskFormView() throws Exception {
                requestBuilder.saveUpdateTask(taskDto)
                	.andDo(print())
                	.andExpect(view().name(TASK_FORM_VIEW_NAME))	            	
	            	;
            }
            
            /**
             * This test ensures that the task form view displays an empty task.
             */
            @Test
            @DisplayName("Should render an empty task attribute")
            void shouldRenderEmptyTaskAttribute() throws Exception {
                requestBuilder.saveUpdateTask(taskDto)
                	.andDo(print())
                	.andExpect(model().attribute("task", new TaskDto()))
	            	;
            }
        }
    }
    
    @Nested
    @DisplayName("Render the HTML view that displays the information of all task items after deleting task")
    class DeletTask {
    	
    	private static final long TASK_TO_DELETE_ID = 1L;
    	
    	@Nested
        @DisplayName("When the requested task item for deletion isn't found from the database")
        class WhenRequestedTaskItemForDeletionIsNotFound {

            @BeforeEach
            void serviceThrowsNotFoundException() {
            	when(service.findById(Mockito.anyLong())).thenThrow(new TaskNotFoundException(TASK_TO_DELETE_ID));
            }

            /**
             * This test ensures that the status code is 404
             */
            @Test
            @DisplayName("Should return the HTTP status code 404")
            void shouldReturnHttpStatusCodeNotFound() throws Exception {
                requestBuilder.deleteTask(TASK_TO_DELETE_ID).andExpect(status().isNotFound());
            }

            /**
             * This test ensures that the 404 error view is the view that is rendered
             */
            @Test
            @DisplayName("Should render the 404 view")
            void shouldRender404View() throws Exception {
                requestBuilder.deleteTask(TASK_TO_DELETE_ID).andExpect(view().name("error/404"));
            }
        }
        
        @Nested
        @DisplayName("When the requested task item for deletion isn't found from the database by mocking empty optional")
        class WhenRequestedTaskItemForDeletionIsNotFoundByMockingEmptyOptional {
        	
            @BeforeEach
            void serviceReturnEmptyOptional() {
            	when(service.findById(Mockito.anyLong())).thenReturn(Optional.empty());
            }

            /**
             * This test ensures that the status code is 404
             */
            @Test
            @DisplayName("Should return the HTTP status code 404")
            void shouldReturnHttpStatusCodeNotFound() throws Exception {
                requestBuilder.deleteTask(TASK_TO_DELETE_ID).andExpect(status().isNotFound());
            }

            /**
             * This test ensures that the 404 error view is the view that is rendered
             */
            @Test
            @DisplayName("Should render the 404 view")
            void shouldRender404View() throws Exception {
                requestBuilder.deleteTask(TASK_TO_DELETE_ID).andExpect(view().name("error/404"));
            }
        }

        @Nested
        @DisplayName("When the requested task item is found from the database")
        class WhenRequestedTaskItemForDeletionIsFound {
        	
            private static final String TASK_TO_DELETE_TITLE = "Deleted task item";
            private static final String TASK_TO_DELETE_TEXT = "Text for deleted task";
			private static final String TASK_TO_DELETE_DUE = "2020-05-01";
            
            private static final long TASK_TO_KEEP_ID = 2L;
            private static final String TASK_TO_KEEP_TITLE = "Keeping task item";
            private static final String TASK_TO_KEEP_TEXT = "Text for kept task";
			private static final String TASK_TO_KEEP_DUE = "2021-07-20";
                       
            @BeforeEach
            void serviceReturnsTaskItem() {
        		TaskDto deletedTaskDto = new TaskDto();
            	deletedTaskDto.setId(TASK_TO_DELETE_ID);
            	deletedTaskDto.setTitle(TASK_TO_DELETE_TITLE);
            	deletedTaskDto.setText(TASK_TO_DELETE_TEXT);
            	deletedTaskDto.setDueTo(TASK_TO_DELETE_DUE);
        		when(service.findById(Mockito.anyLong())).thenReturn(Optional.of(deletedTaskDto));
            }
            
            @BeforeEach
            void serviceReturnsTheLeftTask() {
            	TaskDto leftTaskDto = new TaskDto();
            	leftTaskDto.setId(TASK_TO_KEEP_ID);
            	leftTaskDto.setTitle(TASK_TO_KEEP_TITLE);
            	leftTaskDto.setText(TASK_TO_KEEP_TEXT);
            	leftTaskDto.setDueTo(TASK_TO_KEEP_DUE);
                
                when(service.findAll()).thenReturn(Arrays.asList(leftTaskDto));
            }

            /**
             * This test ensures that the status code is 200
             */
            @Test
            @DisplayName("Should return the HTTP status code 200")
            void shouldReturnHttpStatusCodeOk() throws Exception {
                requestBuilder.deleteTask(TASK_TO_DELETE_ID).andExpect(status().isOk());
            }
            
            /**
             * This test ensures that the task list view is the view that is rendered
             */
            @Test
            @DisplayName("Should render the task item list view")
            void shouldRenderTaskItemListView() throws Exception {
                requestBuilder.deleteTask(TASK_TO_DELETE_ID).andExpect(view().name(TASK_LIST_VIEW_NAME));
            }
            
            /**
             * This test ensures that the list view displays the information
             * of the exact 1 item.
             */
            @Test
            @DisplayName("Should display one task item")
            void shouldDisplayTheLeftOneTaskItem() throws Exception {
                requestBuilder.tasks().andExpect(model().attribute("tasks", hasSize(1)));
            }
            
            /**
             * This test ensures that the list view displays the information
             * of the left task.
             */
            @Test
            @DisplayName("Should display the information of the first and second task item in the correct order")
            void shouldDisplayFirstAndSecondTaskItemInCorrectOrder() throws Exception {
                requestBuilder.tasks()
                        .andExpect(
                                model().attribute("tasks",
                                		contains(
                                                allOf(
                                                		hasProperty("id", equalTo(TASK_TO_KEEP_ID)),
                                                		hasProperty("title", equalTo(TASK_TO_KEEP_TITLE)),
                                                        hasProperty("text", equalTo(TASK_TO_KEEP_TEXT)),
                                                        hasProperty("dueTo", equalTo(BaseModel.dateFormat(TASK_TO_KEEP_DUE)))
                                                )
                                        ))
                        );
            }

        }
    }
}
