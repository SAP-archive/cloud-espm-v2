package com.sap.espm.model.keystore;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.sap.cloud.security.password.PasswordStorage;
import com.sap.cloud.security.password.PasswordStorageException;

public class passwordKeyStore {
	private static passwordKeyStore pks;
	private PasswordStorage passwordStorage;
	private static passwordKeyStore instance= null;
	
	private passwordKeyStore(){
		InitialContext ctx;
		try {
			ctx = new InitialContext();
			passwordStorage = (PasswordStorage) ctx.lookup("java:comp/env/PasswordStorage");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		
	}
	public static passwordKeyStore getInstance(){
		if (instance == null)
	           instance = new passwordKeyStore();
	       return instance;
	}
	
	public void setPassword(String alias, char[] password) throws PasswordStorageException, NamingException {
		this.passwordStorage.setPassword(alias, password);
	  }
	 
	  public char[] getPassword(String alias) throws PasswordStorageException, NamingException {
	   return this.passwordStorage.getPassword(alias);
	  }
	 
	  public void deletePassword(String alias) throws PasswordStorageException, NamingException {
	   
	    this.passwordStorage.deletePassword(alias);
	  }
	

}
