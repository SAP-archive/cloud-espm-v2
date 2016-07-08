package com.sap.espm.model.util;

import javax.persistence.EntityManagerFactory;

/**
 * 
 * Utility class for initializing entity manager factory.
 * 
 */
public class Utility {

	private static EntityManagerFactory emf;

	public static EntityManagerFactory getEntityManagerFactory() {
		if (emf == null) {
			throw new IllegalArgumentException(
					"EntityManagerfactory is not initialized!!!");
		}
		return emf;
	}

	public static void setEntityManagerFactory(EntityManagerFactory emf) {
		Utility.emf = emf;
	}

}
