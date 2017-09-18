package com.sap.espm.model;

import javax.persistence.EntityManagerFactory;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.sap.espm.model.util.TestFactory;

public abstract class AbstractTest {

	protected static EntityManagerFactory emf;

	@BeforeClass
	public static void setup() {
		emf = TestFactory
				.createEntityManagerFactory(TestFactory.PERSISTENCE_UNIT);
	}

	@AfterClass
	public static void shutdown() {
		emf.close();
	}

}
