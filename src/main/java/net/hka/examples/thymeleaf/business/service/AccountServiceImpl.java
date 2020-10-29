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

import net.hka.examples.thymeleaf.business.domain.Account;
import net.hka.examples.thymeleaf.business.domain.UserRole;
import net.hka.examples.thymeleaf.dto.AccountDto;
import net.hka.examples.thymeleaf.util.IterableUtil;

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
	public AccountDto save(AccountDto accountDto, String role) {
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
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepository.findOneByEmail(username);
		if(account == null) {
			throw new UsernameNotFoundException("user not found");
		}
		return this.createUser(account);
	}
	
	
	@Override
	public Account createUserAccount(AccountDto accountDto) {
		return new Account(accountDto.getEmail(), accountDto.getPassword(), UserRole.USER.getValue());
	}
	
	@Override
	public void signin(AccountDto accountDto) {
		Account account = this.createUserAccount(accountDto);
		SecurityContextHolder.getContext().setAuthentication(authenticate(account));
	}
	
	@Override
	public Optional<AccountDto> findOneByEmail(String email) {
		return Optional.of(modelMapper.map(accountRepository.findOneByEmail(email), AccountDto.class));
	}
	
	@Override
	public Optional<AccountDto> findById(Long id) {
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
