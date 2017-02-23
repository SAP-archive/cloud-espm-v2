sap.ui.define([
		"sap/ui/test/Opa5",
		"sap/ui/test/matchers/AggregationLengthEquals",
		"sap/ui/test/matchers/AggregationFilled",
		"sap/ui/test/matchers/PropertyStrictEquals"
	],
	function(Opa5) {
		"use strict";

		var sViewName = "com.sap.espm.shop.view.ProductDetail";
		Opa5.createPageObjects({
			onDetail: {
				actions: {
					
					iPressAddToCartButton: function() {
						return this.waitFor({
							id: "btnAddToCart",
							viewName: sViewName,
							success: function(aButtons) {
								aButtons.$().trigger("tap");
							},
							errorMessage: "Did not find the add to cart button."
						});
					}
				},

				assertions: {
					
					iCheckIfItemGotSelected: function() {
						return this.waitFor({
							id: "productId",
							viewName: sViewName,		
							success: function(oPL) {
								Opa5.assert.ok(oPL, "Detail page successfully loaded");
							},
							errorMessage: "Did not find the detail page"
						});
					},
					iCheckIfItemGotAddedToCart: function() {
						return this.waitFor({
							id: "btnProductHeader",
							viewName: sViewName,
							matchers: new sap.ui.test.matchers.PropertyStrictEquals({
								name: "text",
								value: "1"
							}),
							success: function(cartButton) {
								
								cartButton.$().trigger("tap");
								Opa5.assert.ok(cartButton, "product added to cart and navigate to cart page");
							},
							errorMessage: "product added to cart failed"
						});
					}

				}

			}
		});
	}
);