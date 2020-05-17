package net.hka.examples.thymeleaf.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Creates and sends the HTTP requests which are used
 * to write unit tests for controllers methods which
 * provide CRUD operations for task items.
 */
class SignupRequestBuilder {

    private final MockMvc mockMvc;

    SignupRequestBuilder(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    /**
     * Creates and sends the HTTP requests which gets the
     * HTML document that displays the sign-up form.
     * @return
     * @throws Exception
     */
    ResultActions signup() throws Exception {
        return mockMvc.perform(get("/signup").header("X-Requested-With", ""));
    }
}
