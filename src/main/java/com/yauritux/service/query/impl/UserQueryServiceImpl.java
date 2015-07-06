package com.yauritux.service.query.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yauritux.model.entity.User;
import com.yauritux.repository.dao.UserRepository;
import com.yauritux.service.query.api.UserQueryService;

/**
 * @author yauritux@gmail.com
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class UserQueryServiceImpl implements UserQueryService {
	
	private UserRepository userRepository;
	
	@Autowired
	public UserQueryServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Cannot find user '" + username + "'");
		}		
		
		return user;
	}

	@Override
	public User findByUsername(String username) {
		if (username == null) {
			return null;
		}
		
		return userRepository.findByUsername(username);
	}
}
