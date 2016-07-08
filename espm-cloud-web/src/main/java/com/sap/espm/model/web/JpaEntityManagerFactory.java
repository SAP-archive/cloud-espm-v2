package com.sap.espm.model.web;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;

import org.eclipse.persistence.config.PersistenceUnitProperties;

import com.sap.espm.model.util.Utility;

/**
 * Handles the singleton EntityManagerFactory instance.
 */
public class JpaEntityManagerFactory {

	public static final String DATA_SOURCE_NAME = "java:comp/env/jdbc/DefaultDB";
	public static final String PERSISTENCE_UNIT_NAME = "com.sap.espm.model";

	private static EntityManagerFactory entityManagerFactory = null;

	/**
	 * Returns the singleton EntityManagerFactory instance for accessing the
	 * default database.
	 * 
	 * @return the singleton EntityManagerFactory instance
	 * @throws NamingException
	 *             if a naming exception occurs during initialization
	 * @throws SQLException
	 *             if a database occurs during initialization
	 */
	public static synchronized EntityManagerFactory getEntityManagerFactory()
			throws NamingException, SQLException {
		if (entityManagerFactory == null) {
			InitialContext ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup(DATA_SOURCE_NAME);
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, ds);
			entityManagerFactory = Persistence.createEntityManagerFactory(
					PERSISTENCE_UNIT_NAME, properties);
			Utility.setEntityManagerFactory(entityManagerFactory);
		}
		return entityManagerFactory;
	}

}
