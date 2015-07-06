package com.yauritux.repository.eventhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeLinkDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeLinkSave;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.yauritux.model.entity.Contact;

/**
 * @author yauritux@gmail.com
 */
@Service
@RepositoryEventHandler(Contact.class)
@Secured("ROLE_ADMIN")
public class ContactEventHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ContactEventHandler.class);
	
	@HandleBeforeSave
	public void handleBeforeSave(Contact c) {
		LOG.debug("Before save " + c);
	}
	
	@HandleBeforeCreate
	public void handleBeforeCreate(Contact c) {
		LOG.debug("Before create " + c);
	}
	
	@HandleBeforeLinkSave
	public void handleBeforeLinkSave(Contact c) {
		LOG.debug("Before link save " + c);
	}
	
	@HandleBeforeDelete
	public void handleBeforeDelete(Contact c) {
		LOG.debug("Before delete " + c);
	}
	
	@HandleBeforeLinkDelete
	public void handleBeforeLinkDelete(Contact c) {
		LOG.debug("Before link delete " + c);
	}
}
