package net.hka.examples.thymeleaf.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

//import javax.inject.Inject;

////@RunWith(SpringJUnit4ClassRunner.class)
//@ActiveProfiles("test")
//@WebAppConfiguration
////@ContextConfiguration(classes = {ApplicationConfig.class})
//@ExtendWith(MockitoExtension.class)
//public abstract class WebAppConfigurationAware {
//
//    @InjectMocks
//    protected WebApplicationContext wac;
//    
//    protected MockMvc mockMvc;
//
//    @BeforeEach
//    public void setupMockMvc() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
//    }
//
//}
