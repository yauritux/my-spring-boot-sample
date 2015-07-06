package com.yauritux.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author yauritux@gmail.com
 */
@Entity
@Table(name = "m_contacts")
public class Contact extends AbstractEntity {

	private static final long serialVersionUID = 299823772365685158L;

	@Column(name = "first_name", nullable = false)
	@JsonProperty("first_name")
	private String firstName;
	
	@Column(name = "last_name", nullable = false)
	@JsonProperty("last_name")
	private String lastName;
	
	private boolean male;
	
	@Column(name = "address", nullable = true)
	private String address;
	
	@Column(name = "phone_number", nullable = true)
	@JsonProperty("phone_number")
	private String phone;
	
	@Column(name = "email", nullable = true)
	private String email;
	
	protected Contact() {
		super();
	}
	
	public Contact(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isMale() {
		return male;
	}

	public void setMale(boolean male) {
		this.male = male;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
