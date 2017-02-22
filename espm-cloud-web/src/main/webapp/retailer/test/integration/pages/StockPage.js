sap.ui.define([
		"sap/ui/test/Opa5",
		"sap/ui/test/matchers/AggregationLengthEquals",
		"sap/ui/test/matchers/AggregationFilled",
		"sap/ui/test/matchers/PropertyStrictEquals"
	],
	function(Opa5) {
		"use strict";

		var sViewName = "com.sap.espm.retailer.view.StockInformation";
		Opa5.createPageObjects({
			onStock: {
				actions: {
					
					iClickonStockUpdateButton: function() {
						return this.waitFor({
							id: "catalogTable",
							//controlType: "sap.m.Table",
							viewName: sViewName,
							matchers: new sap.ui.test.matchers.AggregationFilled({
								name: "items"
							}),
							success: function(oSCTab) {
								oSCTab.getItems()[0].getCells()[4].fireEvent("press");
								Opa5.assert.ok(oSCTab, "Fired an event for stock update button");
							},
							errorMessage: "Failed to find the table"
						});
					},
					iEnterStockValue: function(){
						var oOrderNowButton = null;
		                this.waitFor({
		                    searchOpenDialogs : true,
		                    controlType : "sap.m.Input",
		                    check : function (aInputs) {
		                    	aInputs[0].setValue("19");
		                    	aInputs[0].fireEvent("liveChange");
		                    	return true;
		                    },
		                    success : function (field) {
		                    	Opa5.assert.ok(field, "Updated the stock input field");
		                    },
		                    errorMessage : "Did not find the stock input field"
		                });
		                return this;
					},
					
					iClickonSubmitButton: function(){
						var oOrderNowButton = null;
		                this.waitFor({
		                    searchOpenDialogs : true,
		                    controlType : "sap.m.Button",
		                    check : function (aButtons) {
		                        return aButtons.filter(function (oButton) {
		                            if(oButton.getText() !== "Submit") {
		                                return false;
		                            }

		                            oOrderNowButton = oButton;
		                            return true;
		                        });
		                    },
		                    success : function (button) {
		                        oOrderNowButton.$().trigger("tap");
		                        Opa5.assert.ok(button,"Clicked on submit button");
		                    },
		                    errorMessage : "Did not find the submit button"
		                });
		                return this;
					}
					
				},

				assertions: {
					
					iGoBack: function() {
						return this.waitFor({
							//id: "catalogTable",
							controlType: "sap.m.Page",
							viewName: sViewName, 		
							success: function(oPL) {
								oPL[0]._navBtn.firePress();
								Opa5.assert.ok(oPL, "Go back to home page");
							},
							errorMessage: "Did not find the back button"
						});
					},
					
					iSeeStockProductListView: function() {
						return this.waitFor({
							//id: "catalogTable",
							controlType: "sap.m.Table",
							viewName: sViewName,
							matchers: new sap.ui.test.matchers.AggregationFilled({
								name: "items"
									
									}),	
									
							success: function(oPL) {
								Opa5.assert.ok(oPL, "Found the stock products list");
							},
							errorMessage: "Did not find the stock products list"
						});
					}

				}

			}
		});
	}
);