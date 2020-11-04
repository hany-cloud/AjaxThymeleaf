package net.hka.common.ui.conversion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.format.Formatter;

/**
 * A general date formatter 
 * @author Hany Kamal
 */
public final class DateFormatter implements Formatter<Date> {

	private final static String DEFAULT_DATE_FORMATE = "MM/dd/yyyy";
	
    @Autowired
    private MessageSource messageSource;

    

    public DateFormatter() {
        super();
    }
    
    
    
    public Date parse(final String source, String pattern) throws ParseException {
    	
    	if(source.isEmpty()) throw new IllegalArgumentException("String source parameter is empty or null");
    	if(pattern.isEmpty()) throw new IllegalArgumentException("String pattern parameter is empty or null");
    	
    	final SimpleDateFormat dateFormat = createDateFormat(pattern);
        return dateFormat.parse(source);
    }
    
    public Date parse(final String source) throws ParseException {
    	
        return parse(source, Locale.ROOT);
    }
    
    public Date parse(final String source, final Locale locale) throws ParseException {
    	
    	if(source.isEmpty()) throw new IllegalArgumentException("The source paremter is empty");
    	if(locale == null) throw new IllegalArgumentException("The locale paremter is null");
    	
        final SimpleDateFormat dateFormat = createDateFormat(locale);
        return dateFormat.parse(source);
    }
    
    
    
    public String print(final Date date) {

    	return print(date, Locale.ROOT);
    }
    
    public String print(final Date date, final Locale locale) {

    	if(date == null) throw new IllegalArgumentException("The date paremter is empty");
    	if(locale == null) throw new IllegalArgumentException("The locale paremter is null");
    	
        final SimpleDateFormat dateFormat = createDateFormat(locale);
        return dateFormat.format(date);
    }

    private SimpleDateFormat createDateFormat(final Locale locale) {
    	
        String pattern;
        try {
        	pattern = this.messageSource.getMessage("date.format", null, locale);
		} catch(NoSuchMessageException e) {
			pattern = DEFAULT_DATE_FORMATE;
		} 
        final SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setLenient(false);
        return dateFormat;
    }
    
    private SimpleDateFormat createDateFormat(final String pattern) {
    	
        try {
        	return new SimpleDateFormat(pattern);
		} catch(Exception e) {
			return new SimpleDateFormat(DEFAULT_DATE_FORMATE);
		}         
    }
}
