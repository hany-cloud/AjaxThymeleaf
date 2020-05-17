/*
 * This class working on Junit4 
 * and I already migrated to Junit5 test class located in: 
 * 		net.hka.examples.thymeleaf.business.service.AccountServiceTest
 */

package net.hka.examples.thymeleaf.account;

//import static java.util.function.Predicate.isEqual;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.when;
//
//import java.util.Optional;
//
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import net.hka.examples.thymeleaf.business.service.AccountService;
//import net.hka.examples.thymeleaf.dto.model.AccountDto;

//@SuppressWarnings("deprecation")
//@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

//	@InjectMocks
//	private AccountService accountService; //= new AccountService();
//
////	@Mock
////	private AccountRepository accountRepositoryMock;
//
//	@Mock
//	private PasswordEncoder passwordEncoder;
//	
//	@Rule
//	public ExpectedException thrown = ExpectedException.none();
//
//	
//
//	@Test
//	public void shouldThrowExceptionWhenUserNotFound() {
//		// arrange
//		thrown.expect(UsernameNotFoundException.class);
//		thrown.expectMessage("user not found");
//
//		when(accountService.findOneByEmail("user")).thenReturn(null);
//		// act
//		accountService.loadUserByUsername("user");
//	}
//
//	@Test
//	public void shouldReturnUserDetails() {
//		// arrange
//		AccountDto demoUser = new AccountDto(); //("user", "demo", "ROLE_USER");
//		demoUser.setEmail("user");
//		demoUser.setPassword("demo");
//		
//		when(accountService.findOneByEmail("user")).thenReturn(Optional.of(demoUser));
//
//		// act
//		UserDetails userDetails = accountService.loadUserByUsername("user");
//
//		// assert
//		assertThat(demoUser.getEmail()).isEqualTo(userDetails.getUsername());
//		assertThat(demoUser.getPassword()).isEqualTo(userDetails.getPassword());
//		assertThat(hasAuthority(userDetails, "ROLE_USER")).isTrue();
//	}
//
//	private boolean hasAuthority(UserDetails userDetails, String role) {
//		return userDetails.getAuthorities().stream()
//				.map(GrantedAuthority::getAuthority)
//				.anyMatch(isEqual(role));
//	}
}
