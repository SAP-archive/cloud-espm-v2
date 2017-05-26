package com.sap.espm.model.pdf.generator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.espm.model.documentservice.CMISSessionHelper;

/**
 * This {@link HttpServlet} is used to download the Sales Order report for the
 * customer based on his/her sales order history.
 * <p>
 * For this {@link HttpServlet}, the init() method is used to fetch the
 * {@link Session} object that will be used to connect and download documents
 * from the document repository. The {@link Session} object is instantiated only
 * once via the {@link CMISSessionHelper} class.
 *
 */
public class CmisRead extends HttpServlet {
	
	/**
	 * {@link Logger} implementation.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CmisRead.class);

	/**
	 * Serial Id.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Public default constructor.
	 */
	public CmisRead() {
		super();
	}

	/**
	 * The {@link Session} used to connect to the document storage.
	 */
	private Session openCmisSession;

	/**
	 * This init method is used to initialize the connection of the
	 * {@link Session}. This is done via the {@link CMISSessionHelper} class.
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		try {
			openCmisSession = CMISSessionHelper.getInstance().getSession();

		} catch (Exception | NoClassDefFoundError e1) {
			/*
			 * We are catching NoClassDefFoundError as when we deploy on local
			 * Servlet containers, Instantiation of the Session object for CMIS
			 * fails.
			 */
			LOGGER.error(e1.getMessage());
		}
	}

	/**
	 * This is a custom implementation of the doGet method of the
	 * {@link HttpServlet}.
	 * <p>
	 * Here, we expect an "objectId" to be passed as input parameter. This
	 * objectId will be fetched to the {@link Session} to fetch the existing
	 * document(if present).
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		final String objectId = request.getParameter("objectId");
		try {
			if (openCmisSession != null) {
				Document doc = (Document) openCmisSession.getObject(objectId);
				ContentStream content = doc.getContentStream();
				String type = content.getMimeType();
				String name = content.getFileName();
				int length = (int) content.getLength();
				InputStream stream = content.getStream();
				response.setContentType(type);
				response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + name);
				response.setContentLength(length);
				ioCopy(stream, response.getOutputStream());
			} else {
				response.setStatus(501);
			}

		} catch (Exception exception) {
			LOGGER.error(exception.getMessage());
		}

	}

	/**
	 * Here we mainly call the doGet() method. No custom implementation.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Copy the contents of the stream returned from the Document Storage to the
	 * HTTP output stream.
	 * 
	 * @param in
	 *            - The input stream from the Document Storage returned via
	 *            CMIS.
	 * @param out
	 *            - The HTTP Output stream.
	 * @throws IOException
	 *             - The {@link IOException} in case of any exception while
	 *             processing the streams.
	 */
	private void ioCopy(InputStream in, OutputStream out) throws IOException {
		byte[] buf = new byte[1 << 13];
		int read;
		while ((read = in.read(buf)) >= 0)
			out.write(buf, 0, read);
	}
}
