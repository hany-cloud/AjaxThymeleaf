package net.hka.examples.thymeleaf.dto.model;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.hka.examples.thymeleaf.constraint.FieldMatch;

@SuppressWarnings("serial")
@FieldMatch(first = "password", second = "confirmPassword", message = BaseModel.PASSWORD_FIELDS_MUST_MATCH)
public class AccountDto implements Serializable, BaseModel {

	private Long id;
	
    @NotBlank(message = BaseModel.NOT_BLANK_MESSAGE)
	@Email(message = BaseModel.EMAIL_MESSAGE)
    private String email;

    @NotBlank(message = BaseModel.NOT_BLANK_MESSAGE)
    @JsonIgnore
	private String password;

    @NotBlank(message = BaseModel.NOT_BLANK_MESSAGE)
    @JsonIgnore
    private String confirmPassword;
    
    
    public Long getId() {
		return id;
	}
    
    public void setId(final Long id) {
		this.id = id;
	}
    
    public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}
	
	
	public String getConfirmPassword() {
		return confirmPassword;
	}
	
	public void setConfirmPassword(final String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	// Overriding equals for testing purposes
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        AccountDto account = (AccountDto) obj;

        if (id != null ? !id.equals(account.id) : account.id != null) return false;
        if (email != null ? !email.equals(account.email) : account.email != null) return false;
        return password != null ? password.equals(account.password) : account.password == null;
		
	}

	
}
