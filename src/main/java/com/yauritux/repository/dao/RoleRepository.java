package com.yauritux.repository.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.yauritux.model.entity.Role;

/**
 * @author yauritux@gmail.com
 */
@RepositoryRestResource
public interface RoleRepository extends PagingAndSortingRepository<Role, String> {
	
	Role findByRoleName(final String roleName);
}
