package com.yauritux.repository.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.yauritux.model.entity.User;

/**
 * @author yauritux@gmail.com
 */
@RepositoryRestResource
public interface UserRepository extends PagingAndSortingRepository<User, String> {
	
	public User findByUsername(String username);
}
