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
 * Handles the singleton {@link EntityManagerFactory} instance.
 * <p>
 * This class is responsible for fetching the details of the {@link DataSource}
 * that has the connection related details (Host, username, password) on how to
 * connect to the data source.
 * <p>
 * <b>Note</b> - This class fetches the {@link DataSource} details via JNDI, so
 * ensure that you have the DataSource configured as a JNDI lookup variable in
 * the respective ServletContainer where the application is deployed.
 */
public class JpaEntityManagerFactory {

	/**
	 * The JNDI name of the DataSource.
	 */
	public static final String DATA_SOURCE_NAME = "java:comp/env/jdbc/DefaultDB";
	
	/**
	 * The package name which contains all the model classes.
	 */
	public static final String PERSISTENCE_UNIT_NAME = "com.sap.espm.model";

	/**
	 * The static {@link EntityManagerFactory}
	 */
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
