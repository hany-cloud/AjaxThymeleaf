package net.hka.examples.thymeleaf.business.service;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import net.hka.examples.thymeleaf.business.model.Task;

@Repository
interface TaskRepository extends PagingAndSortingRepository<Task, Long> {
}
