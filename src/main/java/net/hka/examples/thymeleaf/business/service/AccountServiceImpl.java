package net.hka.examples.thymeleaf.business.service;

import java.util.Collections;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.hka.common.util.IterableUtil;
import net.hka.examples.thymeleaf.business.dto.AccountDto;
import net.hka.examples.thymeleaf.domain.Account;
import net.hka.examples.thymeleaf.domain.UserRole;
import net.hka.examples.thymeleaf.domain.repository.AccountRepository;

@Service("AccountService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
class AccountServiceImpl implements AccountService {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostConstruct
	private void initialize() {
		
		// Dummy data to initialize database with
		this.initDataTable(new Account("user", "demo", UserRole.USER.getValue()));
		this.initDataTable(new Account("admin", "admin", UserRole.ADMIN.getValue()));
	}
    @Transactional
    private void initDataTable(Account account) {
    	
    	Iterable<Account> accounts = accountRepository.findAll();
    	if(accounts == null || IterableUtil.size(accounts) < 2) accountRepository.save(account);		
	}
	

	@Override
	@Transactional
	public AccountDto save(final AccountDto accountDto, final String role) {
		
		if(accountDto == null) throw new IllegalArgumentException("The accountDto paremter is null");
		if(role.isEmpty()) throw new IllegalArgumentException("The role paremter is empty");
		
		Account otherAccount = accountRepository.findOneByEmail(accountDto.getEmail());
		if(otherAccount == null) {
			accountDto.setPassword(passwordEncoder.encode(accountDto.getPassword()));			
			Account account = new Account(accountDto.getEmail(), accountDto.getPassword(), role);
			Account savedAccount = accountRepository.save(account);
			return modelMapper.map(savedAccount, AccountDto.class);
		}
		
		return accountDto;
	}

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		
		if(username.isEmpty()) throw new IllegalArgumentException("The paremter is empty");
		
		Account account = accountRepository.findOneByEmail(username);
		if(account == null) {
			throw new UsernameNotFoundException("user not found");
		}
		return this.createUser(account);
	}
	
	
	@Override
	public Account createUserAccount(final AccountDto accountDto) {
		
		if(accountDto == null) throw new IllegalArgumentException("The paremter is null");
		
		return new Account(accountDto.getEmail(), accountDto.getPassword(), UserRole.USER.getValue());
	}
	
	@Override
	public void signin(final AccountDto accountDto) {
		
		if(accountDto == null) throw new IllegalArgumentException("The paremter is null");
		
		Account account = this.createUserAccount(accountDto);
		SecurityContextHolder.getContext().setAuthentication(authenticate(account));
	}
	
	@Override
	public Optional<AccountDto> findOneByEmail(final String email) {
		
		if(email.isEmpty()) throw new IllegalArgumentException("The paremter is empty");
		
		return Optional.of(modelMapper.map(accountRepository.findOneByEmail(email), AccountDto.class));
	}
	
	@Override
	public Optional<AccountDto> findById(final Long id) {
		
		if(id == null) throw new IllegalArgumentException("The paremter is null");
		
		Optional<Account> opAccount = accountRepository.findById(id);  		
		if(opAccount.isPresent()) {
    		Account account = opAccount.get();
    		AccountDto accountDto = modelMapper.map(account, AccountDto.class);
        	//modelMapper.validate(); 
        	return Optional.of(accountDto);
    	}
    	
    	return Optional.empty();
	}
	
	/*
	 * Helper methods
	 */
	private Authentication authenticate(Account account) {
		return new UsernamePasswordAuthenticationToken(createUser(account), null, Collections.singleton(createAuthority(account)));
	}
	
	private User createUser(Account account) {
		return new User(account.getEmail(), account.getPassword(), Collections.singleton(createAuthority(account)));
	}

	private GrantedAuthority createAuthority(Account account) {
		return new SimpleGrantedAuthority(account.getRole());
	}

}
