package com.sap.espm.model.pdf.generator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.data.ContentStream;

import com.sap.ecm.api.EcmService;
import com.sap.espm.model.util.ReadProperties;

public class CmisRead extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
  
    public CmisRead() {
        super();
       }
    InitialContext ctx;
    EcmService ecmSvc=null;
    Session openCmisSession=null;
    String repositoryUniqueName="";
    String repositorySecretKey="";
    
    public void init(ServletConfig config) throws ServletException {
      super.init(config);
     
    //TODO connect to the add logic to connect to the CMIS repository - code snippet 1
     }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final String objectId =  request.getParameter("objectId");
		//TODO Logic for reading the contents from the CMIS Repository - code snippet 2
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	private void ioCopy(InputStream in, OutputStream out) throws IOException {
		byte[] buf = new byte[1 << 13];
		int read;
		while ((read = in.read(buf)) >= 0)
			out.write(buf, 0, read);
	}
}
