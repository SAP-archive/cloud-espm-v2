sap.ui.define(
	[],
	function() {
		"use strict";

		QUnit.module("");

		
		opaTest("Home page with products List", function(Given, When, Then) {
			
			Given.iStartMyAppInAFrame("../../index.html");
			//When.onHome.iWaitUntilTheBusyIndicatorIsGone();
			Then.onHome.iSeeProductListView();
			
		});
		
		opaTest("Search for the product in the list items", function(Given, When, Then) {
			
		 	 //Actions
			 When.onHome.iGetTitleOfFirstItem();
			 When.onHome.iSearchForTheFirstObject();
			 //Assertions
			 Then.onHome.iCheckIfItemSearchedIsMatchingOnMasterList();
			
		});
		
		opaTest("Select the first item in the list", function(Given, When, Then) {
			
		 	 //Actions
			 When.onHome.iSelectItemFromTheProductList();
			 //Assertions
			 Then.onDetail.iCheckIfItemGotSelected();
			
		});
		
		opaTest("Add product to the cart and navigate to cart", function(Given, When, Then) {
			
		 	 //Actions
			 When.onDetail.iPressAddToCartButton();
			 //Assertions
			 Then.onDetail.iCheckIfItemGotAddedToCart();
			
		});
		
		opaTest("update quantity in the cart page", function(Given, When, Then) {
			
		 	 //Actions
			 When.onCart.iChangeQuantityinCart();
			 //Assertions
			 Then.onCart.iCheckIfQunatityUpdated();
			 
			
		});
		
		opaTest("Navigate to checkout page", function(Given, When, Then) {
			
		 	 //Actions
			 When.onCart.iPressOnCheckoutButton();
			 //Assertions
			 Then.onCheckout.iSeeCheckoutView();
			 
			
		});
		
		opaTest("Fill new customer personal details", function(Given, When, Then) {
			
		 	 //Actions
			 When.onCheckout.iClickOnWizardStep2();
			 When.onCheckout.iChooseNewCustomerRadiotButton();
			 When.onCheckout.iFillNewCustomerDetails();
			 //Assertions
			 Then.onCheckout.iSeeCheckoutView();
			 
			
		});
		
		opaTest("Fill new customer card details", function(Given, When, Then) {
			
		 	 //Actions
			 When.onCheckout.iClickOnWizardStep3();
			 When.onCheckout.iFillNewCustomerCardDetails();
			 When.onCheckout.iClickOnWizardReview();
			 //Assertions
			 Then.onCheckout.iSeeCheckoutView();
			 
			
		});
		
		opaTest("Place an order", function(Given, When, Then) {
			
		 	 //Actions
			 When.onCheckout.iPressOnOrderButton();
			 //Assertions
			 Then.onCheckout.iSeeCheckoutView();
			 
			
		});
		
		
	
	}
);