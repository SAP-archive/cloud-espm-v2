sap.ui.define(
	[],
	function() {
		"use strict";

		QUnit.module("");

		
		opaTest("Home page with Tile container", function(Given, When, Then) {
			
			Given.iStartMyAppInAFrame("../../index.html");
			//When.onHome.iWaitUntilTheBusyIndicatorIsGone();
			Then.onHome.iSeeTileContainer();
			
		});
		
		opaTest("Choose stock information tile container", function(Given, When, Then) {
			
		 	 //Actions
			 When.onHome.iClickStockInformationTile();
			 //Assertions
			 Then.onStock.iSeeStockProductListView();
			
		});
		
		opaTest("Update stock information for a single product", function(Given, When, Then) {
			
		 	 //Actions
			 When.onStock.iClickonStockUpdateButton();
			 When.onStock.iEnterStockValue();
			 When.onStock.iClickonSubmitButton();
			 //Assertions
			 Then.onStock.iSeeStockProductListView();
			 Then.onStock.iGoBack();
			
		});
		
		opaTest("Choose sales order tile container", function(Given, When, Then) {
			
		 	 //Actions
			 When.onHome.iClickSalesOrderTile();
			 //Assertions
			 Then.onSales.iSeeSalesOrderListView();
			
		});
		
		opaTest("Approve sales order", function(Given, When, Then) {
			
		 	 //Actions
			 When.onSales.iClickSalesOrderListItem();
			 When.onSales.iPressApproveButton();
			 When.onSales.iPressApproveDialogOkButton();
			 When.onSales.iClickSalesOrderListItem();
			 //Assertions
			 Then.onSales.iSeeSalesOrderListView();
			
		});
		
	}
);