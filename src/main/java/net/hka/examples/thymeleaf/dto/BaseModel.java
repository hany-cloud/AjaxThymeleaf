package net.hka.examples.thymeleaf.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public interface BaseModel {
	String NOT_BLANK_MESSAGE = "{notBlank.message}";
	String MIN_SIZE_MESSAGE = "{min.size.message}";
	String EMAIL_MESSAGE = "{email.message}";
	String PASSWORD_FIELDS_MUST_MATCH = "{password.fields.must.match}";
	
	String DATE_FORMAT = "{date.format}";
	
	// this fixed format is always returned from any input control with type "date"
	String PARSED_DATE_FORMAT = "yyyy-MM-dd";
	
	public static Date toDate(final String dateString) throws ParseException {
		if(dateString.isEmpty()) throw new IllegalArgumentException("String date parameter is empty or null");
		SimpleDateFormat sdf = new SimpleDateFormat(PARSED_DATE_FORMAT);//"yyyy.MM.dd HH:mm:ss"
		return sdf.parse(dateString);
    }
	
	public static String dateFormat(final String dateString) throws ParseException {
		if(dateString.isEmpty()) throw new IllegalArgumentException("String date parameter is empty or null");
		SimpleDateFormat sdf = new SimpleDateFormat(PARSED_DATE_FORMAT);//"yyyy.MM.dd HH:mm:ss"
		Date date = sdf.parse(dateString);
        return sdf.format(date);			
    }
}
