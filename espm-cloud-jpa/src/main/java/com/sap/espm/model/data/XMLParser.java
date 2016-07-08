package com.sap.espm.model.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.espm.model.Customer;
import com.sap.espm.model.CustomerReview;
import com.sap.espm.model.Product;
import com.sap.espm.model.ProductCategory;
import com.sap.espm.model.Supplier;

/**
 * Stax Parser Implementation
 * 
 */
public class XMLParser {
	// Product tags defined in Products.xml
	static final String PRODUCT = "product";
	static final String PRODUCT_ID = "product_id";
	static final String PRODUCT_TYPE_CODE = "type_code";
	static final String PRODUCT_CATEGORY_ID = "category_id";
	static final String PRODUCT_TAX_TARIF_CODE = "tax_tarif_code";
	static final String PRODUCT_MEASURE_UNIT = "measure_unit";
	static final String PRODUCT_WEIGHT_MEASURE = "weight_measure";
	static final String PRODUCT_WEIGHT_UNIT = "weight_unit";
	static final String PRODUCT_DESCRIPTION = "description";
	static final String PRODUCT_DESCRIPTION_LANGUAGE = "langu";
	static final String PRODUCT_DESCRIPTION_TEXT = "text";
	static final String PRODUCT_NAME = "prod_name";
	static final String PRODUCT_NAME_LANGUAGE = "langu";
	static final String PRODUCT_NAME_NAME = "name";
	static final String PRODUCT_PIC = "web_resource";
	static final String PRODUCT_PIC_URL = "web_adress";
	static final String PRODUCT_PIC_TYPE = "type";
	static final String PRODUCT_PIC_LANGUAGE = "langu";
	static final String PRODUCT_PIC_TEXT = "text";
	static final String PRODUCT_PRICE = "price";
	static final String PRODUCT_WIDTH = "width";
	static final String PRODUCT_DEPTH = "depth";
	static final String PRODUCT_HEIGHT = "height";
	static final String PRODUCT_UNIT = "unit";
	
	//CustomerReviews tags defined in CustomerReviews.xml
	static final String CUSTOMERREVIEW = "customerReview";
	static final String CUSTOMERREVIEW_COMMENT = "comment";
	static final String CUSTOMERREVIEW_CREATIONDATE = "creationdate";
	static final String CUSTOMERREVIEW_FIRSTNAME = "firstname";
	static final String CUSTOMERREVIEW_LASTNAME = "lastname";
	static final String CUSTOMERREVIEW_RATING = "rating";
	static final String CUSTOMERREVIEW_PRODUCT_ID = "product_id";
	
	// Business Partner tags defined in Business_Partners.xml
	static final String BUSINESS_PARTNER = "businessPartner";
	static final String BP_ID = "partner_id";
	static final String BP_CURRENCY_CODE = "currency";
	static final String BP_COMPANY_NAME = "company_name";
	static final String BP_LEGAL_FORM = "legal_form";
	static final String BP_FIRST_NAME = "firstname";
	static final String BP_LAST_NAME = "lastname";
	static final String BP_MIDDLE_NAME = "middlename";
	static final String BP_SEX = "sex";
	static final String BP_DATE_OF_BIRTH = "date_of_birth";
	static final String BP_ROLE = "role_code";
	static final String BP_ADDRESS = "address";
	static final String BP_CITY = "city";
	static final String BP_POSTAL_CODE = "postal_code";
	static final String BP_STREET = "street";
	static final String BP_BUILDING = "building";
	static final String BP_COUNTRY = "country";
	static final String BP_ADDRESS_TYPE = "type";
	static final String BP_VAL_DATE_START = "val_date_Start";
	static final String BP_VAL_DATE_END = "val_date_end";
	static final String BP_LANGUAGE = "langu";
	static final String BP_TEXT = "text";
	static final String BP_PHONE = "phone";
	static final String BP_PHONE_NUMBER = "phone_number";
	static final String BP_PHONE_EXT = "phone_extension";
	static final String BP_PHONE_VALIDITY_DATE = "validity_date";
	static final String BP_EMAIL = "email";
	static final String BP_EMAIL_ADDRESS = "email_address";
	static final String BP_EMAIL_VALIDITY_DATE = "validity_date";
	static final String BP_WEB = "webaddress";
	static final String BP_WEB_ADDRESS = "web_address";
	static final String BP_WEB_VALIDITY_DATE = "validity_date";
	static final String BP_SUPPLIER_NAME = "company_name";
	// Product Category tags defined inside Product_Category.xml
	static final String PC_PRODUCT_CATEGORY = "product_category";
	static final String PC_MAIN_CATEGORY = "main_category";
	static final String PC_CATEGORY = "category";
	// Purchase Order Status defined inside Purchase_Order_Status.xml
	static final String ST_PURCHASE_ORDER_STATUS = "purchase_order_status";
	static final String ST_STATUS = "status";
	static final String ST_LIFE_CYCLE_STATUS = "life_cycle_status";
	static final String ST_LIFE_CYCLE_STATUS_NAME = "life_cycle_status_name";
	static final String ST_LANGUAGE = "langu";

