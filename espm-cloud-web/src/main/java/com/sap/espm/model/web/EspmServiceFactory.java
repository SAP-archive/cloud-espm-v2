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
