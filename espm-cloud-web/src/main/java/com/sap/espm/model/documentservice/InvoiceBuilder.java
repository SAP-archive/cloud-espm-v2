package com.sap.espm.model.documentservice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.espm.model.SalesOrderHeader;
import com.sap.espm.model.SalesOrderItem;
import com.sap.espm.model.exception.CMISConnectionException;
import com.sap.espm.model.exception.ReportGenerationException;

/**
 * This class is used to generate an Invoice based on a Customer's Order(s). The
 * Order related information are obtained via a {@link SalesOrderItem} object.
 * 
 */
public class InvoiceBuilder {

	/**
	 * The {@link Logger} instance used for logging.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceBuilder.class);

	/**
	 * This method will be used to generate a sales order based on the input
	 * {@link SalesOrderItem} entity object passed as input.
	 * <p>
	 * Based on the input {@link SalesOrderItem}, the Customer details are
	 * obtained from the {@link SalesOrderHeader} and the product details of the
	 * order are obtained from the {@link SalesOrderItem}.
	 * <p>
	 * Using the above mentioned data, the content is populated in a
	 * {@link PDPageContentStream} object. This {@link PDPageContentStream} is
	 * then added to a {@link PDDocument}. This {@link PDDocument} represents
	 * the PDF document that will be stored in the document repository.
	 * 
	 * @param salesOrderItemList
	 *            - The input list of {@link SalesOrderItem}.
	 * @return - if the document generation and upload is successful, this
	 *         string variable will hold the location of the document on the
	 *         document storage.
	 */
	public String generateInvoice(List<SalesOrderItem> salesOrderItemList) {

		Map<String, String> customerMap = null;
		String reportPath = null;

		/*
		 * Flow/Algo; 1) Get the Customer Map from the SalesOrderHeader (fetched
		 * from SalesOrderHeader) 2) Get the Product Map from the SalesOrderItem
		 * 3) Add the Map data in the PDF Stream.
		 */

		if (salesOrderItemList != null && salesOrderItemList.size() >= 1) {

			try {

				// Create the PDF document content
				// access the root folder of the repository
				Folder root = CMISSessionHelper.getInstance().getSession().getRootFolder();
				PDDocument invoiceDocument = new PDDocument();
				PDPage page = new PDPage();
				invoiceDocument.addPage(page);
				PDPageContentStream contentStream = null;

				contentStream = new PDPageContentStream(invoiceDocument, page);
				contentStream.beginText();

				PDFont font = PDType1Font.TIMES_ROMAN;
				contentStream.setFont(font, 10);
				contentStream.moveTextPositionByAmount(100, 500);

				// 1. Get the Customer Map Details.
				customerMap = salesOrderItemList.get(0).getSalesOrderHeader().getCustomer().getCustomerReportData();

				for (Entry<String, String> entry : customerMap.entrySet()) {
					String customerField = entry.getKey();
					String customerValue = entry.getValue();
					contentStream.drawString(customerField + " : " + customerValue);
					contentStream.moveTextPositionByAmount(0, 10);

				}

				// 2. Generate the Sales Order Data.

				for (SalesOrderItem salesOrderItem : salesOrderItemList) {
					Map<String, String> productMap = salesOrderItem.getProduct().getProductReportMap();

					// 3. Write each map details in the Stream.
					for (Entry<String, String> product : productMap.entrySet()) {
						String productField = product.getKey();
						String productValue = product.getValue();

						contentStream.drawString(productField + " : " + productValue);
						contentStream.moveTextPositionByAmount(0, 10);

					}

				}

				// 4. Close the stream
				contentStream.endText();
				contentStream.close();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				invoiceDocument.save(out);
				invoiceDocument.close();

				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");

				// PDF file name
				String fileName = "Invoice" + salesOrderItemList.get(0).getSalesOrderId() + System.currentTimeMillis()
						+ ".pdf";
				properties.put(PropertyIds.NAME, fileName);
				byte[] encoded = out.toByteArray();
				InputStream inputStream = new ByteArrayInputStream(encoded);
				ContentStream contentStream1 = CMISSessionHelper.getInstance().getSession().getObjectFactory()
						.createContentStream(fileName, -1, "application/pdf;charset=UTF-8", inputStream);

				Document cmisDocument = root.createDocument(properties, contentStream1, VersioningState.NONE);
				inputStream.close();
				out.close();
				reportPath = cmisDocument.getId();

			} catch (IOException | CMISConnectionException e) {
				LOGGER.error(e.getMessage());
				throw new ReportGenerationException(e);
			}

		}
		return reportPath;
	}

}
