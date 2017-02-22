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
			
			
		
			//to-do replace this with your code implementation
			
			//initialize router
			
			this.getRouter().initialize();
		}
	});

});
