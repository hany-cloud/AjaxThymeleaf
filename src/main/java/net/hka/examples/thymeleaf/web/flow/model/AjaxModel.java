package net.hka.examples.thymeleaf.web.flow.model;

import java.io.Serializable;
import java.util.Date;

public class AjaxModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String currentDate;

	public String getCurrentDate() {
		currentDate = new Date().toString();
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}
}
