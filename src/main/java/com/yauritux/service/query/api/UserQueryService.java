package com.yauritux.service.query.api;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.yauritux.model.entity.User;

/**
 * @author yauritux@gmail.com
 */
public interface UserQueryService extends UserDetailsService {
	
	User findByUsername(final String username);
}
