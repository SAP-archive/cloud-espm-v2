package com.sap.espm.model.util;

import javax.persistence.EntityManagerFactory;

/**
 * 
 * This is a Singleton Utility class that is used to Configure an
 * {@link EntityManagerFactory}.
 * <p>
 * Refer to the Documentation <i>
 * (http://docs.oracle.com/javaee/7/api/javax/persistence/EntityManagerFactory.html)
 * </i> for more information on how to configure an EntityManager.
 * <p>
 * For more information regarding the Model classes and other JPA related
 * configuration details, refer to the "META-INF/persistence.xml" file in the
 * resources folder.
 * 
 */
public class Utility {

	/**
	 * The {@link EntityManagerFactory} instance.
	 */
	private static EntityManagerFactory emf;

	/**
	 * The static method to return the {@link EntityManagerFactory} instance.
	 * @return
	 */
	public static EntityManagerFactory getEntityManagerFactory() {
		if (emf == null) {
			throw new IllegalArgumentException(
					"EntityManagerfactory is not initialized!!!");
		}
		return emf;
	}

	/**
	 * Setter for the {@link EntityManagerFactory}
	 * @param emf - The {@link EntityManagerFactory} to set.
	 */
	public static void setEntityManagerFactory(EntityManagerFactory emf) {
		Utility.emf = emf;
	}

}
