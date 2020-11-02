package net.hka.examples.thymeleaf.business.service;

import static java.util.function.Predicate.isEqual;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import net.hka.examples.thymeleaf.business.domain.Account;
import net.hka.examples.thymeleaf.business.domain.UserRole;
import net.hka.examples.thymeleaf.business.dto.AccountDto;
import net.hka.examples.thymeleaf.business.repository.AccountRepository;

@DisplayName("When running The Test for AccountService Class")
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
	@Mock
	private AccountRepository accountRepository;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private ModelMapper modelMapper;
	
	
	@InjectMocks
	private AccountServiceImpl accountService;
	
		
	private AccountDto demoUser;
	private static final long DUMMY_USER_ID = 100;
	
	@BeforeEach
	void init() {
		demoUser = new AccountDto(); //("user", "demo", "ROLE_USER");
		demoUser.setEmail("user");
		demoUser.setPassword("demo");
	}
	
	@Test
	void testSave() {
		// arrange
		when(accountRepository.findOneByEmail("user")).thenReturn(null);
		demoUser.setId(DUMMY_USER_ID);
		when(accountService.save(demoUser, UserRole.USER.getValue())).thenReturn(demoUser);
		
		
		// act
		AccountDto newAccountDto = accountService.save(demoUser, UserRole.USER.getValue());
		
		// assert
		assertThat(demoUser.getEmail()).isEqualTo(newAccountDto.getEmail());
		assertThat(demoUser.getPassword()).isEqualTo(newAccountDto.getPassword());
		assertThat(newAccountDto.getId()).isNotNull();	
		assertThat(newAccountDto.getId()).isEqualTo(DUMMY_USER_ID);	
	}
	
	
	@Test
	public void shouldThrowExceptionWhenUserNotFound() {
		// arrange
		when(accountRepository.findOneByEmail("user")).thenReturn(null);
		
		
		// act & assert
		Exception exception = assertThrows(UsernameNotFoundException.class, () -> accountService.loadUserByUsername("user"), () -> "Should throw an exception when trying to fetch a non existing user");
		assertEquals("user not found", exception.getMessage());
	}
	
	@Test
	public void shouldReturnUserDetails() {
		// arrange
		Account account = new Account(demoUser.getEmail(), demoUser.getPassword(), UserRole.USER.getValue());
		when(accountRepository.findOneByEmail("user")).thenReturn(account);

		// act
		UserDetails userDetails = accountService.loadUserByUsername("user");

		// assert
		assertThat(account.getEmail()).isEqualTo(userDetails.getUsername());
		assertThat(account.getPassword()).isEqualTo(userDetails.getPassword());
		assertThat(hasAuthority(userDetails, UserRole.USER.getValue())).isTrue();
	}
	
	private boolean hasAuthority(UserDetails userDetails, String role) {
		return userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(isEqual(role));
	}

}
