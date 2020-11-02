package net.hka.examples.thymeleaf.business.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import net.hka.examples.thymeleaf.business.domain.Task;

@Repository
public interface TaskRepository extends PagingAndSortingRepository<Task, Long> {
}
