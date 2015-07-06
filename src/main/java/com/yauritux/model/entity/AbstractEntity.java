package com.yauritux.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;

/**
 * @author yauritux@gmail.com
 */
@MappedSuperclass
public abstract class AbstractEntity implements Persistable<String> {

	private static final long serialVersionUID = 8606310519290844496L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;
	
	@Column(name = "created_at", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Column(name = "updated_at", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;
	
	@Column(name = "deleted_at", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedAt;
	
	@Override
	public String getId() {
		return id;
	}
	
	protected void setId(final String id) {
		this.id = id;
	}
	
	@Override
	public boolean isNew() {
		return this.id == null;
	}
	
	@Override
	public String toString() {
		return String.format("Entity of type %s with id: %s", this.getClass().getName(), getId());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (this == obj) {
			return true;
		}
		
		if (!getClass().equals(obj.getClass())) {
			return false;
		}
		
		AbstractEntity entity = (AbstractEntity) obj;
		return this.id == null ? false : this.id.equalsIgnoreCase(entity.id);
	}
	
	@Override
	public int hashCode() {
		int hashCode = 17;
		hashCode += (this.id == null ? 0 : this.id.hashCode() * 31);
		return hashCode;
	}
	
	@PrePersist
	void prePersist() {
		this.createdAt = new Date();
		this.updatedAt = new Date();
	}
	
	@PreUpdate
	void preUpdate() {
		this.updatedAt = new Date();
	}
}
