package net.hka.examples.thymeleaf.config;

import java.util.Collections;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.webflow.config.FlowBuilderServicesBuilder;
import org.springframework.webflow.config.FlowDefinitionRegistryBuilder;
import org.springframework.webflow.config.FlowExecutorBuilder;
import org.springframework.webflow.definition.registry.FlowDefinitionLocator;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.ViewFactoryCreator;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;
import org.springframework.webflow.executor.FlowExecutor;
import org.springframework.webflow.mvc.builder.MvcViewFactoryCreator;
import org.springframework.webflow.mvc.servlet.FlowHandlerAdapter;
import org.springframework.webflow.mvc.servlet.FlowHandlerMapping;
import org.springframework.webflow.security.SecurityFlowExecutionListener;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.webflow.view.AjaxThymeleafViewResolver;
import org.thymeleaf.spring5.webflow.view.FlowAjaxThymeleafView;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.UrlTemplateResolver;

import net.hka.common.ui.conversion.DateFormatter;
import nz.net.ultraq.thymeleaf.LayoutDialect;

@Configuration
@ComponentScan
class MvcConfig  extends WebMvcConfigurationSupport {
	    	
	private static final String VIEWS = "classpath:/templates/";

    private static final String RESOURCES_LOCATION = "classpath:/static/";
    private static final String RESOURCES_HANDLER = "/resources/" + "**";
    
    private static final String MESSAGE_RESOURCES_LOCATION = "classpath:messages";
    
    private static final String WEBFLOW_RESOURCES_LOCATION = "classpath:flows";
    private static final String WEBFLOW_LOCATION_PATTERN = "/**/*-flow.xml";

    //-----------------------------------------------------------------------------------------------------------------------------
    // Spring Webflow and Spring MCV configuration and integration with Spring security Section
    //-----------------------------------------------------------------------------------------------------------------------------
    @Bean	
	public FlowHandlerMapping flowHandlerMapping() {
    	
		FlowHandlerMapping handlerMapping = new FlowHandlerMapping();
		handlerMapping.setOrder(-1);
		handlerMapping.setFlowRegistry(this.flowRegistry());
		return handlerMapping;
	}

	@Bean
	public FlowHandlerAdapter flowHandlerAdapter() {
		
		FlowHandlerAdapter handlerAdapter = new FlowHandlerAdapter();
		handlerAdapter.setFlowExecutor(this.flowExecutor());
		handlerAdapter.setSaveOutputToFlashScopeOnRedirect(true);
		return handlerAdapter;
	}
    
	@Bean
	public FlowExecutor flowExecutor() {
		
		return getFlowExecutorBuilder(this.flowRegistry()) // to set the web flow resource and pattern locations
				.addFlowExecutionListener(new SecurityFlowExecutionListener()) // adding spring security web flow listener for flow executor  
				.build();
	}
	
	
	@Bean
	public FlowDefinitionRegistry flowRegistry() {
		
		return getFlowDefinitionRegistryBuilder() 
				.setBasePath(WEBFLOW_RESOURCES_LOCATION) 
				.addFlowLocationPattern(WEBFLOW_LOCATION_PATTERN) 
				.setFlowBuilderServices(this.flowBuilderServices()) 
				.build();
	}

    @Bean
	public FlowBuilderServices flowBuilderServices() {
    	
		return getFlowBuilderServicesBuilder() //
				.setViewFactoryCreator(this.mvcViewFactoryCreator()) // Important!
				.setValidator(this.getValidator())
				.build();
	}

	@Bean
	public ViewFactoryCreator mvcViewFactoryCreator() {
		
		MvcViewFactoryCreator factoryCreator = new MvcViewFactoryCreator();
		factoryCreator.setViewResolvers(Collections.singletonList(this.viewResolver()));
		factoryCreator.setUseSpringBeanBinding(true);
		return factoryCreator;
	}
    
	/**
	 * Return a builder for creating a {@link FlowExecutor} instance.
	 * @param flowRegistry the {@link FlowDefinitionRegistry} to configure on the flow executor
	 * @return the created builder
	 */
	protected FlowExecutorBuilder getFlowExecutorBuilder(FlowDefinitionLocator flowRegistry) {
		return new FlowExecutorBuilder(flowRegistry);
	}

	/**
	 * Return a builder for creating a {@link FlowDefinitionRegistry} instance.
	 * @return the created builder
	 */
	protected FlowDefinitionRegistryBuilder getFlowDefinitionRegistryBuilder() {
		return new FlowDefinitionRegistryBuilder(this.getApplicationContext());
	}

	/**
	 * Return a builder for creating a {@link FlowDefinitionRegistry} instance.
	 * @param flowBuilderServices the {@link FlowBuilderServices} to configure on the flow registry with
	 * @return the created builder
	 */
	protected FlowDefinitionRegistryBuilder getFlowDefinitionRegistryBuilder(FlowBuilderServices flowBuilderServices) {
		return new FlowDefinitionRegistryBuilder(this.getApplicationContext(), flowBuilderServices);
	}

