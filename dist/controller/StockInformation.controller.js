sap.ui.define([
	"sap/ui/core/mvc/Controller",
	"com/sap/espm/retailer/model/formatter"
	
], function(Controller,formatter) {
	"use strict";

	var bindingObject;
	
	return Controller.extend("com.sap.espm.retailer.controller.StockInformation", {

		formatter: formatter,
		/**
		 * Called when a controller is instantiated and its View controls (if available) are already created.
		 * Can be used to modify the View before it is displayed, to bind event handlers and do other one-time initialization.
		 * @memberOf com.sap.espm.retailer.view.StockInformation
		 */
			onInit: function() {
				
			},

		/**
		 * Similar to onAfterRendering, but this hook is invoked before the controller's View is re-rendered
		 * (NOT before the first rendering! onInit() is used for that one!).
		 * @memberOf com.sap.espm.retailer.view.StockInformation
		 */
		//	onBeforeRendering: function() {
		//
		//	},

		/**
		 * Called when the View has been rendered (so its HTML is part of the document). Post-rendering manipulations of the HTML could be done here.
		 * This hook is the same one that SAPUI5 controls get after being rendered.
		 * @memberOf com.sap.espm.retailer.view.StockInformation
		 */
		//	onAfterRendering: function() {
		//
		//	},

		/**
		 * Called when the Controller is destroyed. Use this one to free resources and finalize activities.
		 * @memberOf com.sap.espm.retailer.view.StockInformation
		 */
		//	onExit: function() {
		//
		//	}
		
		_initializeSupplierCard: function() {
			this._oSupplierCard = sap.ui.xmlfragment("com.sap.espm.retailer.fragment.SupplierCard",this.getView());
			this._oSupplierCard.bindElement({
				path: bindingObject + "/Product/Supplier"
			});
			this.getView().addDependent(this._oSupplierCard);
		},
		onLineItemPressed: function(event){
			
			bindingObject = event.getSource().getBindingContextPath();
			bindingObject = "espmRetailerModel>"+bindingObject;
	        var oLink = event.getSource();
			if (!this._oSupplierCard) {
				this._initializeSupplierCard();
			}
			else{
				this._oSupplierCard.bindElement({
				path: bindingObject + "/Product/Supplier"
				
			});
			
			}
			this._oSupplierCard.openBy(oLink.getCells()[3]);
			
		},
		
		onNavBack: function(){
			window.history.go(-1);
		},
		
		updateStock: function(event){
			var bundle = this.getView().getModel("i18n").getResourceBundle(); 			
			var updatedStockValue;
			var getBindingPath = event.getSource().getParent().getBindingContextPath();
			var stockString = event.getSource().getText();
			stockString = stockString.split("/");
			
			var that = this;
			var dialog = new sap.m.Dialog({
				id:"stockDialogId",
				title: bundle.getText("stock.minQuantity"),
				type: 'Message',
				content: [
					new sap.ui.layout.form.SimpleForm({
						id:"stockFormId",
						content:[
						        new sap.m.Label({text:bundle.getText("stock.minLevel")}),
								new sap.m.Input({
									id:"stockInputId",
									value:stockString[1],
									liveChange:function(oEvent){
										updatedStockValue = oEvent.getSource().getValue();
									}
								}),
								new sap.m.Label({text:bundle.getText("stock.itemsInStock")}),
								new sap.m.Input({value:stockString[0],editable: false})
						         
						      ]
					})
				],
				beginButton: new sap.m.Button({
					text: bundle.getText("stock.submit"),
					press: function () {
						
						var oEntry = {};
						if(updatedStockValue){
							oEntry.MinStock = updatedStockValue;
						}
						else{
							oEntry.MinStock = stockString[1];
						}
						
						var oDataModel = that.getView().getModel("espmRetailerModel");
						
						oDataModel.setHeaders({  
			            "Content-Type": "application/json",
			            "Accept": "application/json"
			        	}); 
						
						oDataModel.update(getBindingPath, oEntry, null, function(){
						 		sap.m.MessageToast.show(bundle.getText("stock.stockInformationUpdated"));
						 	},function(){
						 		sap.m.MessageToast.show(bundle.getText("stock.stockUpdateFailed"));});
						dialog.close();
					}
				}),
				endButton: new sap.m.Button({
					text: bundle.getText("stock.cancel"),
					press: function () {
						dialog.close();
					}
				}),
				afterClose: function() {
					dialog.destroy();
				}
			});
 
			dialog.open();
		
		}

	});

});