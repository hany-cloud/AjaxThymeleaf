package net.hka.examples.thymeleaf.business.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import net.hka.examples.thymeleaf.business.model.Account;
import net.hka.examples.thymeleaf.dto.model.AccountDto;

public interface AccountService extends UserDetailsService {
		
	public AccountDto save(AccountDto accountDto, String role);

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
	
	public void signin(AccountDto accountDto);
	
	public Optional<AccountDto> findOneByEmail(String email);
	
	public Optional<AccountDto> findById(Long id);
	
	public Account createUserAccount(AccountDto accountDto);
		
}
