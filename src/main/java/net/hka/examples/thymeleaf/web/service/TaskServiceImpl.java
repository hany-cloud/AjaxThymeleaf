package net.hka.examples.thymeleaf.web.service;

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

import net.hka.common.ui.conversion.DateFormatter;
import net.hka.common.util.IterableUtil;
import net.hka.examples.thymeleaf.business.domain.Task;
import net.hka.examples.thymeleaf.business.repository.TaskRepository;
import net.hka.examples.thymeleaf.web.dto.BaseDto;
import net.hka.examples.thymeleaf.web.dto.TaskDto;

@Service("TaskService")
public class TaskServiceImpl implements TaskService {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
    private TaskRepository taskRepository;
	
	@Autowired
    private DateFormatter dateFormatter;

    @PostConstruct
    private void initialize() throws ParseException {
    	
    	// Dummy data to initialize database with
        this.initDataTable(new Task("Shopping", "Buy Milk and Butter...", dateFormatter.parse("2017-01-01", BaseDto.PARSED_DATE_FORMAT)));
        this.initDataTable(new Task("Books", "Read 'Lords of The Ring'", dateFormatter.parse("2017-01-01", BaseDto.PARSED_DATE_FORMAT)));
    }
    @Transactional
    private void initDataTable(Task task) {
    	
    	Iterable<Task> tasks = taskRepository.findAll();
    	if(tasks == null || IterableUtil.size(tasks) < 2) taskRepository.save(task);		
	}
    
    
    @Override
    @Transactional
    public TaskDto save(final TaskDto taskDto) {
    	
    	if(taskDto == null) throw new IllegalArgumentException("The paremter is null");
    	
    	Date dueDate = new Date();
    	try {
    		if(!taskDto.getDueTo().isEmpty()) dueDate = dateFormatter.parse(taskDto.getDueTo(), BaseDto.PARSED_DATE_FORMAT); 
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Task task = new Task(taskDto.getId(), taskDto.getTitle(), taskDto.getText(), dueDate);
		Task newTask = taskRepository.save(task);
		return modelMapper.map(newTask, TaskDto.class);
    	
    	
	}
    
    
    @Override
    @Transactional
    public void delete(final Long id) {
    	
    	if(id == null) throw new IllegalArgumentException("The paremter is null");
    	
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
    public Optional<TaskDto> findById(final Long id) {
    	
    	if(id == null) throw new IllegalArgumentException("The paremter is null");
    	
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
