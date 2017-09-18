package com.sap.espm.services;
/**
 * A Java class that implements a simple text learner, based on WEKA.
 * To be used with MyFilteredClassifier.java.
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

import weka.core.Instances;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.classifiers.Evaluation;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.converters.ArffLoader.ArffReader;
import java.io.*;

/**
 * This class implements a simple text learner in Java using WEKA.
 * It loads a text dataset written in ARFF format, evaluates a classifier on it,
 * and saves the learnt model for further use.
 * @author Jose Maria Gomez Hidalgo - http://www.esp.uem.es/jmgomez
 * @see TextClassifier
 */
public class TextLearner {

	/**
	 * Object that stores training data.
	 */
	private static Instances trainData;
	private static final Logger logger = LoggerFactory.getLogger(WebhooksServlet.class);
	/**
	 * Object that stores the filter
	 */
	private static StringToWordVector filter;
	/**
	 * Object that stores the classifier
	 */
	private static FilteredClassifier classifier;
		
	/**
	 * This method loads a dataset in ARFF format. If the file does not exist, or
	 * it has a wrong format, the attribute trainData is null.
	 * @param fileName The name of the file that stores the dataset.
	 */
	public void loadDataset(String fileName) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			ArffReader arff = new ArffReader(reader);
			trainData = arff.getData();
			logger.info("===== Loaded dataset: " + fileName + " =====");
			reader.close();
		}
		catch (IOException e) {
			logger.error("Problem found when reading: " + fileName+". \n"+e.getMessage());
		}finally {
			if (reader != null) 
			{ 
				try { 
					reader.close(); 
				} catch (IOException e) 
				{ 
					logger.info("Problem found when closing stream"); 
				}
			}
		}
	}
	
	/**
	 * This method evaluates the classifier. As recommended by WEKA documentation,
	 * the classifier is defined but not trained yet. Evaluation of previously
	 * trained classifiers can lead to unexpected results.
	 */
	public void evaluate() {
		try {
			trainData.setClassIndex(0);
			filter = new StringToWordVector();
//			filter.setWordsToKeep(1000000);
//			filter.setIDFTransform(true);
//			filter.setTFTransform(true);
//			filter.setLowerCaseTokens(true);
//		    filter.setOutputWordCounts(true);
			filter.setAttributeIndices("last");
			classifier = new FilteredClassifier();
			classifier.setFilter(filter);
			classifier.setClassifier(new NaiveBayes());
			Evaluation eval = new Evaluation(trainData);
//			Random rnd = new Random();
//			rnd.setSeed(1);
			eval.crossValidateModel(classifier, trainData, 4, new Random());
			logger.info(eval.toSummaryString());
			logger.info(eval.toClassDetailsString());
			logger.info("===== Evaluating on filtered (training) dataset done =====");
		}
		catch (Exception e) {
			logger.info("Problem found when evaluating.\n"+e.getMessage());
		}
	}
	
	/**
	 * This method trains the classifier on the loaded dataset.
	 */
	public void learn() {
		try {
			trainData.setClassIndex(0);
			filter = new StringToWordVector();
			filter.setAttributeIndices("last");
			classifier = new FilteredClassifier();
			classifier.setFilter(filter);
			classifier.setClassifier(new NaiveBayes());
			classifier.buildClassifier(trainData);
			logger.info("===== Training on filtered (training) dataset done =====");
		}
		catch (Exception e) {
			logger.info("Problem found when training.\n"+e.getMessage());
		}
	}
	
	/**
	 * This method saves the trained model into a file. This is done by
	 * simple serialization of the classifier object.
	 * @param fileName The name of the file that will store the trained model.
	 */
	public void saveModel(String fileName) {
		ObjectOutputStream out = null;
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(fileName);
            out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(classifier);
            out.close();
            logger.info("===== Saved model: " + fileName + " =====");
        } 
		catch (IOException e) {
			logger.info("Problem found when writing: " + fileName);
		}finally{
			if (out != null) 
			{ 
				try { 
					out.close(); 
				} catch (IOException e) 
				{ 
					logger.info("Problem found when closing stream"); 
				}
			}
			if (fileOutputStream != null) 
			{ 
				try { 
					fileOutputStream.close(); 
				} catch (IOException e) 
				{ 
					logger.info("Problem found when closing stream"); 
				}
			}
		}
	}
	
	/**
	 * Main method. It is an example of the usage of this class.
	 * @param args Command-line arguments: fileData and fileModel.
	 */
//	public static void main (String[] args) {
//	
//		TextLearner learner;
////		if (args.length < 2)
////			System.out.println("Usage: java MyLearner <fileData> <fileModel>");
////		else {
//			learner = new TextLearner();
//			learner.loadDataset("C:/Users/i321572/git/espmv2.0/espm-cloud-jambott/src/main/resources/utils/espmdataset.arff");
//			// Evaluation mus be done before training
//			// More info in: http://weka.wikispaces.com/Use+WEKA+in+your+Java+code
//			learner.evaluate();
//			learner.learn();
//			learner.saveModel("classifier1.dat");
////		}
//	}
}	