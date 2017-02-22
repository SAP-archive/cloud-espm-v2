package com.sap.espm.model.keystore;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.sap.cloud.security.password.PasswordStorage;
import com.sap.cloud.security.password.PasswordStorageException;

/**
 * This Singleton class is used to store the Password keystore. This password
 * key store is configured via a JNDI parameter
 * ("java:comp/env/PasswordStorage")
 * <p>
 * After fetching the data via JNDI, the password related data is in a
 * {@link PasswordStorage} object.
 * 
 *
 */
public class passwordKeyStore {

	private static passwordKeyStore pks;

	/**
	 * The {@link PasswordStorage}
	 */
	private PasswordStorage passwordStorage;

	/**
	 * The static instance of the class used for the Singleton pattern.
	 */
	private static passwordKeyStore instance = null;

	/**
	 * The private constructor used for the Singleton design pattern.
	 * <p>
	 * This constructor will look up via JNDI parameter
	 * ("java:comp/env/PasswordStorage").
	 */
	private passwordKeyStore() {
		InitialContext ctx;
		try {
			ctx = new InitialContext();
			passwordStorage = (PasswordStorage) ctx.lookup("java:comp/env/PasswordStorage");
		} catch (NamingException e) {
			e.printStackTrace();
		}

	}

	/**
	 * The static getInstace method used to return the single instance of the
	 * {@link passwordKeyStore} class.
	 * 
	 * @return - The {@link passwordKeyStore}
	 */
	public static passwordKeyStore getInstance() {
		if (instance == null)
			instance = new passwordKeyStore();
		return instance;
	}

	/**
	 * This method is used to set the new password in the
	 * {@link PasswordStorage}.
	 * 
	 * @param alias
	 *            - The new alias.
	 * @param password
	 *            - The new password to set.
	 * @throws PasswordStorageException
	 *             - In case of any exception while setting the new password.
	 * @throws NamingException-
	 *             In case of any exception while fetching the password storage
	 *             via JNDI.
	 */
	public void setPassword(String alias, char[] password) throws PasswordStorageException, NamingException {
		this.passwordStorage.setPassword(alias, password);
	}

	/**
	 * This method is used to return the password stored in the
	 * {@link PasswordStorage}.
	 * 
	 * @param alias
	 *            - The alias of the password
	 * @return - The character array of the password.
	 * @throws PasswordStorageException
	 *             - In case of any exception while getting the password.
	 * @throws NamingException
	 *             - In case of exception while getting the password via JNDI.
	 */
	public char[] getPassword(String alias) throws PasswordStorageException, NamingException {
		return this.passwordStorage.getPassword(alias);
	}

	/**
	 * This method is used to delete the password stored in the
	 * {@link PasswordStorage}.
	 * 
	 * @param alias
	 *            - The alias of the password.
	 * @throws PasswordStorageException
	 *             - In case of any exception while deleting the password
	 * @throws NamingException
	 *             - In case of any exception while fetching/deleting the
	 *             password data via JNDI.
	 */
	public void deletePassword(String alias) throws PasswordStorageException, NamingException {

		this.passwordStorage.deletePassword(alias);
	}

}
