package net.hka.examples.thymeleaf.web.controller;


import static net.hka.examples.thymeleaf.config.WebTestConfig.exceptionResolver;
import static net.hka.examples.thymeleaf.config.WebTestConfig.fixedLocaleResolver;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import net.hka.examples.thymeleaf.business.dto.AccountDto;
import net.hka.examples.thymeleaf.business.service.AccountService;


public class SignupControllerTest {
	private static final String SIGNUP_VIEW_NAME = "signup/signup";
	
	private SignupRequestBuilder requestBuilder;
    private AccountService service;
    
    @BeforeEach
    void configureSystemUnderTest() {
        service = mock(AccountService.class);
        
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new SignupController(service))
                .setHandlerExceptionResolvers(exceptionResolver())
                .setLocaleResolver(fixedLocaleResolver())
                //.setViewResolvers(jspViewResolver())
                .build();
        requestBuilder = new SignupRequestBuilder(mockMvc);
    }
	
//    @Test
//    void displaysSignupForm() throws Exception {
//        mockMvc.perform(get("/signup")
//        		.header("X-Requested-With", ""))
//                .andExpect(model().attributeExists("accountDto"))
//                .andExpect(view().name(SIGNUP_VIEW_NAME))
//                .andExpect(content().string(
//                        allOf(
//                                containsString("<title>Signup</title>"),
//                                containsString("<legend>Please Sign Up</legend>")
//                        ))
//                );
//    }
    @Nested
    @DisplayName("Render the HTML view that displays the view for signup")
    class Signup {

    	/**
         * This test ensures that the status code is 200
         */
    	@Test
        @DisplayName("Should return the HTTP status code 200")
        void shouldReturnHttpStatusCodeOk() throws Exception {
            requestBuilder.signup().andExpect(status().isOk());
        }

    	/**
         * This test ensures that the sign-up form view is the view that is rendered
         */
        @Test
        @DisplayName("Should render the new task form view")
        void shouldRenderNewTaskItemView() throws Exception {
        	requestBuilder.signup().andExpect(view().name(SIGNUP_VIEW_NAME));
        }
        
        /**
         * This test ensures that the sign-up form view displays an empty account.
         */
        @Test
        @DisplayName("Should render an empty task attribute")
        void shouldRenderEmptyTaskAttribute() throws Exception {
            requestBuilder.signup()
            	.andDo(print())
            	.andExpect(model().attribute("accountDto", new AccountDto()))
            	;
        }
    
    }
    
}