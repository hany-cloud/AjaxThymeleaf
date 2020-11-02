package net.hka.common.web.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public interface BaseModel {
		
	// this fixed format is always returned from any input control with type "date"
	String PARSED_DATE_FORMAT = "yyyy-MM-dd";
	
	static Date toDate(final String dateString) throws ParseException {
		
		if(dateString.isEmpty()) throw new IllegalArgumentException("String date parameter is empty or null");
		
		SimpleDateFormat sdf = new SimpleDateFormat(PARSED_DATE_FORMAT);//"yyyy.MM.dd HH:mm:ss"
		return sdf.parse(dateString);
    }
	
	static String dateFormat(final String dateString) throws ParseException {
		
		if(dateString.isEmpty()) throw new IllegalArgumentException("String date parameter is empty or null");
		
		SimpleDateFormat sdf = new SimpleDateFormat(PARSED_DATE_FORMAT);//"yyyy.MM.dd HH:mm:ss"
		Date date = sdf.parse(dateString);
        return sdf.format(date);			
    }
}
