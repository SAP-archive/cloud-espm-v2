package com.sap.espm.model.documentservice;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.ecm.api.EcmService;
import com.sap.ecm.api.RepositoryOptions;
import com.sap.ecm.api.RepositoryOptions.Visibility;
import com.sap.ecm.api.ServiceException;
import com.sap.espm.model.exception.CMISConnectionException;
import com.sap.espm.model.util.ReadProperties;

/**
 * This class is used to instantiate a {@link Session} that will be used to
 * connect to the document repository. Using this {@link Session} object we can
 * retrieve and store documents in the document storage.
 *
 */
public final class CMISSessionHelper {

	/**
	 * The {@link Logger} instance used for logging.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CMISSessionHelper.class);

	/**
	 * The {@link CMISSessionHelper} used for the Singleton.
	 */
	private static CMISSessionHelper helper;

	/**
	 * The static instance of the {@link Session} that will be used to connect
	 * and retrieve data from the Document repository.
	 */
	private static Session openCmisSession = null;

	/**
	 * Double checked locking for init of the {@link CMISSessionHelper}.
	 * 
	 * @return - The single instance of the {@link CMISSessionHelper}.
	 */
	public static CMISSessionHelper getInstance() {
		if (helper == null) {
			synchronized (CMISSessionHelper.class) {
				if (helper == null) {
					helper = new CMISSessionHelper();
				}
			}
		}
		return helper;
	}

	/**
	 * Public Constructor. This constructor will use the credentials to connect
	 * to the Document storage (located in the "config.properties" file in the
	 * resources folder). Once the connection is established, a check is
	 * performed to check whether the document repository exists or not, if it
	 * does not, a new document repository is created. This document repository
	 * is used to connect to store and retrieve the documents used in ESPM.
	 */
	private CMISSessionHelper() {

		String repositoryUniqueName = "";
		String repositorySecretKey = "";
		EcmService ecmSvc = null;
		try {

			InitialContext ctx = new InitialContext();
			String lookupName = "java:comp/env/" + "EcmService";
			ecmSvc = (EcmService) ctx.lookup(lookupName);
			repositoryUniqueName = ReadProperties.getInstance().getValue("uniqueName");
			repositorySecretKey = ReadProperties.getInstance().getValue("secretKey");
			openCmisSession = ecmSvc.connect(repositoryUniqueName, repositorySecretKey);

		} catch (NamingException | ExceptionInInitializerError namingException) {
			LOGGER.error(namingException.getMessage());
			throw new CMISConnectionException(namingException);
		} catch (ServiceException serviceException) {
			LOGGER.error(serviceException.getMessage());
			throw new CMISConnectionException(serviceException);
		} catch (CmisObjectNotFoundException e) {
			// Check if the repository exists, else create
			/*
			 * This catch block will always be executed the first time as the
			 * repository folder may not exists.
			 */
			RepositoryOptions options = new RepositoryOptions();
			options.setUniqueName(repositoryUniqueName);
			options.setRepositoryKey(repositorySecretKey);
			options.setVisibility(Visibility.PROTECTED);
			ecmSvc.createRepository(options);
			// should be created now, so connect to it
			openCmisSession = ecmSvc.connect(repositoryUniqueName, repositorySecretKey);
		} catch (Exception exception) {
			// Generic Exception Block, in the case of exception while
			// connection to a local Document Store.
			LOGGER.error(exception.getMessage());
			throw new CMISConnectionException(exception);
		}
	}

	/**
	 * This method is used to return the {@link Session} used to connect to the
	 * document repository. If connection to the document repository was not
	 * possible, then a <b> null </b> object is returned.
	 * 
	 * @return
	 */
	public Session getSession() {
		/*
		 * if (helper == null) { helper = new CMISSessionHelper(); }
		 */

		return openCmisSession;
	}

}
