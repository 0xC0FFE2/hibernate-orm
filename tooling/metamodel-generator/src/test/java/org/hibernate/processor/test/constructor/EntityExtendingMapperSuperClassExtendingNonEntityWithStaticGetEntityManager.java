/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.processor.test.constructor;

import jakarta.persistence.Entity;

@Entity
public class EntityExtendingMapperSuperClassExtendingNonEntityWithStaticGetEntityManager
		extends MapperSuperClassExtendingNonEntityWithStaticGetEntityManager {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String otherName;

	public String getOtherName() {
		return otherName;
	}

	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}
}
