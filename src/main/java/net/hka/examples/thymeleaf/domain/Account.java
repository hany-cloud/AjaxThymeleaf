package net.hka.examples.thymeleaf.domain;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "account")
public class Account {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true)
	@NotBlank
	private String email;
	
	@JsonIgnore
	@NotBlank
	private String password;

	@NotBlank
	private String role = UserRole.USER.getValue();

	@Version
	private Instant created = Instant.now();

    protected Account() {
    }
	
	public Account(final String email, final String password, final String role) {
		
		this.email = email;
		this.password = password;
		this.role = role;
		this.created = Instant.now();
	}

	public Long getId() {
		return id;
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

	public String getRole() {
		return role;
	}

	public void setRole(final String role) {
		this.role = role;
	}

	public Instant getCreated() {
		return created;
	}
}
