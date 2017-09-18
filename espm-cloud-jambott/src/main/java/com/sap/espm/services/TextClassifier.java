package com.sap.espm.services;

/**
 * A Java class that implements a simple text classifier, based on WEKA.
 * To be used with MyFilteredLearner.java.
 * WEKA is available at: http://www.cs.waikato.ac.nz/ml/weka/
 * Copyright (C) 2013 Jose Maria Gomez Hidalgo - http://www.esp.uem.es/jmgomez
 *
 * This program is free software: you can redistribute it and/or modify
 * it for any purpose.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
 
import weka.core.*;
import weka.classifiers.meta.FilteredClassifier;
//import java.util.logging.Logger;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;

/**
 * This class implements a simple text classifier in Java using WEKA.
 * It loads a file with the text to classify, and the model that has been
 * learnt with MyFilteredLearner.java.
 * @author Jose Maria Gomez Hidalgo - http://www.esp.uem.es/jmgomez
 * @see TextLearner
 */

 public class TextClassifier {
//	 private static final Logger logger = Logger.getLogger(WebhooksServlet.class.getName());
	 public TextClassifier() {
	    	super();
	    }

	/**
	 * String that stores the text to classify
	 */
	 private static  String text;
	/**
	 * Object that stores the instance.
	 */
	 private static  Instances instances;
	/**
	 * Object that stores the classifier.
	 */
	 private static FilteredClassifier classifier;
	 private static boolean doClassPredicted = false;
		
	/**
	 * This method loads the text to be classified.
	 * @param logger 
	 * @param fileName The name of the file that stores the text.
	 * @throws Exception 
	 */
	public void load(String testCase, Logger logger) throws Exception {
		try {
			if(testCase != null){
				text = testCase;
				logger.info("Message to classify "+text);
			}else{
				throw new UnhandledException("Message to classify is null");
			}
		}
		catch (UnhandledException e) {
			logger.info("Problem found when reading: " + testCase);
			throw new Exception("Problem found when reading: " + testCase);
		}
	}
			
	/**
	 * This method loads the model to be used as classifier.
	 * @param fileName The name of the file that stores the text.
	 * @param logger 
	 * @throws UnhandledException 
	 */
	public void loadModel(String fileName, Logger logger) throws UnhandledException {
		InputStream file = null;
		try {
			file=this.getClass().getResourceAsStream(fileName);
			
			classifier = (FilteredClassifier) weka.core.SerializationHelper.read(file);
       		} catch (UnhandledException e) {
			// Given the cast, a ClassNotFoundException must be caught along with the IOException
			logger.info("Problem found when reading: " + fileName+ " exception"+e);
			throw new UnhandledException("Problem found when reading: " + fileName);
		}
		catch(ClassNotFoundException e){
			logger.info("Problem found when reading: " + fileName+ " exception"+e);
			throw new UnhandledException("Problem found when reading: " + fileName);
		}catch(IOException e){
			logger.info("Problem found when reading: " + fileName+ " exception"+e);
			throw new UnhandledException("Problem found when reading: " + fileName);
		}catch (Exception e) {
			logger.info("Problem found when reading: " + fileName+ " exception"+e);
			throw new UnhandledException("Problem found when reading: " + fileName);
		}finally{
			if (file != null) 
	 			{ 
	 				try { 
	 					file.close(); 
	 				} catch (IOException e) 
	 				{ 
	 					logger.info("Problem found when closing stream"); 
	 				}
	 			}
			
		}
	}
	/**
	 * This method creates the instance to be classified, from the text that has been read.
	 */
	public void makeInstance(String categories) {
		 ArrayList<Attribute> atts = new ArrayList<Attribute>(2);
	        ArrayList<String> classVal = new ArrayList<String>();
	        String[] categoriesList = categories.split(",");
	        for(int i=0; i<categoriesList.length;i++){
	        	classVal.add(categoriesList[i]);
	        }
	        atts.add(new Attribute("class",classVal));
	        atts.add(new Attribute("text",(ArrayList<String>)null));
	        instances = new Instances("Test relation",atts,1);
	        instances.setClassIndex(0);
	        DenseInstance instance = new DenseInstance(2);
	        instance.setValue(instances.attribute(1), text);
	        instances.add(instance);
	}
	
	/**
	 * This method performs the classification of the instance.
	 * Output is done at the command-line.
	 * @param logger 
	 * @return 
	 * @throws UnhandledException 
	 */
	public String classify(Logger logger) throws UnhandledException {
		logger.info("Inside classify");
		String classPredicted = "Sorry, Couldnt predict the Class";
		try {
			double pred = classifier.classifyInstance(instances.instance(0));
			classPredicted =  instances.classAttribute().value((int) pred);
			// Get the prediction probability distribution.
	        double[] predictionDistribution = 
	            classifier.distributionForInstance(instances.instance(0)); 
	        // Loop over all the prediction labels in the distribution.
	        for (int predictionDistributionIndex = 0; 
	             predictionDistributionIndex < predictionDistribution.length; 
	             predictionDistributionIndex++)
	        {
	            // Get this distribution index's class label.
	            String predictionDistributionIndexAsClassLabel = 
	                instances.classAttribute().value(
	                    predictionDistributionIndex);

	            // Get the probability.
	            double predictionProbability = 
	                predictionDistribution[predictionDistributionIndex];
	            
	            logger.info("Prediction Distribution " + predictionDistributionIndexAsClassLabel);
	            logger.info("Prediction Probability " + predictionProbability);
	            if(predictionProbability >= 0.5){
	            	doClassPredicted = true;
	            }
	        }
		}catch (ArrayIndexOutOfBoundsException e) {
			logger.info("Problem found when classifying the text. "+e.getMessage());
			throw new UnhandledException("Problem found when classifying the text. "+e.getMessage());
		}
		catch (Exception e) {
			logger.info("Problem found when classifying the text. "+e.getMessage());
			throw new UnhandledException("Problem found when classifying the text. "+e.getMessage());
		}
		if(!doClassPredicted){
			classPredicted="None";
		}
		logger.info("inside classify end"+classPredicted);
		System.out.println("inside classify end "+classPredicted);
		return classPredicted;		
	}
	
	/**
	 * Main method. It is an example of the usage of this class.
	 * @param args Command-line arguments: fileData and fileModel.
	 */
//	public static void main(String[] args) {
//		Logger logger = LoggerFactory.getLogger(OdataClient.class.getName());
//		Utility util = new Utility();
//		TextClassifier classifier;
//			classifier = new TextClassifier();
//			try {
//				classifier.load("get me bottom rated products for middle aged group", logger);
//				classifier.loadModel("C:/Users/i321572/git/espmv2.0/espm-cloud-jambott/src/main/resources/utils/classifier1.dat", logger);
//				classifier.makeInstance(util.get("categories"));
//				classifier.classify(logger);
//			}catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	}
}	
