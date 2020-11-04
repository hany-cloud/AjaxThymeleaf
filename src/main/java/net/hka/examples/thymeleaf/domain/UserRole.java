package net.hka.examples.thymeleaf.domain;

public enum UserRole {
	
	USER("ROLE_USER"), 
	ADMIN("ROLE_ADMIN");

	private String value;

	private UserRole(final String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	// return a label formated string by capitalize the first character and make
	// rest of the role word as lower case
	public String getValueAsLabel() {
		return Character.toUpperCase(this.value.charAt(0)) + this.value.toLowerCase().substring(1);
	}
}
