package com.yauritux.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author yauritux@gmail.com
 */
@Entity
@Table(name = "sys_roles")
public class Role extends AbstractEntity {

	private static final long serialVersionUID = 1026785770672164851L;
	
	@Column(name = "role_name", nullable = false, unique = true)
	@JsonProperty("role_name")
	private String roleName;
	
	@Column(name = "description", nullable = true)
	private String description;
	
	protected Role() {
		super();
	}
	
	public Role(String roleName) {
		super();
		this.roleName = roleName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
