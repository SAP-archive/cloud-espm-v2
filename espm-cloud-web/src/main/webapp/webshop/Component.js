sap.ui.define([
	"sap/ui/core/UIComponent",
	"sap/ui/Device",
	"com/sap/espm/shop/model/models",
	"sap/m/MessageToast"
], function(UIComponent, Device, models) {
	"use strict";

	return UIComponent.extend("com.sap.espm.shop.Component", {

		metadata: {
			manifest: "json"
		},

		/**
		 * The component is initialized by UI5 automatically during the startup of the app and calls the init method once.
		 * @public
		 * @override
		 */
		init: function() {
			// call the base component's init function
			UIComponent.prototype.init.apply(this, arguments);
			//initialize router
			
			// set the device model
			this.setModel(models.createDeviceModel(), "device");
			
			var sServiceUrl = this.getMetadata().getManifestEntry("sap.app").dataSources.espmDataModel.uri;
			var oEspmModel = new sap.ui.model.odata.ODataModel(sServiceUrl, {json: true,loadMetadataAsync: true});
			this.setModel(oEspmModel,"EspmModel");

			var oData ={
				ShoppingCart:[]
			};
			var oModel = new sap.ui.model.json.JSONModel(oData);
			this.setModel(oModel,"Cart");
			
			this.getRouter().initialize();
		}
	});

});