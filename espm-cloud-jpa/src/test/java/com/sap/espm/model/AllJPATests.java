package com.sap.espm.model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CustomerTest.class, ProductTest.class,
		SalesOrderHeaderTest.class, SalesOrderItemTest.class, 
		ProductCategoryTest.class, CustomerReviewTest.class, SupplierTest.class, StockTest.class })
public class AllJPATests {

}
