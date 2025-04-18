/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.orm.test.annotations.mapsid;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import org.hibernate.testing.orm.junit.DomainModel;
import org.hibernate.testing.orm.junit.SessionFactory;
import org.hibernate.testing.orm.junit.SessionFactoryScope;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SessionFactory
@DomainModel(annotatedClasses = {MapsEmbeddedIdTest.Loan.class, MapsEmbeddedIdTest.ExtensionId.class, MapsEmbeddedIdTest.Extension.class})
public class MapsEmbeddedIdTest {

	@Test void test(SessionFactoryScope scope) {
		ExtensionId eid = scope.fromTransaction( s -> {
			Loan loan = new Loan();
			loan.id = 999L;
			ExtensionId exid = new ExtensionId(loan.id, 1);
			Extension extension = new Extension();
			extension.id = exid;
			extension.loan = loan;
			extension.exExtensionDays = 30;
			loan.extensions.add(extension);
			exid = new ExtensionId(loan.id, 2);
			extension = new Extension();
			extension.id = exid;
			extension.loan = loan;
			extension.exExtensionDays = 14;
			loan.extensions.add(extension);
			s.persist(loan);
			return exid;
		});
		scope.inSession( s -> {
			List<Extension> extensions = s.createQuery("from Extension", Extension.class).getResultList();
			assertEquals(2, extensions.size());
		} );
		scope.inSession( s -> {
			Extension extension = s.find(Extension.class, eid);
			assertEquals(14, extension.exExtensionDays);
			assertEquals(2, extension.id.exNo);
			assertEquals(999L, extension.id.exLoanId);
			assertNotNull( extension.loan );
		});
		scope.inSession( s -> {
			Loan loan = s.find(Loan.class, eid.exLoanId);
			Extension extension = loan.extensions.get(0);
			assertEquals(1, extension.id.exNo);
			assertEquals(30, extension.exExtensionDays);
			assertEquals(999L, extension.id.exLoanId);
			assertEquals(loan, extension.loan);
		});
	}

	@Entity(name = "Loan")
	static class Loan {
		@Id
		@Column(name = "LOAN_ID")
		private Long id;

		private BigDecimal amount = BigDecimal.ZERO;

		@OneToMany(cascade = CascadeType.ALL, mappedBy = "loan")
		private List<Extension> extensions = new ArrayList<>();
	}

	@Embeddable
	public static class ExtensionId {

		private Long exLoanId;

		@Column(name="EX_NO")
		private int exNo;

		public ExtensionId(Long exLoanId, int exNo) {
			this.exLoanId = exLoanId;
			this.exNo = exNo;
		}

		public ExtensionId() {
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof ExtensionId)) return false;
			ExtensionId that = (ExtensionId) o;
			return exNo == that.exNo && Objects.equals(exLoanId, that.exLoanId);
		}

		@Override
		public int hashCode() {
			return Objects.hash(exLoanId, exNo);
		}
	}

	@Entity(name = "Extension")
	static class Extension {

		@EmbeddedId
		private ExtensionId id;

		@Column(name = "EX_EXTENSION_DAYS")
		private int exExtensionDays;

		@ManyToOne
		@MapsId("exLoanId")
		@JoinColumn(name = "EX_LOAN_ID")
		private Loan loan;
	}
}
