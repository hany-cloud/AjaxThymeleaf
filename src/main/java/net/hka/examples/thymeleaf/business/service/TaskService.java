package net.hka.examples.thymeleaf.business.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import net.hka.examples.thymeleaf.dto.TaskDto;

@Service
public interface TaskService {

    public TaskDto save(TaskDto taskDto);
    
    public void delete(Long id);
    
    public Iterable<TaskDto> findAll();

    public Optional<TaskDto> findById(Long id);
}
