/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.orm.test.envers.integration.onetoone.bidirectional.ids;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

import org.hibernate.envers.Audited;
import org.hibernate.orm.test.envers.entities.ids.EmbId;

/**
 * @author Adam Warski (adam at warski dot org)
 */
@Entity
public class BiEmbIdRefEdEntity {
	@EmbeddedId
	private EmbId id;

	@Audited
	private String data;

	@Audited
	@OneToOne(mappedBy = "reference")
	private BiEmbIdRefIngEntity referencing;

	public BiEmbIdRefEdEntity() {
	}

	public BiEmbIdRefEdEntity(EmbId id, String data) {
		this.id = id;
		this.data = data;
	}

	public BiEmbIdRefEdEntity(EmbId id, String data, BiEmbIdRefIngEntity referencing) {
		this.id = id;
		this.data = data;
		this.referencing = referencing;
	}

	public EmbId getId() {
		return id;
	}

	public void setId(EmbId id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public BiEmbIdRefIngEntity getReferencing() {
		return referencing;
	}

	public void setReferencing(BiEmbIdRefIngEntity referencing) {
		this.referencing = referencing;
	}

	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( !(o instanceof BiEmbIdRefEdEntity) ) {
			return false;
		}

		BiEmbIdRefEdEntity that = (BiEmbIdRefEdEntity) o;

		if ( data != null ? !data.equals( that.data ) : that.data != null ) {
			return false;
		}
		if ( id != null ? !id.equals( that.id ) : that.id != null ) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		int result;
		result = (id != null ? id.hashCode() : 0);
		result = 31 * result + (data != null ? data.hashCode() : 0);
		return result;
	}
}