	/**
	 * Return a builder for creating a {@link FlowBuilderServices} instance.
	 * @return the created builder
	 */
	protected FlowBuilderServicesBuilder getFlowBuilderServicesBuilder() {
		return new FlowBuilderServicesBuilder();
	}
	// ----------------------------------------------------------
	
	//-----------------------------------------------------------------------------------------------------------------------------
    // Thymeleaf Configuration Section with Spring and AJAX integration
    //-----------------------------------------------------------------------------------------------------------------------------
	
	@Bean
	@Description("Thymeleaf AJAX view resolver for Spring WebFlow")
	public AjaxThymeleafViewResolver viewResolver() {
		
		AjaxThymeleafViewResolver viewResolver = new AjaxThymeleafViewResolver();
		viewResolver.setViewClass(FlowAjaxThymeleafView.class);
		viewResolver.setTemplateEngine(this.templateEngine());
		viewResolver.setCharacterEncoding("UTF-8");
		return viewResolver;
	}
	
	@Bean
    @Description("Thymeleaf template engine with Spring integration")
    public SpringTemplateEngine templateEngine() {
		
    	SpringTemplateEngine templateEngine = new SpringTemplateEngine(); // establishes by default an instance of {@link SpringStandardDialect}
        																  // It also configures a {@link SpringMessageResolver} for MessageSource
        
        templateEngine.addTemplateResolver(new UrlTemplateResolver()); // creates {@link UrlTemplateResource} instances for template resources.
        
        templateEngine.addTemplateResolver(this.templateResolver()); // set template defaults, makes the html is the first template in the chain, 
        																 // you can add more template resolver in chain.
        
        templateEngine.addDialect(new SpringSecurityDialect()); // Integrating Thymeleaf with spring security
         
        templateEngine.addDialect(new LayoutDialect()); // A dialect for Thymeleaf that lets you build layouts and reusable templates in
        												// order to improve code reuse
        
        templateEngine.addDialect(new Java8TimeDialect()); // Thymeleaf Dialect to format and create Java 8 Time objects.
        
        
        return templateEngine;
    }
    
    @Bean    
    public ITemplateResolver templateResolver() {
    	
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();    
        resolver.setPrefix(VIEWS);
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML); 
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);
        return resolver;
    }
    // ----------------------------------------------------------
	
    
    //-----------------------------------------------------------------------------------------------------------------------------
    // Message resource settings and general beans configuration section
    //-----------------------------------------------------------------------------------------------------------------------------
    
	/*
	 * To apply javax.validation (JSR-303) setup in a Spring application context: 
	 * It bootstraps a javax.validation.ValidationFactory and exposes it through the Spring
	 */
    @Bean(name="validator")
    public LocalValidatorFactoryBean getValidator() {
    	
    	LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
    	validator.setValidationMessageSource(this.messageSource());
        return validator;
    }
    
	
	/*
	 * Setting the static resources, since we did not using the auto configuration annotation @EnableWebMvc
	 */
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(RESOURCES_HANDLER).addResourceLocations(RESOURCES_LOCATION);        
    }
	
	/*
     *  Message externalization/internationalization
     */
    @Bean
    @ConditionalOnBean(name="servletContext")
    public MessageSource messageSource() {
    	
    	ReloadableResourceBundleMessageSource messageSource
          = new ReloadableResourceBundleMessageSource();
         
        messageSource.setBasename(MESSAGE_RESOURCES_LOCATION);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
    
    
    /*
	 * Add formatter for class {@link java.util.Date}
	 * in addition to the one registered by default
	 */
	@Override
	public void addFormatters(final FormatterRegistry registry) {
		registry.addFormatter(dateFormatter());
	}
	
	@Bean
	public DateFormatter dateFormatter() {
		return new DateFormatter();
	}
	
	
	/*
	 * To map between different model structure 
	 */
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	
	
	/*
	 * Adding Locale feature in the application
	 */
	// In order for our application to be able to determine which locale is currently being used
	/*@Bean
	public LocaleResolver localeResolver() {
	    SessionLocaleResolver slr = new SessionLocaleResolver();
	    slr.setDefaultLocale(Locale.US);
	    return slr;
	}
	// An interceptor bean that will switch to a new locale based on the value of the "lang" parameter appended to a request
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
	    LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
	    lci.setParamName("lang");
	    return lci;
	}*/
	/*
	 * Add interceptor in addition to the one registered by default
	 */
	/*@Override
	public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(localeChangeInterceptor());
	}*/
	
	
    /**
     * Handles favicon.ico requests assuring no <code>404 Not Found</code> error is returned.
     * This is a great idea only in case of the project working in root context path
     * such as "http://localhost:8080/"
     */
//    @Controller
//    static class FaviconController {
//        @RequestMapping("favicon.ico")
//        public String favicon() {
//            return "forward:/resources/images/favicon.ico";
//        }
//    }
		    
}
