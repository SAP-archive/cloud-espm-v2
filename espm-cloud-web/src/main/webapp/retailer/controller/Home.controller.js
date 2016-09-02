sap.ui.define([
	"sap/ui/core/mvc/Controller",
	'sap/ui/model/json/JSONModel'
], function(Controller, JSONModel) {
	"use strict";

	return Controller.extend("com.sap.espm.retailer.controller.Home", {
		
		onInit : function () {
			// set mock model
			
			var oModel = new JSONModel("/espm-cloud-web/retailer/model/tileData.json");
			this.getView().setModel(oModel);
		},
		
		approveTilePressed: function(){
			
			var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
			oRouter.navTo("SalesOrder");
		},
		stockTilePressed: function(){
			
			var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
			oRouter.navTo("StockInformation");
		}

	});

});