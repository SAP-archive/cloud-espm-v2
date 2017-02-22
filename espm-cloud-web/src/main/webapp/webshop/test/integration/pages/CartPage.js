sap.ui.define([
		"sap/ui/test/Opa5",
		"sap/ui/test/matchers/AggregationLengthEquals",
		"sap/ui/test/matchers/AggregationFilled",
		"sap/ui/test/matchers/PropertyStrictEquals"
	],
	function(Opa5) {
		"use strict";
		var sViewName = "com.sap.espm.shop.view.Shoppingcart";
		
		Opa5.createPageObjects({
			onCart: {
				actions: {
					
					iChangeQuantityinCart: function() {
						// Change the quantity of the shopping item   
						return this.waitFor({
							id: "shoppingCartTable",
							//controlType: "sap.m.Table",
							viewName: sViewName,
							matchers: new sap.ui.test.matchers.AggregationFilled({
								name: "items"
							}),
							success: function(oSCTab) {
								oSCTab.getItems()[0].getCells()[2].setValue("2");
								oSCTab.getItems()[0].getCells()[2].fireEvent("change");
								Opa5.assert.ok(oSCTab, "Quantitiy of products changed");
							},
							errorMessage: "Failed to find the field"
						});
					},
					iPressOnCheckoutButton: function() {
						return this.waitFor({
							id: "btnCheckOut",
							viewName: sViewName,
							success: function(aButtons) {
								aButtons.$().trigger("tap");
							},
							errorMessage: "Did not find the checkout button."
						});
					}
				},

				assertions: {
					
					iCheckIfQunatityUpdated: function() {
						return this.waitFor({
							id: "btnShoppingCartHeader",
							viewName: sViewName,
							matchers: new sap.ui.test.matchers.PropertyStrictEquals({
								name: "text",
								value: "2"
							}),
							success: function(cartButton) {
								
								Opa5.assert.ok(cartButton, "Qunatity has been updated");
							},
							errorMessage: "Failed to update quantity"
						});
					}

				}

			}
		});
	}
);