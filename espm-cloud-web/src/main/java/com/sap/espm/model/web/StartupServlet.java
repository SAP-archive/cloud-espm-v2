package com.sap.espm.model.web;

import javax.persistence.EntityManagerFactory;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sap.espm.model.data.DataLoader;

/**
 * The Startup {@link Servlet} loads intitial data and provides some additional basic
 * services.
 */
public class StartupServlet extends HttpServlet {

	/**
	 * Serial Id.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Static {@link EntityManagerFactory} used to connect to the DataSource.
	 */
	private static EntityManagerFactory emf;

	/**
	 * {@link Logger} implementation.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(StartupServlet.class);

	/**
	 * Retrieves the JPA entity manager factory and loads initial data into the
	 * ESPM model tables.
	 */
	@Override
	public void init() throws ServletException {
		try {
			emf = JpaEntityManagerFactory.getEntityManagerFactory();
			DataLoader loader = new DataLoader(emf);
			loader.loadData();
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	/*
	 * Closes the JPA entity manager factory.
	 */
	@Override
	public void destroy() {
		emf.close();
	}

}
