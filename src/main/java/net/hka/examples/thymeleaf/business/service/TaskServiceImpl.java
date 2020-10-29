package net.hka.examples.thymeleaf.business.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.hka.examples.thymeleaf.business.domain.Task;
import net.hka.examples.thymeleaf.dto.BaseModel;
import net.hka.examples.thymeleaf.dto.TaskDto;
import net.hka.examples.thymeleaf.util.IterableUtil;

@Service("TaskService")
public class TaskServiceImpl implements TaskService {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
    private TaskRepository taskRepository;

    @PostConstruct
    private void initialize() throws ParseException {
    	// Dummy data to initialize database with
        this.initDataTable(new Task("Shopping", "Buy Milk and Butter...", BaseModel.toDate("01/01/2017")));
        this.initDataTable(new Task("Books", "Read 'Lords of The Ring'", BaseModel.toDate("02/01/2017")));
    }
    @Transactional
    private void initDataTable(Task task) {
    	Iterable<Task> tasks = taskRepository.findAll();
    	if(tasks == null || IterableUtil.size(tasks) < 2) taskRepository.save(task);		
	}
    
    
    @Override
    @Transactional
    public TaskDto save(TaskDto taskDto) {
    	if(taskDto == null) throw new IllegalArgumentException("The paremter is null");
    	
    	Date dueDate = new Date();
    	try {
    		dueDate = BaseModel.toDate(taskDto.getDueTo());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Task task = new Task(taskDto.getId(), taskDto.getTitle(), taskDto.getText(), dueDate);
		Task newTask = taskRepository.save(task);
		return modelMapper.map(newTask, TaskDto.class);
    	
    	
	}
    
    
    @Override
    @Transactional
    public void delete(Long id) {
    	if(id == null) throw new IllegalArgumentException();
    	taskRepository.deleteById(id);
	}
    
    @Override
    public List<TaskDto> findAll() {
    	return StreamSupport.stream(taskRepository.findAll().spliterator(), false)
    			.map(task -> modelMapper.map(task, TaskDto.class))
    		    .collect(Collectors.toList());
    	
//    	return (List<TaskDto>) ((Collection) taskRepository.findAll())
//                .stream()
//                .map(task -> modelMapper.map(task, TaskDto.class))
//                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Optional<TaskDto> findById(Long id) {
    	Optional<Task> opTask = taskRepository.findById(id);    	
    	if(opTask.isPresent()) {
    		Task task = opTask.get();
    		
    		// returning mapping object, to be able to use modelMapper.validate()
    		TaskDto taskDto = modelMapper.map(task, TaskDto.class);
        	//modelMapper.validate(); 
    		
    		return Optional.of(taskDto);
    	}
    	
    	return Optional.empty();
    }

}
