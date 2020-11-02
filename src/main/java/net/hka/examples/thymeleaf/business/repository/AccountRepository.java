package net.hka.examples.thymeleaf.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.hka.examples.thymeleaf.business.domain.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	
	Account findOneByEmail(final String email);
}