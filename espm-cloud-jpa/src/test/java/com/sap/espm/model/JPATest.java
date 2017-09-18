package com.sap.espm.model;

import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;

import org.junit.Test;

public class JPATest extends AbstractTest {

	@Test
	public void testDropAndCreateTables() {
		EntityManager em = null;
		em = emf.createEntityManager();
		assertNotNull("Entity Manager Factory not created", em);
		em.close();
	}
}
