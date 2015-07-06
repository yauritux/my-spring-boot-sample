package com.yauritux.repository.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.yauritux.model.entity.Contact;

/**
 * @author yauritux@gmail.com
 */
@RepositoryRestResource
public interface ContactRepository extends PagingAndSortingRepository<Contact, String> {
}
