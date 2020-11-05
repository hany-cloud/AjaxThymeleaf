package net.hka.examples.thymeleaf.business.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import net.hka.examples.thymeleaf.business.dto.AccountDto;
import net.hka.examples.thymeleaf.domain.Account;

public interface AccountService extends UserDetailsService {
		
	AccountDto save(AccountDto accountDto, String role);

	@Override
	UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException;
	
	void signin(final AccountDto accountDto);
	
	Optional<AccountDto> findOneByEmail(final String email);
	
	Optional<AccountDto> findById(final Long id);
	
	Account createUserAccount(final AccountDto accountDto);
}
