sap.ui.define([
	"sap/ui/core/mvc/Controller",
	"sap/m/Dialog",
	"sap/m/Button",
	"sap/m/Text",
	"com/sap/espm/shop/model/formatter",
	"sap/ui/core/UIComponent",
	"sap/ui/core/mvc/ViewType",
	"sap/m/MessageToast",
	"sap/ui/model/odata/ODataModel",
	"sap/ui/model/json/JSONModel"
], function(Controller, Dialog, Button, Text) {
	"use strict";

	return Controller.extend("com.sap.espm.shop.controller.App", {
		
		onInit:function()
		{

			
		},
		onAfterRendering: function() 
		{
		
			if (!this.pressDialog) {
				this.pressDialog = new Dialog({
					title: 'Important Information',
					content: new Text({
						text: "ESPM is a demo application. So, please do not enter any real personal information when using the application"
					}),
					beginButton: new Button({
						text: 'Close',
						press: function () {
							this.pressDialog.close();
						}.bind(this)
					})
				});

				//to get access to the global model
				this.getView().addDependent(this.pressDialog);
			}

			this.pressDialog.open();
			
		},
		onBeforeRendering: function() 
		{
		
		},
		onLineItemPressed: function()
		{
			
		}

		

	});

});