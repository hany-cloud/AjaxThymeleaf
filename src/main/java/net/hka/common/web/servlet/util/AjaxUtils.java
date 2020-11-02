package net.hka.common.web.servlet.util;

/**
 * It is a utility helper class used to check if the @RequestHeader with value = "X-Requested-With string is equal to "XMLHttpRequest" string 
 * @author Hany Kamal
 */
public class AjaxUtils {

    private AjaxUtils() {
    }

    public static boolean isAjaxRequest(final String requestedWith) {
    	
        return requestedWith != null ? "XMLHttpRequest".equals(requestedWith) : false;
    }
}
