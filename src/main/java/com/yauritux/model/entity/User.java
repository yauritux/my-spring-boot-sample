package com.yauritux.model.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author yauritux@gmail.com
 */
@Entity
@Table(name = "sys_users")
public class User extends AbstractEntity implements UserDetails {

	private static final long serialVersionUID = -7261685060213490133L;

	@Column(name = "username", nullable = false, unique = true)
	private String username;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "sys_role_assignments",
			joinColumns = { @JoinColumn(name = "sys_user_id", referencedColumnName = "id") },
			inverseJoinColumns = { @JoinColumn(name = "sys_role_id", referencedColumnName = "id") }
	)
	private Set<Role> roles = new HashSet<>();
	
	@Column(name = "auth_token", nullable = true)
	private String authToken;
	
	@Column(name = "auth_token_valid_thru")
	@Temporal(TemporalType.TIMESTAMP)
	private Date authTokenValidThru;
	
	protected User() {
		super();
	}
	
	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Set<Role> getRoles() {
		return roles;
	}
	
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public String getAuthToken() {
		return authToken;
	}
	
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	
	public Date getAuthTokenValidThru() {
		return authTokenValidThru;
	}
	
	public void setAuthTokenValidThru(Date authTokenValidThru) {
		this.authTokenValidThru = authTokenValidThru;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Role> roles = this.getRoles();
		
		if (roles == null) {
			return Collections.emptyList();
		}
		
		Set<GrantedAuthority> authorities = new HashSet<>();
		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
		}
		
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
