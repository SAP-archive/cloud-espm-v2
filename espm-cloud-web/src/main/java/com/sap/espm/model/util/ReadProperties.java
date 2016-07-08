package com.sap.espm.model.util;

import java.io.IOException;
import java.util.Properties;
/**
 * Reading property file
 *
 */
public class ReadProperties{

   private static ReadProperties instance = null;

   private Properties props = null;

   private ReadProperties(){
	   props = new Properties();
       try {
		props.load(getClass().getResourceAsStream("/config.properties"));
	} catch (IOException e) {
		e.printStackTrace();
	}
   }

   public static synchronized ReadProperties getInstance(){
       if (instance == null)
           instance = new ReadProperties();
       return instance;
   }

   public String getValue(String propKey){
       return this.props.getProperty(propKey);
   }
}