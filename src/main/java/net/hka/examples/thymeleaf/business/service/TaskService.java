package net.hka.examples.thymeleaf.business.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import net.hka.examples.thymeleaf.business.dto.TaskDto;

@Service
public interface TaskService {

    public TaskDto save(final TaskDto taskDto);
    
    public void delete(final Long id);
    
    public Iterable<TaskDto> findAll();

    public Optional<TaskDto> findById(final Long id);
}
