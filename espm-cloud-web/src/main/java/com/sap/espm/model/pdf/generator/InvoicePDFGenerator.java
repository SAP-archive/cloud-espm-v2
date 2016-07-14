package com.sap.espm.model.pdf.generator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.sap.ecm.api.EcmService;
import com.sap.ecm.api.RepositoryOptions;
import com.sap.ecm.api.RepositoryOptions.Visibility;
import com.sap.espm.model.SalesOrderItem;
import com.sap.espm.model.builder.GenericInvoiceBuilder;
import com.sap.espm.model.builder.ReportBuilder;
import com.sap.espm.model.builder.pdf.CustomerSalesOrderBuilder;
import com.sap.espm.model.builder.pdf.ProductSalesOrderBuilder;
import com.sap.espm.model.builder.pdf.SalesOrderInvoiceBuilder;
import com.sap.espm.model.util.ReadProperties;

/**
 * Sales Order generator in PDF format
 *
 */
public class InvoicePDFGenerator {

	GenericInvoiceBuilder<SalesOrderItem, StringBuffer> invoiceBuilder;

	public InvoicePDFGenerator() {
		List<ReportBuilder<SalesOrderItem, StringBuffer>> builderList = new ArrayList<>(
				2);
		builderList.add(new CustomerSalesOrderBuilder());
		builderList.add(new ProductSalesOrderBuilder());
		invoiceBuilder = new SalesOrderInvoiceBuilder(builderList);

	}
	
	public String generateInvoicePdf(List<SalesOrderItem> soiList) {
		String reportPath = "";
		EcmService ecmSvc = null;
		String repositoryUniqueName="";
	    String repositorySecretKey="";
	    Session openCmisSession = null;
		InitialContext ctx;
		try {
			ctx = new InitialContext();
			String lookupName = "java:comp/env/" + "EcmService";
			ecmSvc = (EcmService) ctx.lookup(lookupName);
			repositoryUniqueName = ReadProperties.getInstance().getValue("uniqueName");
			repositorySecretKey=ReadProperties.getInstance().getValue("secretKey");
			openCmisSession = ecmSvc.connect(repositoryUniqueName, repositorySecretKey);
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (CmisObjectNotFoundException e) {
			// repository does not exist, so try to create it
			RepositoryOptions options = new RepositoryOptions();
			options.setUniqueName(repositoryUniqueName);
			options.setRepositoryKey(repositorySecretKey);
			options.setVisibility(Visibility.PROTECTED);
			ecmSvc.createRepository(options);
			// should be created now, so connect to it
			openCmisSession = ecmSvc.connect(repositoryUniqueName, repositorySecretKey);
		}

		// access the root folder of the repository
		Folder root = openCmisSession.getRootFolder();
		PDDocument invoiceDocument = new PDDocument();
		PDPage page = new PDPage();
		invoiceDocument.addPage(page);
		PDPageContentStream contentStream = null;

		try {
			contentStream = new PDPageContentStream(invoiceDocument, page);
			contentStream.beginText();
			StringBuffer reportContent = invoiceBuilder
					.generateInvoice(soiList);
			
			PDFont font = PDType1Font.TIMES_ROMAN;
			contentStream.setFont(font, 10);
			contentStream.moveTextPositionByAmount(100, 500);
			String reportData = reportContent.toString();
			String[] reportArray = reportData.split("\\|");
			
			for(int i=reportArray.length-1;i>=0;i--)
			{
				contentStream.drawString(reportArray[i]);
				contentStream.moveTextPositionByAmount(0, 10);
				
			}
			contentStream.endText();
			contentStream.close();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			invoiceDocument.save(out);
			invoiceDocument.close();
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
			SecureRandom random = new SecureRandom();
			long n = random.nextLong();
			String fileName = "Invoice" + n + ".pdf";
			properties.put(PropertyIds.NAME, fileName);
			byte[] encoded = out.toByteArray();
			InputStream inputStream = new ByteArrayInputStream(encoded);
			ContentStream contentStream1 = openCmisSession.getObjectFactory()
					.createContentStream(fileName,-1 ,
							"application/pdf;charset=UTF-8", inputStream);
			
			Document cmisDocument=root.createDocument(properties, contentStream1,
					VersioningState.NONE);
			inputStream.close();
			out.close();
			reportPath=cmisDocument.getId();
			
		}catch (IOException | COSVisitorException e) {
			e.printStackTrace();
		}

		return reportPath;
	}
	
}
