package com.sap.espm.model.web;

import javax.persistence.EntityManagerFactory;

import org.apache.olingo.odata2.core.exception.ODataRuntimeException;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAServiceFactory;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPARuntimeException;

/**
 * Odata JPA Processor implementation class. This is required for the
 * configuration of OData via the Olingo Framework.
 * <p>
 * For more information regarding the Olingo framework configuration steps,
 * refer to the documentation: https://olingo.apache.org/doc/odata4/index.html
 */
public class EspmServiceFactory extends ODataJPAServiceFactory {

	/**
	 * The package that contains all the model classes.
	 */
	private static final String PERSISTENCE_UNIT_NAME = "com.sap.espm.model";

	@Override
	public ODataJPAContext initializeODataJPAContext()
			throws ODataJPARuntimeException {
	//to-do - replace this with your code implementation

	}
}
