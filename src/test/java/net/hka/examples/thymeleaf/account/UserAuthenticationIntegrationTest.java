package net.hka.examples.thymeleaf.account;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

//import net.hka.examples.thymeleaf.config.WebSecurityConfigurationAware;

//public class UserAuthenticationIntegrationTest extends WebSecurityConfigurationAware {
//
//    private static String SEC_CONTEXT_ATTR = HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
//
//    @Test
//    public void requiresAuthentication() throws Exception {
//        mockMvc.perform(get("/account/current"))
//                .andExpect(redirectedUrl("http://localhost/signin"));
//    }
//
//    @Test
//    public void userAuthenticates() throws Exception {
//        final String username = "user";
//
//        mockMvc.perform(post("/authenticate").param("username", username).param("password", "demo"))
//                .andExpect(redirectedUrl("/"))
//                .andExpect(r -> assertEquals(((SecurityContext) r.getRequest().getSession().getAttribute(SEC_CONTEXT_ATTR)).getAuthentication().getName(), username));
//
//    }
//
//    @Test
//    public void userAuthenticationFails() throws Exception {
//        final String username = "user";
//        mockMvc.perform(post("/authenticate").param("username", username).param("password", "invalid"))
//                .andExpect(redirectedUrl("/signin?error=1"))
//                .andExpect(r -> assertNull(r.getRequest().getSession().getAttribute(SEC_CONTEXT_ATTR)));
//    }
//}
