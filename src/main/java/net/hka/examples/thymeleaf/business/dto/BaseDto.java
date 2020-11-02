package net.hka.examples.thymeleaf.business.dto;

import net.hka.common.web.model.BaseModel;

interface BaseDto extends BaseModel {
		
	String NOT_BLANK_MESSAGE = "{notBlank.message}";
	String MIN_SIZE_MESSAGE = "{min.size.message}";
	String EMAIL_MESSAGE = "{email.message}";
	String PASSWORD_FIELDS_MUST_MATCH = "{password.fields.must.match}";
	
	String DATE_FORMAT = "{date.format}";	
}