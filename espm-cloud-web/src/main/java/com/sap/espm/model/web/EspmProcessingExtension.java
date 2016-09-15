package com.sap.espm.model.web;

import org.apache.olingo.odata2.jpa.processor.api.model.JPAEdmExtension;
import org.apache.olingo.odata2.jpa.processor.api.model.JPAEdmSchemaView;
import java.io.InputStream;
import com.sap.espm.model.function.impl.CustomerProcessor;
import com.sap.espm.model.function.impl.SalesOrderProcessor;
import com.sap.espm.model.function.impl.CustomerReviewProcessor;

/**
 * 
 * Class for registering function imports.
 * 
 */
public class EspmProcessingExtension implements JPAEdmExtension {

	/**
	 * Register function imports.
	 */

	@Override
	public void extendWithOperation(JPAEdmSchemaView view) {
		view.registerOperations(CustomerReviewProcessor.class, null);
		view.registerOperations(SalesOrderProcessor.class, null);
		//to-do replace this with your code implementation

	}

	@Override
	public void extendJPAEdmSchema(JPAEdmSchemaView arg0) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public InputStream getJPAEdmMappingModelStream(){
		// TODO Auto-generated method stub
		return null;
	}

}
