package com.sap.espm.model.web.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.espm.model.CustomerReview;

/**
 * StAX Parser Implementation
 * 
 */
public class XMLParser {
	static final String ENTRY = "entry";
	static final String CUSTOMER_REVIEW_ID = "CustomerReviewId";
	static final String COMMENT = "Comment";
	static final String RATING = "Rating";
	static final String PRODUCT_ID = "ProductId";
	static final String DATE_OF_CREATION = "CreationDate";
	static final String FIRST_NAME = "FirstName";
	static final String LAST_NAME = "LastName";
	static Logger logger = LoggerFactory.getLogger(XMLParser.class);

	private InputStream in = null;
	private XMLEventReader eventReader = null;
	protected Boolean status;

	/**
	 * Parse Customers and fill List
	 * 
	 * @param customerReview
	 *            : A customer review
	 * @return Parsed List of Customers
	 */
	public List<CustomerReview> readCustomerReview(String customerReview) {
		List<CustomerReview> customerreviews = new ArrayList<CustomerReview>();
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			in = new ByteArrayInputStream(customerReview.getBytes());
			eventReader = inputFactory.createXMLEventReader(in);
			CustomerReview custreview = null;
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					if (startElement.getName().getLocalPart() == (ENTRY)) {
						custreview = new CustomerReview();
					}

					if (event.asStartElement().getName().getLocalPart()
							.equals(LAST_NAME) && custreview!=null) {
						event = eventReader.nextEvent();
						custreview.setLastName(getEvent(event));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(CUSTOMER_REVIEW_ID) && custreview!=null) {
						event = eventReader.nextEvent();
						custreview.setCustomerReviewId(getEvent(event));
						continue;
					}

					if (event.asStartElement().getName().getLocalPart()
							.equals(RATING) && custreview!=null) {
						event = eventReader.nextEvent();
						custreview.setRating(Integer.parseInt(getEvent(event)));
						continue;
					}

					if (event.asStartElement().getName().getLocalPart()
							.equals(FIRST_NAME) && custreview!=null) {
						event = eventReader.nextEvent();
						custreview.setFirstName(getEvent(event));
						continue;
					}

					if (event.asStartElement().getName().getLocalPart()
							.equals(COMMENT) && custreview!=null) {

						event = eventReader.nextEvent();
						custreview.setComment(getEvent(event));
						continue;

					}

					if (event.asStartElement().getName().getLocalPart()
							.equals(PRODUCT_ID) && custreview!=null) {
						event = eventReader.nextEvent();
						custreview.setProductId(getEvent(event));
						continue;
					}

				}

				if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();
					if (endElement.getName().getLocalPart() == (ENTRY)) {
						customerreviews.add(custreview);
					}
				}
			}

		} catch (XMLStreamException e) {
			logger.error("Exception occured", e);
			status = false;
		} finally {
			try {
				in.close();
				eventReader.close();
			} catch (XMLStreamException e) {
				logger.error("XMLStream exception occured", e);
				status = false;
			} catch (IOException e) {
				logger.error("XMLStream exception occured", e);
				status = false;
			}
		}

		return customerreviews;
	}

	public String getEvent(XMLEvent event) {
		if (event.isCharacters()) {
			return event.asCharacters().getData();
		} else {
			return null;
		}
	}

}
