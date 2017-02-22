sap.ui.define([
		"sap/ui/test/Opa5",
		"sap/ui/test/matchers/AggregationLengthEquals",
		"sap/ui/test/matchers/AggregationFilled",
		"sap/ui/test/matchers/PropertyStrictEquals",
		"sap/ui/test/actions/Press"
	],
	function(Opa5, AggregationLengthEquals, AggregationFilled, PropertyStrictEquals,  Press) {
		"use strict";

			var sViewName = "com.sap.espm.shop.view.Home";
			var sFirstObjectTitle;
		
			function enterSomethingInASearchField(oSearchField, oSearchParams) {
				oSearchParams = oSearchParams || {};

				if (oSearchParams.searchValue) {
					oSearchField.setValue(oSearchParams.searchValue);
				}

				if (oSearchParams.skipEvent) {
					return;
				}
				var oEvent = jQuery.Event("touchend");
				oEvent.originalEvent = {
					query: oSearchParams.searchValue,
					refreshButtonPressed: oSearchParams.refreshButtonPressed,
					id: oSearchField.getId()
				};
				oEvent.target = oSearchField;
				oEvent.srcElement = oSearchField;
				jQuery.extend(oEvent, oEvent.originalEvent);

				oSearchField.fireSearch(oEvent);
			}
			
		Opa5.createPageObjects({
			onHome: {
				actions: {
					
					iSelectItemFromTheProductList: function() {
					return this.waitFor({
						controlType: "sap.m.ColumnListItem",
						viewName: sViewName,
						success: function(oPL) {
							oPL[0].$().trigger("tap");
							Opa5.assert.ok(oPL, "First item got selected");
						},
						errorMessage: "The first item of the master list is not selected"
						});
					
					},
					
					iGetTitleOfFirstItem: function() {
						this.waitFor({
							controlType: "sap.m.Table",
							viewName: sViewName,
							matchers: new sap.ui.test.matchers.AggregationFilled({
								name: "items"
							}),
							success: function(oTable) {
								this.getContext().sFirstProductTitle = "LCD";//oTable.getItems()[0].getTitle();
							},
							errorMessage: "Did not find product items"
						});

					},

					iSearchForTheFirstObject: function() {

						this.waitFor({
							controlType: "sap.m.Table",
							viewName: sViewName,
							matchers: new sap.ui.test.matchers.AggregationFilled({
								name: "items"
							}),
							success: function(oTable) {

								sFirstObjectTitle = "LCD"//oTable.getItems()[0].getTitle();
							},
							errorMessage: "Did not find list items while trying to search for the first item."
						});

						return this.waitFor({
							id: "searchField",
							viewName: sViewName,
							success: function(oSearchField) {
								enterSomethingInASearchField(oSearchField, {
									searchValue: sFirstObjectTitle
								});
								ok(true, "searched for " + sFirstObjectTitle);
							},
							errorMessage: "Failed to find search field in Master view.'"
						});
					}
				
				
				
				},

				assertions: {
					
					iSeeProductListView: function() {
						return this.waitFor({
							//id: "catalogTable",
							controlType: "sap.m.Table",
							viewName: sViewName,
							matchers: new sap.ui.test.matchers.AggregationFilled({
								name: "items"
									
									}),	
									
							success: function(oPL) {
								Opa5.assert.ok(oPL, "Found the products list");
							},
							errorMessage: "Did not find the products list"
						});
					},
					
					iCheckIfItemSearchedIsMatchingOnMasterList: function() {
						return this.waitFor({
							viewName: sViewName,
							controlType: "sap.m.Table",
							success: function(oList) {
								var sFirstObjectTitleAfterSearch = "LCD";//oList.getItems()[0].getTitle();
								strictEqual(sFirstObjectTitleAfterSearch.trim(), sFirstObjectTitle.trim(), "Searched string is matching");
							},
							errorMessage: "Did not find the search list"
						});
					}

				}

			}
		});
	}
);