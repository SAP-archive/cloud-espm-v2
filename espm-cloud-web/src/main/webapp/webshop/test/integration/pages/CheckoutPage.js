sap.ui.define([
		"sap/ui/test/Opa5",
		"sap/ui/test/matchers/AggregationLengthEquals",
		"sap/ui/test/matchers/AggregationFilled",
		"sap/ui/test/matchers/PropertyStrictEquals"
	],
	function(Opa5) {
		"use strict";

		var sViewName = "com.sap.espm.shop.view.Checkout";
		var sFragmentReviewName = "com.sap.espm.shop.view.fragment.ReviewPage";
		
		Opa5.createPageObjects({
			onCheckout: {
				actions: {
					
					iClickOnWizardStep2: function() {
						return this.waitFor({
							id:"checkoutWizard",
							viewName: sViewName,
							success: function(wizard) {
								wizard._handleNextButtonPress();
							},
							errorMessage: "Did not find the wizard"
						});
					},
					
					iChooseNewCustomerRadiotButton: function() {
						return this.waitFor({
							id: "newCustomerId",
							viewName: sViewName,
							success: function(radioButton) {
								radioButton.$().trigger("tap");
							},
							errorMessage: "Did not find the add to cart button."
						});
					},
					
					iFillNewCustomerDetails: function() {
						return this.waitFor({
							id: "newFormId",
							viewName: sViewName,
							success: function(newForm) {
								newForm.getContent()[1].setValue("Karthik");
								newForm.getContent()[2].setValue("Ram");
								newForm.getContent()[4].setValue("ka142345@gmail.com");
								newForm.getContent()[6].setValue("Feb 1, 2000");
								newForm.getContent()[8].setValue("345");
								newForm.getContent()[9].setValue("Gotham");
								newForm.getContent()[10].setValue("7864");
								newForm.getContent()[11].setValue("IN");
								Opa5.assert.ok(newForm, "New customer details got filled");
								
							},
							errorMessage: "Did not find the Form."
						});
					},
					
					iClickOnWizardStep3: function() {
						return this.waitFor({
							id:"checkoutWizard",
							viewName: sViewName,
							success: function(wizard) {
								wizard._handleNextButtonPress();
							},
							errorMessage: "Did not find the wizard"
						});
					},
					
					iFillNewCustomerCardDetails: function() {
						return this.waitFor({
							id: "cardFormId",
							viewName: sViewName,
							success: function(newForm) {
								newForm.getContent()[2].setValue("Karthik Ram");
								newForm.getContent()[4].setValue("3452 2345 3212 1235");
								newForm.getContent()[6].setValue("4312");
								Opa5.assert.ok(newForm, "New customer card details got filled");
								
							},
							errorMessage: "Did not find the Form."
						});
					},
					iClickOnWizardReview: function() {
						return this.waitFor({
							id:"checkoutWizard",
							viewName: sViewName,
							success: function(wizard) {
								wizard._handleNextButtonPress();
							},
							errorMessage: "Did not find the wizard"
						});
					},
					iPressOnOrderButton: function() {
						return this.waitFor({
							id: "wizardNavContainer",
							viewName: sViewName,
							success: function(navContainer) {
								 navContainer.getCurrentPage().getFooter().getContentRight()[0].$().trigger("tap");
							},
							errorMessage: "Did not find the checkout button."
						});
					}
					
				},

				assertions: {
					
					iSeeCheckoutView: function() {
						return this.waitFor({
							controlType: "sap.m.Table",
							viewName: sViewName,
							matchers: new sap.ui.test.matchers.AggregationFilled({
								name: "items"
									
									}),	
									
							success: function(oPL) {
								Opa5.assert.ok(oPL, "Checkout page loaded");
							},
							errorMessage: "Failed to load the checkout page"
						});
					},
					iSeeSalesOrderMessageBox: function(){
						return this.waitFor({
							controlType: "sap.m.MessageBox",
							viewName: sViewName,
							success: function(messageBox) {
								Opa5.assert.ok(messageBox, "Sales order has been created");
							},
							errorMessage: "Sales order creation has been failed"
						});
					}

				}

			}
		});
	}
);