sap.ui.define([
		"sap/ui/test/Opa5",
		"sap/ui/test/matchers/AggregationLengthEquals",
		"sap/ui/test/matchers/AggregationFilled",
		"sap/ui/test/matchers/PropertyStrictEquals"
	],
	function(Opa5) {
		"use strict";

		var sViewName = "com.sap.espm.retailer.view.SalesOrder";
		Opa5.createPageObjects({
			onSales: {
				actions: {
					
					iClickSalesOrderListItem: function() {
						return this.waitFor({
							id: "list",
							//controlType: "sap.m.List",
							viewName: sViewName,
							matchers: new sap.ui.test.matchers.AggregationFilled({
								name: "items"
									
									}),	
									
							success: function(oPL) {
								oPL.getItems()[0].fireEvent("press");
								Opa5.assert.ok(oPL, "Found the sales order list");
							},
							errorMessage: "Did not find the sales products list"
						});
					},
					
					iPressApproveButton: function() {
						return this.waitFor({
							id: "approveButtonId",
							viewName: sViewName,
							success: function(aButtons) {
								aButtons.$().trigger("tap");
							},
							errorMessage: "Did not find the approve button."
						});
					},
					iPressApproveDialogOkButton: function(){
						var oOrderNowButton = null;
		                this.waitFor({
		                    searchOpenDialogs : true,
		                    controlType : "sap.m.Button",
		                    check : function (aButtons) {
		                        return aButtons.filter(function (oButton) {
		                            if(oButton.getText() !== "OK") {
		                                return false;
		                            }

		                            oOrderNowButton = oButton;
		                            return true;
		                        });
		                    },
		                    success : function (button) {
		                        oOrderNowButton.$().trigger("tap");
		                        Opa5.assert.ok(button,"Sales order has been approved");
		                    },
		                    errorMessage : "Did not find the OK button"
		                });
		                return this;
					}
				},

				assertions: {
					
					iSeeSalesOrderListView: function() {
						return this.waitFor({
							id: "list",
							//controlType: "sap.m.List",
							viewName: sViewName, 
							matchers: new sap.ui.test.matchers.AggregationFilled({
								name: "items"
									
									}),	
									
							success: function(oPL) {
								Opa5.assert.ok(oPL, "Found the sales order list");
							},
							errorMessage: "Did not find the sales products list"
						});
					}

				}

			}
		});
	}
);