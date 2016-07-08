package com.sap.espm.model.web;

import javax.persistence.EntityManagerFactory;

import org.apache.olingo.odata2.core.exception.ODataRuntimeException;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAServiceFactory;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPARuntimeException;

/**
 * Odata JPA Processor implementation class
 */
public class EspmServiceFactory extends ODataJPAServiceFactory {

	private static final String PERSISTENCE_UNIT_NAME = "com.sap.espm.model";

	@Override
	public ODataJPAContext initializeODataJPAContext()
			throws ODataJPARuntimeException {
		ODataJPAContext oDataJPAContext = this.getODataJPAContext();
		EntityManagerFactory emf;
		try {
			emf = JpaEntityManagerFactory.getEntityManagerFactory();
			oDataJPAContext.setEntityManagerFactory(emf);
			oDataJPAContext.setPersistenceUnitName(PERSISTENCE_UNIT_NAME);
			oDataJPAContext.setJPAEdmExtension(new EspmProcessingExtension());
			oDataJPAContext.setJPAEdmMappingModel("EspmEdmMapping.xml");
			return oDataJPAContext;
		} catch (Exception e) {
			throw new ODataRuntimeException(e);
		}

	}
}
