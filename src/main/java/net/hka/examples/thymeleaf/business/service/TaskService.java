package net.hka.examples.thymeleaf.business.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import net.hka.examples.thymeleaf.business.dto.TaskDto;

@Service
public interface TaskService {

    TaskDto save(final TaskDto taskDto);
    
    void delete(final Long id);
    
    Iterable<TaskDto> findAll();

    Optional<TaskDto> findById(final Long id);
}
