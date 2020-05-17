package net.hka.examples.thymeleaf.constraint;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;


/**
 * 
 * @author Hany K Abdelsayed
 *
 * @param <T> 
 * 
 * Validate the model sent as generic parameter to the class against it's annotation constraints 
 * 
 */
@Component
public /*abstract*/ class ModelValidator<T> {
	
	@Autowired
    private MessageSource messageSource;
	
	private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	/**
	 * 
	 * @param model
	 * @param errors
	 * 
	 * This method is to validate model against it's annotation constraints and setting the @org.springframework.validation.Error by rejected values.
	 */
	public void validateModel(final T model, Errors errors) {
        Set<ConstraintViolation<T>> violations = validator.validate(model);
        if(violations != null && violations.size() > 0) {
        	for (ConstraintViolation<T> violation : violations) {
        		String sourceName = violation.getPropertyPath().toString();
        		String codeOrMessage = violation.getMessage()
        				.replace("{", "")
        				.replace("}", "");
        		
        		errors.rejectValue(sourceName, codeOrMessage, "Inncorrect" + sourceName + "!");
        	}
        	
        }
    }
	
	/**
	 * 
	 * @param model
	 * @param context
	 * @param args
	 * 
	 * This method is to validate model against it's annotation constraints and setting the @org.springframework.binding.validation.ValidationContext.
	 */
	public void validateModel(final T model, ValidationContext context, final Object...args) {
        MessageContext messages = context.getMessageContext();
        this.validateModel(model, messages, args);
    }
	
	
	/**
	 * 
	 * @param model
	 * @param messages
	 * @return Set<ConstraintViolation<T>>
	 * 
	 * This method is to validate model against it's annotation constraints.
	 * and displaying manipulated error messages using that can be sent as a code from property file or plain text.
	 * This method gives another feature in the message manipulation by replacing all attributes that can be sent within the message 
	 * in curly braces. 
	 * i.e
	 *  	in property file: min.size.message = The value should not be less than {value}!
	 *  The message attributes are always expected to be one of annotation attributes such as value in the examples 
	 *  
	 */
	public Set<ConstraintViolation<T>> validateModel(final T model, MessageContext messages) {
        Set<ConstraintViolation<T>> violations = validator.validate(model);
        if(violations != null && violations.size() > 0) {
        	for (ConstraintViolation<T> violation : violations) {
        		String sourceName = violation.getPropertyPath().toString();
        		String codeOrMessage = violation.getMessage()
        				.replace("{", "")
        				.replace("}", "");
        		
        		// get the actual message
        		StringBuilder message = new StringBuilder();
        		try {
        			message.append(this.messageSource.getMessage(codeOrMessage, null, LocaleContextHolder.getLocale())); 
        		} catch(NoSuchMessageException e) {
        			message.append(codeOrMessage);
        		} 
        		
        		// extract attributes from the message if any exist, and replace them by actual values in the message
        		Matcher m = Pattern.compile( "\\{(.*?)\\}"   ).matcher(message);
        		while(m.find()) {
        			// get actual attribute value in the message
        			String attrVal = violation.getConstraintDescriptor().getAttributes().getOrDefault(m.group(1), m.group()).toString();
        			
        			//int start = message.indexOf(m.group());
        			//int end = start + m.group().length();
        			//message.replace(start, end, attrVal);
        			
        			// replace the attribute in the message by the attribute value
        			message = new StringBuilder(message.toString().replace(m.group(), attrVal));
        		}
        		
        		// set your messages by the final message value
        		messages.addMessage(new MessageBuilder().error().source(sourceName).
						defaultText(message.toString()).build());
        		
        	}
        }
        
        return violations;
    }
	
	/**
	 * 
	 * @param model
	 * @param messages
	 * @param args
	 * @return violations
	 * 
	 * This method is to validate model against it's annotation constraints and setting the @org.springframework.binding.message.MessageContext.
	 */
	public Set<ConstraintViolation<T>> validateModel(final T model, MessageContext messages, final Object...args) {
        Set<ConstraintViolation<T>> violations = validator.validate(model);
        if(violations != null && violations.size() > 0) {
        	for (ConstraintViolation<T> violation : violations) {
        		String sourceName = violation.getPropertyPath().toString();
        		String codeOrMessage = violation.getMessage()
        				.replace("{", "")
        				.replace("}", "");
        		
        		if(args != null)
	        		messages.addMessage(new MessageBuilder().error().source(sourceName).
							code(codeOrMessage).args(args).defaultText(codeOrMessage).build());
        		else 
        			messages.addMessage(new MessageBuilder().error().source(sourceName).
							code(codeOrMessage).defaultText(codeOrMessage).build());
        	}
        }
        
        return violations;
    }
	
}
