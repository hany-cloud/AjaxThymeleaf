package net.hka.examples.thymeleaf.business.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import net.hka.examples.thymeleaf.business.domain.Account;
import net.hka.examples.thymeleaf.web.dto.AccountDto;

public interface AccountService extends UserDetailsService {
		
	public AccountDto save(AccountDto accountDto, String role);

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException;
	
	public void signin(final AccountDto accountDto);
	
	public Optional<AccountDto> findOneByEmail(final String email);
	
	public Optional<AccountDto> findById(final Long id);
	
	public Account createUserAccount(final AccountDto accountDto);
		
}
