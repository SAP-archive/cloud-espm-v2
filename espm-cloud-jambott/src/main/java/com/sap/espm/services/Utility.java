package com.sap.espm.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
//import java.util.logging.Logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utility {
	private static final Properties properties = new Properties();
//	private final Logger logger = Logger.getLogger(WebhooksServlet.class.getName());
	private static final Logger LOGGER = LoggerFactory.getLogger(Utility.class);
	private final String path = "/utils/util.properties";
	private final InputStream tempClass = this.getClass().getResourceAsStream(path);
	
	public Utility(){
		super();
		loadProperties();
	}
	
    private void loadProperties() {
    	try {
			properties.load(tempClass);
		} catch (IOException e) {
			LOGGER.error("Application couldnt load properties file");
		}
	}

	public String get(String key){
    	
        return (String)properties.get(key); 
    }

	public Logger getLogger() {
		return LOGGER;
	}

}
