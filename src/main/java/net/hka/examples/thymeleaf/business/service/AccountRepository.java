package net.hka.examples.thymeleaf.business.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.hka.examples.thymeleaf.business.model.Account;

@Repository
interface AccountRepository extends JpaRepository<Account, Long> {
	Account findOneByEmail(String email);
}