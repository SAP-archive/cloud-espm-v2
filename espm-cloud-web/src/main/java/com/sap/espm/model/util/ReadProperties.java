package com.sap.espm.model.util;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This singleton class is used read the property file "config.properties" in
 * the resources folder.
 * <p>
 * As of now the property file holds the key and value used to connect to the
 * Document Repository. This property file can be scaled up for further
 * properties if need arises.
 */
public class ReadProperties{
	
	/**
	 * Logger instance.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ReadProperties.class);

	/**
	 * The static instance of the class.
	 */
   private static ReadProperties instance = null;

   /**
    * The {@link Properties} object that will hold the key and values.
    */
   private Properties props = null;

	/**
	 * Private constructor used in the Singleton pattern. This will load the
	 * property file content and store the contents in the {@link Properties}
	 * object.
	 */
   private ReadProperties(){
	   props = new Properties();
       try {
		props.load(getClass().getResourceAsStream("/config.properties"));
	} catch (IOException e) {
		LOGGER.error(e.getMessage());
	}
   }

	/**
	 * Static getInstance method used to return the contents of the property
	 * file returned in a {@link Properties} object.
	 * 
	 * @return - The Instance of this class.
	 */
   public static synchronized ReadProperties getInstance(){
       if (instance == null)
           instance = new ReadProperties();
       return instance;
   }

	/**
	 * This method will be used to fetch a particular value from the property
	 * file.
	 * 
	 * @param propKey
	 *            - The Key of the property.
	 * @return - The value of the property, null if does not exist.
	 */
   public String getValue(String propKey){
       return this.props.getProperty(propKey);
   }
}