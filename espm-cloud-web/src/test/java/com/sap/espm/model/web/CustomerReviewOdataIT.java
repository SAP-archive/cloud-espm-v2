package com.sap.espm.model.web;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.sap.espm.model.CustomerReview;
import com.sap.espm.model.web.util.HttpResponse;
import com.sap.espm.model.web.util.RequestExecutionHelper;
import com.sap.espm.model.web.util.StreamHelper;
import com.sap.espm.model.web.util.XMLParser;

/**
 * 
 * Customer Review OData test class
 * 
 */
public class CustomerReviewOdataIT {
	private static final String ENTITY_NAME = "CustomerReviews";
	private static final String FILENAME = "com/sap/espm/model/web/customerReview.xml";
	private XMLParser parser = new XMLParser();

	/**
	 * Test if preloaded Customer Reviews exist
	 * 
	 * @throws IOException
	 */
	@Test
	public void testReadPreloadedCustomerReviewExists() throws IOException {
		checkExistenceOfCustomerReviewViaREST("111");
	}

	private void checkExistenceOfCustomerReviewViaREST(final String id)
			throws IOException {
		HttpResponse resp = RequestExecutionHelper
				.executeGetRequest(ENTITY_NAME
						+ "?$filter=CustomerReviewId%20eq%20%27" + id + "%27");

		List<CustomerReview> custReviews = parser.readCustomerReview(resp
				.getBody());
		assertEquals(
				"Not exactly one customer review found with the id: " + id, 1,
				custReviews.size());
		assertEquals("Id of customer review found is unexpected: " + id, id,
				custReviews.get(0).getCustomerReviewId());
	}

	/**
	 * Test Creation of Customer via HTTP Post request.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCreateCustomerReviewAndAssertItExists() throws IOException {
		String customerReviewXml = StreamHelper.readFromFile(FILENAME);
		HttpResponse resp = RequestExecutionHelper.executePostRequest(
				ENTITY_NAME, customerReviewXml);

		String body = resp.getBody();
		List<CustomerReview> custReviews = parser.readCustomerReview(body);
		assertEquals(1, custReviews.size());
		CustomerReview customerReviewCreated = custReviews.get(0);
		String id = customerReviewCreated.getCustomerReviewId();

		checkExistenceOfCustomerReviewViaREST(id);
	}

}
