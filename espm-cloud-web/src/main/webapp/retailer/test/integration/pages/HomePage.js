sap.ui.define([
		"sap/ui/test/Opa5",
		"sap/ui/test/matchers/AggregationLengthEquals",
		"sap/ui/test/matchers/AggregationFilled",
		"sap/ui/test/matchers/PropertyStrictEquals"
	],
	function(Opa5) {
		"use strict";

		var sViewName = "com.sap.espm.retailer.view.Home";
		Opa5.createPageObjects({
			onHome: {
				actions: {
					
					iClickStockInformationTile: function() {
						return this.waitFor({
							id: "container",
							viewName: sViewName,
							success: function(tile) {
								tile.getTiles()[1].$().trigger("tap");
							},
							errorMessage: "Not able to click on Tile button."
						});
					},
					iClickSalesOrderTile: function() {
						return this.waitFor({
							id: "container",
							viewName: sViewName,
							success: function(tile) {
								tile.getTiles()[0].$().trigger("tap");
							},
							errorMessage: "Not able to click on Tile button."
						});
					}
				},

				assertions: {
					
					iSeeTileContainer: function() {
						return this.waitFor({
							id: "container",
							viewName: sViewName,		
							success: function(oPL) {
								Opa5.assert.ok(oPL, "Tile container successfully loaded");
							},
							errorMessage: "Did not find the Home page"
						});
					}

				}

			}
		});
	}
);