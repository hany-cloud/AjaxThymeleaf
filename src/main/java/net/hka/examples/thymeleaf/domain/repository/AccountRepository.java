package net.hka.examples.thymeleaf.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.hka.examples.thymeleaf.domain.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	
	Account findOneByEmail(final String email);
}