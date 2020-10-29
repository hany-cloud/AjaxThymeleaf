package net.hka.examples.thymeleaf.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import net.hka.examples.thymeleaf.business.domain.Task;
import net.hka.examples.thymeleaf.dto.BaseModel;
import net.hka.examples.thymeleaf.dto.TaskDto;
import net.hka.examples.thymeleaf.error.exception.TaskNotFoundException;

@DisplayName("When running the test for TaskService Class")
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

	@Mock
	private TaskRepository taskRepository;
		
	@Mock
	private ModelMapper modelMapper;
	
	
	@InjectMocks
	private TaskServiceImpl taskService;
	
	
	@BeforeEach
	void configureSystemUnderTest() {
		// you can use this method to initialize all the objects that you need through the whole test cases
		// such as taskRepository, modelMapper, and taskService 
		// instead of using annotations @Mock, and @InjectMocks 
	}
	
	@Nested
    @DisplayName("Save task")
    class Save {
		
		@Nested
		@DisplayName("When saving null task item")
        class WhenSaveFailedByThrowIllegalArgumentException {
			
			/**
			 * This test ensures that the task service will throw IllegalArgumentException 
			 * if the task parameter is null
			 */
			@Test
			@DisplayName("Should throw IllegalArgumentException")
            void serviceThrowsIllegalArgumentException() {
                assertThrows(IllegalArgumentException.class, () -> taskService.save(null));
            }
			
		}
		
		@Nested
        @DisplayName("When save task operation is succeed")
        class WhenSaveSucceed {
			
			private static final long TASK_ID = 100L;
			private static final String TITLE = "Write example project";
            private static final String TEXT = "Use JUnit 5";
            private static final String DUE = "2020-05-01";
			
            private TaskDto taskDto;
            
			@BeforeEach
            void repositorySaveTask() throws ParseException {
				Task task = new Task(TITLE, TEXT, BaseModel.toDate(DUE));
				Task savedTask = new Task(TITLE, TEXT, BaseModel.toDate(DUE));
				savedTask.setId(TASK_ID);
				//when(taskRepository.save((Task) any(Task.class))).thenReturn(savedTask);
				when(taskRepository.save(task)).thenReturn(savedTask);

				taskDto = map(task);
		        taskDto.setDueTo(DUE);
				
				TaskDto mappedTaskDto = map(savedTask);
				mappedTaskDto.setDueTo(DUE);
				//when(modelMapper.map(any(Task.class), TaskDto.class)).thenReturn(mappedTaskDto);
		        when(modelMapper.map(savedTask, TaskDto.class)).thenReturn(mappedTaskDto);
            }
			
			/**
			 * This test ensures that the task service will save the task 
			 * if it is correct and complete
			 */
			@Test
            @DisplayName("Should save task")
            void shouldSaveTask() throws Exception {
				// act
				TaskDto newTaskDto = taskService.save(taskDto);
				
				// assert
				assertThat(newTaskDto.getTitle()).isEqualTo(TITLE);
				assertThat(newTaskDto.getText()).isEqualTo(TEXT);
				assertThat(newTaskDto.getId()).isNotNull();	
				assertThat(newTaskDto.getId()).isEqualTo(TASK_ID);	
            }
		}
	}
	
	@Nested
    @DisplayName("Delete task")
    class Delete {
		
		@Nested
		@DisplayName("When deleting null task item")
        class WhenDeleteFailedByThrowIllegalArgumentException {
			
			/**
			 * This test ensures that the task service will throw IllegalArgumentException 
			 * if the task parameter is null
			 */
			@Test
			@DisplayName("Should throw IllegalArgumentException")
            void serviceThrowsIllegalArgumentException() {
                assertThrows(IllegalArgumentException.class, () -> taskService.delete(null));
            }
			
		}
		
		@Nested
        @DisplayName("When delete task operation is succeed")
        class WhenDeleteSucceed {
			
			private static final long TASK_ID = 101L;
			
			@BeforeEach
            void repositoryDeleteTask() {
				doNothing().when(taskRepository).deleteById(Mockito.anyLong());
            }
			
			/**
			 * This test ensures that the task service will delete the task with 
			 * existing task id
			 */
			@Test
            @DisplayName("Should delete task")
            void shouldSaveTask() throws Exception {
				// act
				taskService.delete(TASK_ID);
			  
				// verify void method
				verify(taskRepository).deleteById(TASK_ID);
			    verify(taskRepository, times(1)).deleteById(TASK_ID);	
            }
		}
	}
	
	@Nested
    @DisplayName("Return the information of all task items")
    class FindAll {
		
		@Nested
        @DisplayName("When no task items is found from the database")
        class WhenNoTasksIsFoundFromDatabase {
			
			@BeforeEach
            void repositoryReturnsEmptyList() {
                when(taskRepository.findAll()).thenReturn(new ArrayList<>());
            }
			
			/**
			 * This test ensures that the task service will display zero task 
			 * if there is no tasks in database
			 */
			@Test
            @DisplayName("Should display zero task items")
            void shouldDisplayZeroTasks() throws Exception {
				// act
				List<TaskDto> taskDtos = taskService.findAll();
				
				// assert
				assertThat(taskDtos, hasSize(0));
            }
		}
		
		@Nested
        @DisplayName("When two task items are found from the database")
        class WhenTwoTasksAreFoundFromDatabase {
			
			private static final long TASK_ONE_ID = 90L;
			private static final String TASK_ONE_TITLE = "First Task";
			private static final String TASK_ONE_TEXT = "Text for first task";
			private static final String TASK_ONE_DUE = "2020-05-01";
			
			private static final long TASK_TWO_ID = 95L;
			private static final String TASK_TWO_TITLE = "First Task";
			private static final String TASK_TWO_TEXT = "Text for second task";
			private static final String TASK_TWO_DUE = "2021-07-20";
			
			@BeforeEach
            void repositoryReturnsTwoTasks() throws ParseException {
				TaskDto firstDto = new TaskDto(); 
				firstDto.setTitle(TASK_ONE_TITLE);
				firstDto.setText(TASK_ONE_TEXT);
				firstDto.setDueTo(TASK_ONE_DUE);
				Task first = new Task(firstDto.getTitle(), firstDto.getText(), BaseModel.toDate(TASK_ONE_DUE));
				first.setId(TASK_ONE_ID);
				
				TaskDto secondDto = new TaskDto();
				secondDto.setTitle(TASK_TWO_TITLE);
				secondDto.setText(TASK_TWO_TEXT);
				secondDto.setDueTo(TASK_TWO_DUE);
                Task second = new Task(secondDto.getTitle(), secondDto.getText(), BaseModel.toDate(TASK_TWO_DUE));
                second.setId(TASK_TWO_ID);
                
                when(taskRepository.findAll()).thenReturn(Arrays.asList(first, second));
                
                
        		TaskDto mappedTaskDtoFirst = map(first);
        		mappedTaskDtoFirst.setDueTo(TASK_ONE_DUE);
        		when(modelMapper.map(first, TaskDto.class)).thenReturn(mappedTaskDtoFirst);
        		
        		TaskDto mappedTaskDtoSecond = map(second);
        		mappedTaskDtoSecond.setDueTo(TASK_TWO_DUE);
        		when(modelMapper.map(second, TaskDto.class)).thenReturn(mappedTaskDtoSecond);
            }
			
			/**
			 * This test ensures that the task service will display two tasks 
			 * that are exist
			 */
			@Test
            @DisplayName("Should display two task items")
            void shouldDisplayTwoTaskItems() throws Exception {
				// act
				List<TaskDto> taskDtos = taskService.findAll();
				
				// assert
				assertThat(taskDtos, hasSize(2));
            }
			
			/**
             * These two tests ensure that the task service displays the information
             * of the found task items but they don't guarantee that tasks
             * are displayed in the correct order
             */
            @Test
            @DisplayName("Should display the information of the first task item")
            void shouldDisplayInformationOfFirstTaskItem() throws Exception {
            	// act
				List<TaskDto> taskDtos = taskService.findAll();
				//System.err.println(taskDtos);
				
				// assert
				assertThat(taskDtos, hasItem(allOf(
                        hasProperty("title", equalTo(TASK_ONE_TITLE)),
                        hasProperty("text", equalTo(TASK_ONE_TEXT)),
                        hasProperty("dueTo", equalTo(BaseModel.dateFormat(TASK_ONE_DUE)))
                )));
            }

            @Test
            @DisplayName("Should display the information of the second task item")
            void shouldDisplayInformationOfSecondTaskItem() throws Exception {
            	// act
				List<TaskDto> taskDtos = taskService.findAll();
				//System.err.println(taskDtos);
				
				// assert
				assertThat(taskDtos, hasItem(allOf(
                        hasProperty("title", equalTo(TASK_TWO_TITLE)),
                        hasProperty("text", equalTo(TASK_TWO_TEXT)),
                        hasProperty("dueTo", equalTo(BaseModel.dateFormat(TASK_TWO_DUE)))
                )));
            }
            
            /**
             * This test ensures that the task service displays the information
             * of the found task items in the correct order.
             */
            @Test
            @DisplayName("Should display the information of the first and second task items in the correct order")
            void shouldDisplayFirstAndSecondTaskItemInCorrectOrder() throws Exception {
            	// act
				List<TaskDto> taskDtos = taskService.findAll();
				
				// assert
				assertThat(taskDtos, 
                                        contains(
                                                allOf(
                                                		hasProperty("title", equalTo(TASK_ONE_TITLE)),
                                                        hasProperty("text", equalTo(TASK_ONE_TEXT)),
                                                        hasProperty("dueTo", equalTo(BaseModel.dateFormat(TASK_ONE_DUE)))
                                                ),
                                                allOf(
                                                		hasProperty("title", equalTo(TASK_TWO_TITLE)),
                                                        hasProperty("text", equalTo(TASK_TWO_TEXT)),
                                                        hasProperty("dueTo", equalTo(BaseModel.dateFormat(TASK_TWO_DUE)))
                                                )
                                        )
               );
            }
			
		}
	}
	
	@Nested
	@DisplayName("Return the information of the requested task item")
	class FindById {
		
		private static final long TASK_ID = 99L;
		
		@Nested
        @DisplayName("When the requested task item isn't found from the database")
        class WhenRequestedTaskItemIsNotFound {
			
			/**
             * This test ensures that the task service will throw TaskNotFoundException 
             * if the provided task id is not exist.
             */
			@Test
			@DisplayName("Should throw TaskNotFoundException")
            void serviceThrowsNotFoundException() {
                when(taskRepository.findById(TASK_ID)).thenThrow(new TaskNotFoundException(TASK_ID));
                
                assertThrows(TaskNotFoundException.class, () -> taskService.findById(TASK_ID));
            }
		}
		
		@Nested
        @DisplayName("When the requested task item is found from the database")
        class WhenRequestedTaskItemIsFound {
			
			private static final String TITLE = "Write example project";
            private static final String TEXT = "Use JUnit 5";
            private static final String DUE = "2020-05-01";
            
            @BeforeEach
            void repositoryReturnsTaskItem() throws ParseException {
        		Optional<Task> task = Optional.of(new Task(TITLE, TEXT, BaseModel.toDate(DUE)));
        		task.get().setId(TASK_ID);
        		when(taskRepository.findById(Mockito.anyLong())).thenReturn(task);
        		
        		TaskDto mappedTaskDto = map(task.get());
        		mappedTaskDto.setDueTo(DUE);
        		when(modelMapper.map(task.get(), TaskDto.class)).thenReturn(mappedTaskDto);
            }
            
            /**
             * This test ensures that the task service will fetch the exact task  
             * with the same requested task id
             */
            @Test
            @DisplayName("Should display the information of the correct task item")
            void shouldDisplayInformationOfCorrectTaskItem() throws Exception {
            	// act
            	Optional<TaskDto> fetchedTask = taskService.findById(TASK_ID);
				
				// assert
        		assertTrue(fetchedTask.isPresent());
        		assertThat(fetchedTask.get().getId()).isEqualTo(TASK_ID);
            	
            }

            /**
             * This test ensures that the task service will fetch the exact task  
             * with the same attributes.
             */
            @Test
            @DisplayName("Should display the correct title and text")
            void shouldDisplayCorrectTitleAndText() throws Exception {
            	// act
            	Optional<TaskDto> fetchedTask = taskService.findById(TASK_ID);
				
				// assert
        		assertTrue(fetchedTask.isPresent());
        		assertThat(fetchedTask.get().getTitle()).isEqualTo(TITLE);
        		assertThat(fetchedTask.get().getText()).isEqualTo(TEXT);
            }
		}
	}

	private TaskDto map(Task taskToBeMapped) {
		return new ModelMapper().map(taskToBeMapped, TaskDto.class);
	}
}