	static Logger logger = LoggerFactory.getLogger(XMLParser.class);

	private int desFlg = 0;
	private int prodNameFlg = 0;
	private int productPicFlg = 0;
	private InputStream in = null;
	private XMLEventReader eventReader = null;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	protected Boolean status;

	/**
	 * Parse Product XML and fill it in List
	 * 
	 * @param productXml
	 * @return Parsed List of Products
	 */
	public List<Product> readProduct(EntityManager em, String productXml,
			List<Supplier> suppliers) {
		List<Product> products = new ArrayList<Product>();
		int count = 0;
		Supplier sup = null;
		try {

			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			in = getResourceAsInputStream(productXml);
			eventReader = inputFactory.createXMLEventReader(in);
			Product product = null;
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					if (startElement.getName().getLocalPart() == (PRODUCT)) {
						product = new Product();
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(PRODUCT_ID)) {
						event = eventReader.nextEvent();
						product.setProductId(getEvent(event));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(PRODUCT_CATEGORY_ID)) {
						event = eventReader.nextEvent();
						String category = getEvent(event);
						product.setCategory(category);
						product.setCategoryName(category);
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(PRODUCT_MEASURE_UNIT)) {
						event = eventReader.nextEvent();
						product.setQuantityUnit(getEvent(event));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(PRODUCT_WEIGHT_MEASURE)) {
						event = eventReader.nextEvent();
						product.setWeight(BigDecimal.valueOf(Double
								.parseDouble(getEvent(event))));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(PRODUCT_WEIGHT_UNIT)) {
						event = eventReader.nextEvent();
						product.setWeightUnit(getEvent(event));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(PRODUCT_DESCRIPTION)) {
						desFlg = 1;
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(PRODUCT_DESCRIPTION_LANGUAGE)
							&& desFlg == 1) {
						event = eventReader.nextEvent();
						String language = event.asCharacters().getData();
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(PRODUCT_DESCRIPTION_TEXT)
							&& desFlg == 1) {
						event = eventReader.nextEvent();
						String text = event.asCharacters().getData();
						product.setShortDescription(text);
						product.setLongDescription(text);
						desFlg = 0;
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(PRODUCT_NAME)) {
						prodNameFlg = 1;
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(PRODUCT_NAME_LANGUAGE)
							&& prodNameFlg == 1) {
						event = eventReader.nextEvent();
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(PRODUCT_NAME_NAME)
							&& prodNameFlg == 1) {
						event = eventReader.nextEvent();
						product.setName(getEvent(event));
						prodNameFlg = 0;
						continue;
					}

					if (event.asStartElement().getName().getLocalPart()
							.equals(PRODUCT_PIC)) {
						productPicFlg = 1;
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(PRODUCT_PIC_URL)
							&& productPicFlg == 1) {
						event = eventReader.nextEvent();
						String picUrl = getEvent(event);
						if (picUrl == null) {
							continue;
						}
						product.setPictureUrl(new File(picUrl).getName());
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(PRODUCT_PRICE)) {
						event = eventReader.nextEvent();
						product.setPrice(BigDecimal.valueOf(Double
								.parseDouble(getEvent(event))));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(PRODUCT_WIDTH)) {
						event = eventReader.nextEvent();
						product.setDimensionWidth(BigDecimal.valueOf(Double
								.parseDouble(getEvent(event))));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(PRODUCT_DEPTH)) {
						event = eventReader.nextEvent();
						product.setDimensionDepth(BigDecimal.valueOf(Double
								.parseDouble(getEvent(event))));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(PRODUCT_HEIGHT)) {
						event = eventReader.nextEvent();
						product.setDimensionHeight(BigDecimal.valueOf(Double
								.parseDouble(getEvent(event))));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(PRODUCT_UNIT)) {
						event = eventReader.nextEvent();
						product.setDimensionUnit(getEvent(event));
						continue;
					}

				}

				// If we reach the end of an item element we add it to the list
				if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();
					if (endElement.getName().getLocalPart() == (PRODUCT)) {
						if (!(suppliers == null)) {
							product.setSupplierId(suppliers.get(count)
									.getSupplierId());

							sup = em.find(Supplier.class, suppliers.get(count)
									.getSupplierId());
							product.setSupplier(sup);

							count++;
							if (count == suppliers.size()) {
								count = 0;
							}
						}
						em.persist(product);
						products.add(product);
						desFlg = 0;
						prodNameFlg = 0;
						productPicFlg = 0;
					}
				}

			}
		} catch (Exception e) {
			logger.error("Exception occured", e);
			status = false;
		} finally {
			try {
				in.close();
				eventReader.close();
			} catch (IOException e) {
				logger.error("IO Exception occured", e);
				status = false;
			} catch (XMLStreamException e) {
				logger.error("XMLStream exception occured", e);
				status = false;
			}
		}
		return products;

	}

	/**
	 * Parse Product XML and fill it in List
	 * 
	 * @param reviewXml
	 * @return Parsed List of CustomerReviews
	 */
	public List<CustomerReview> readCustomerReview(EntityManager em, String reviewXml,
			List<Product> products) {
		List<CustomerReview> customerReviews = new ArrayList<CustomerReview>();
		Product prod = null;
		String product_Id = null;
		try {

			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			in = getResourceAsInputStream(reviewXml);
			eventReader = inputFactory.createXMLEventReader(in);
			CustomerReview customerReview = null;
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					if (startElement.getName().getLocalPart() == (CUSTOMERREVIEW)) {
						customerReview = new CustomerReview();
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(CUSTOMERREVIEW_COMMENT)) {
						event = eventReader.nextEvent();
						customerReview.setComment(getEvent(event));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(CUSTOMERREVIEW_CREATIONDATE)) {
						event = eventReader.nextEvent();
						String datetime = getEvent(event);
						Calendar cal = Calendar.getInstance();
						Date date = null;
						DateFormat formatter = new SimpleDateFormat("yyyymmdd");
						date = formatter.parse(datetime);
						cal.setTime(date);
						customerReview.setCreationDate(cal);
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(CUSTOMERREVIEW_FIRSTNAME)) {
						event = eventReader.nextEvent();
						customerReview.setFirstName(getEvent(event));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(CUSTOMERREVIEW_LASTNAME)) {
						event = eventReader.nextEvent();
						customerReview.setLastName(getEvent(event));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(CUSTOMERREVIEW_RATING)) {
						event = eventReader.nextEvent();
						customerReview.setRating(Integer.parseInt(getEvent(event)));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(CUSTOMERREVIEW_PRODUCT_ID)) {
						event = eventReader.nextEvent();
						product_Id = getEvent(event);
						customerReview.setProductId(product_Id);
						continue;
					}
				}

				// If we reach the end of an item element we add it to the list
				if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();
					if (endElement.getName().getLocalPart() == (CUSTOMERREVIEW)) {
						if (!(products == null)) {
							prod = em.find(Product.class, product_Id);
							customerReview.setProduct(prod);
						}
						em.persist(customerReview);
						prod.addReview(customerReview);
						customerReviews.add(customerReview);
					}
				}

			}
		} catch (Exception e) {
			logger.error("Exception occured", e);
			status = false;
		} finally {
			try {
				in.close();
				eventReader.close();
			} catch (IOException e) {
				logger.error("IO Exception occured", e);
				status = false;
			} catch (XMLStreamException e) {
				logger.error("XMLStream exception occured", e);
				status = false;
			}
		}
		return customerReviews;

	}

	/**
	 * Parse Customers and fill List
	 * 
	 * @param bpXml
	 * @return Parsed List of Customers
	 */
	public List<Customer> readCustomers(EntityManager em, String bpXml) {
		ArrayList<Customer> customers = new ArrayList<Customer>();
		String role = "01";
		Date date = null;
		DateFormat formatter = new SimpleDateFormat("yyyymmdd");
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			in = getResourceAsInputStream(bpXml);
			eventReader = inputFactory.createXMLEventReader(in);
			Customer cus = null;
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					if (startElement.getName().getLocalPart() == (BUSINESS_PARTNER)) {
						cus = new Customer();
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(BP_ID)) {

						/*
						 * Customer id will be generated automatically, just
						 * process the event
						 */
						event = eventReader.nextEvent();
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(BP_FIRST_NAME)) {
						event = eventReader.nextEvent();
						cus.setFirstName(getEvent(event));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(BP_LAST_NAME)) {
						event = eventReader.nextEvent();
						cus.setLastName(getEvent(event));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(BP_DATE_OF_BIRTH)) {
						event = eventReader.nextEvent();
						date = formatter.parse(getEvent(event).toString());
						Calendar cal = Calendar.getInstance();
						cal.setTime(date);
						cus.setDateOfBirth(cal);
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(BP_ROLE)) {
						event = eventReader.nextEvent();
						role = getEvent(event);
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(BP_ADDRESS)) {
						event = eventReader.nextEvent();
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(BP_CITY)) {
						event = eventReader.nextEvent();
						cus.setCity(getEvent(event));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(BP_POSTAL_CODE)) {
						event = eventReader.nextEvent();
						cus.setPostalCode(getEvent(event));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(BP_STREET)) {
						event = eventReader.nextEvent();
						cus.setStreet(getEvent(event));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(BP_BUILDING)) {
						event = eventReader.nextEvent();
						cus.setHouseNumber(getEvent(event));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(BP_COUNTRY)) {
						event = eventReader.nextEvent();
						cus.setCountry(getEvent(event));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(BP_PHONE_NUMBER)) {
						event = eventReader.nextEvent();
						cus.setPhoneNumber(getEvent(event));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(BP_EMAIL_ADDRESS)) {
						event = eventReader.nextEvent();
						cus.setEmailAddress(getEvent(event));
						continue;
					}
				}

				// If we reach the end of an item element we add it to the list
				if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();
					if (endElement.getName().getLocalPart() == (BUSINESS_PARTNER)) {
						// em.persist(address);

						if (Integer.parseInt(role) == 1) {
							em.persist(cus);
							customers.add(cus);
						}
					}
				}

			}
		} catch (Exception e) {
			logger.error("Exception occured", e);
			status = false;
		} finally {
			try {
				in.close();
				eventReader.close();
			} catch (IOException e) {
				logger.error("IO Exception occured", e);
				status = false;
			} catch (XMLStreamException e) {
				logger.error("XMLStream exception occured", e);
				status = false;
			}
		}

		return customers;

	}

	/**
	 * Parse Suppliers and fill List
	 * 
	 * @param bpXml
	 * @return Parsed List of Suppliers
	 */
	public List<Supplier> readSuppliers(EntityManager em, String bpXml) {
		ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
		String role = "02";
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			in = getResourceAsInputStream(bpXml);
			eventReader = inputFactory.createXMLEventReader(in);
			Supplier sup = null;
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					if (startElement.getName().getLocalPart() == (BUSINESS_PARTNER)) {
						sup = new Supplier();
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(BP_ID)) {

						/*
						 * Customer id will be generated automatically, just
						 * process the event
						 */
						event = eventReader.nextEvent();
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(BP_SUPPLIER_NAME)) {
						event = eventReader.nextEvent();
						sup.setSupplierName(getEvent(event));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(BP_ROLE)) {
						event = eventReader.nextEvent();
						role = getEvent(event);
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(BP_ADDRESS)) {
						event = eventReader.nextEvent();
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(BP_CITY)) {
						event = eventReader.nextEvent();
						sup.setCity(getEvent(event));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(BP_POSTAL_CODE)) {
						event = eventReader.nextEvent();
						sup.setPostalCode(getEvent(event));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(BP_STREET)) {
						event = eventReader.nextEvent();
						sup.setStreet(getEvent(event));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(BP_BUILDING)) {
						event = eventReader.nextEvent();
						sup.setHouseNumber(getEvent(event));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(BP_COUNTRY)) {
						event = eventReader.nextEvent();
						sup.setCountry(getEvent(event));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(BP_PHONE_NUMBER)) {
						event = eventReader.nextEvent();
						sup.setPhoneNumber(getEvent(event));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(BP_EMAIL_ADDRESS)) {
						event = eventReader.nextEvent();
						sup.setEmailAddress(getEvent(event));
						continue;
					}
				}

				// If we reach the end of an item element we add it to the list
				if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();
					if (endElement.getName().getLocalPart() == (BUSINESS_PARTNER)) {
						// em.persist(address);

						if (Integer.parseInt(role) == 2) {
							em.persist(sup);
							suppliers.add(sup);
						}
					}
				}

			}
		} catch (Exception e) {
			logger.error("Exception occured", e);
			status = false;
		} finally {
			try {
				in.close();
				eventReader.close();
			} catch (IOException e) {
				logger.error("IO Exception occured", e);
				status = false;
			} catch (XMLStreamException e) {
				logger.error("XMLStream exception occured", e);
				status = false;
			}
		}

		return suppliers;

	}

	/**
	 * Parse Product Categeory and fill List
	 * 
	 * @param pcXml
	 * @param productList
	 *            TODO
	 * 
	 * @return Parsed List of Product Categories
	 */
	public Boolean readProductCategory(EntityManager em, String pcXml,
			List<Product> products) {
		status = true;

		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			in = getResourceAsInputStream(pcXml);
			eventReader = inputFactory.createXMLEventReader(in);
			ProductCategory pc = null;

			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					if (startElement.getName().getLocalPart() == (PC_PRODUCT_CATEGORY)) {
						pc = new ProductCategory();

					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(PC_MAIN_CATEGORY)) {
						event = eventReader.nextEvent();
						String category = getEvent(event);
						pc.setMainCategory(category);
						pc.setMainCategoryName(category);
						continue;
					}
					if (event.asStartElement().getName().getLocalPart()
							.equals(PC_CATEGORY)) {
						event = eventReader.nextEvent();
						String category = getEvent(event);
						pc.setCategory(category);
						pc.setCategoryName(category);
						pc.setNumberOfProducts((products != null) ? getNumberofProducts(
								products, category) : 0);
						continue;
					}

				}
				// If we reach the end of an item element we add it to the
				// list
				if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();
					if (endElement.getName().getLocalPart() == (PC_PRODUCT_CATEGORY)) {
						em.persist(pc);
					}

				}
			}
		} catch (Exception e) {
			logger.error("Exception occured", e);
			status = false;
		} finally {
			try {
				in.close();
				eventReader.close();
			} catch (IOException e) {
				logger.error("IO Exception occured", e);
				status = false;
			} catch (XMLStreamException e) {
				logger.error("XMLStream exception occured", e);
				status = false;
			}
		}
		return status;

	}

	/**
	 * Check if node has text and return it else return null.
	 * 
	 * @param event
	 * @return text of the node
	 */
	public String getEvent(XMLEvent event) {
		if (event.isCharacters()) {
			return event.asCharacters().getData();
		} else {
			return null;
		}
	}

	/**
	 * Converts date from string format to java.util.Calendar format
	 * 
	 * @param date
	 *            in String format
	 * @return date in java.util.Calendar format
	 */
	Calendar getCalendar(String date) {
		Calendar cal = null;
		try {
			Date d = dateFormat.parse(date);
			cal = Calendar.getInstance();
			cal.setTime(d);
		} catch (ParseException e) {
			logger.error("ParseException occured", e);
		}
		return cal;
	}

	/**
	 * Find a resource file and convert a Resource File as input stream
	 * 
	 * @param xmlFile
	 *            Resource file which needs to be converted to input stream
	 * @return resource as input stream
	 */
	InputStream getResourceAsInputStream(String xmlFile) {
		return XMLParser.class.getClassLoader().getResourceAsStream(xmlFile);
	}

	private static long getNumberofProducts(List<Product> entity,
			String category) {
		long count = 0;

		for (int i = 0; i < entity.size(); i++) {
			if (entity.get(i).getCategory().equals(category)) {
				count++;
			}
		}
		return count;

	}
}
