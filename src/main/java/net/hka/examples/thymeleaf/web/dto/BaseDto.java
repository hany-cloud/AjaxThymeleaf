package net.hka.examples.thymeleaf.web.dto;

import java.io.Serializable;

public interface BaseDto extends Serializable {
		
	String NOT_BLANK_MESSAGE = "{notBlank.message}";
	String MIN_SIZE_MESSAGE = "{min.size.message}";
	String EMAIL_MESSAGE = "{email.message}";
	String PASSWORD_FIELDS_MUST_MATCH = "{password.fields.must.match}";
	
	String DATE_FORMAT = "{date.format}";	
	
	// This fixed format is always returned from any input control with type "date"
	String PARSED_DATE_FORMAT = "yyyy-MM-dd";
}