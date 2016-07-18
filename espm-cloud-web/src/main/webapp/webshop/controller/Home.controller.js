sap.ui.define([
	"sap/ui/core/mvc/Controller",
	"com/sap/espm/shop/model/formatter",
	"com/sap/espm/shop/model/utility",
	"sap/ui/core/UIComponent",
	"sap/ui/core/mvc/ViewType",
	"com/sap/espm/shop/util/TableOperations",
	"sap/ui/model/json/JSONModel",
	"sap/ui/model/Sorter",
	"sap/ui/model/Filter",
	"sap/ui/model/FilterType",
	'sap/m/Button',
	'sap/m/Dialog',
	'sap/m/Text',
	"sap/ui/model/FilterOperator"
], function(Controller, formatter, utility, Dialog, Text, Button) {
	"use strict";


	return Controller.extend("com.sap.espm.shop.controller.Home", {
		
		formatter: formatter,
		utility: utility,
		onInit:function()
		{
			
			this._oCombobox = this.byId("categoryListId");
			
			var oComponent = this.getOwnerComponent();
			var oModel = oComponent.getModel("Cart");
			var that = this;
			this.getView().addEventDelegate({
				onAfterShow: function() {
					var count = formatter.onAddCountToCart(oModel);
					that.getView().byId("btnProductListHeader").setText(count);
				}});
			/*var oModel = new sap.ui.model.odata.ODataModel("https://espmespm.neo.ondemand.com/espm-cloud-web/espm.svc");
			this.getView().setModel(oModel);*/
			
			this.mGroupFunctions = {
				Category: function(oContext) {
					var name = oContext.getProperty("Category");
					return {
						key: name,
						text: name
					};
				}, 
				Price: function(oContext) {
					var price = oContext.getProperty("Price");
					var currencyCode = oContext.getProperty("CurrencyCode");
					var key, text;
					if (price <= 100) {
						key = "LE100";
						text = "100 " + currencyCode + " or less";
					} else if (price <= 1000) {
						key = "BT100-1000";
						text = "Between 100 and 1000 " + currencyCode;
					} else {
						key = "GT1000";
						text = "More than 1000 " + currencyCode;
					}
					return {
						key: key,
						text: text
					};
				}
			};
			
			
		},
		onAfterRendering: function() 
		{
			 
			/*jQuery.sap.require("jquery.sap.storage");
			var oStorage = jQuery.sap.storage(jQuery.sap.storage.Type.local);
			
			if (!oStorage.get("disclaimer")) {  
				
				var dialog = new sap.m.Dialog({
					title: 'SAP HANA Cloud Development Scenario',
					type: 'Message',
					content: [new sap.m.Text({
						text: 'This application is intended to serve as a reference application for usage of SAP HANA Cloud services and related technologies as part of a development scenario. This application consists of dummy data retrieved from a reference backend system. Users are advised not to enter any personal data as the application does not serve as a reference for handling confidential or sensitive information.'
					}),
					new sap.m.RadioButton({
						text : "Do not show this message again",
						select:function(evt){
							
							var data = {  
									  "disclaimer" : [ {  
									  "show" : "1"  
									  } ]  
									  };
							oStorage.put("disclaimer", data);  

						}
					})],
					beginButton: new sap.m.Button({
						text: 'OK',
						press: function () {
							dialog.close();
						}
					}),
					afterClose: function() {
						dialog.destroy();
					}
				});
	 
				dialog.open();
			}*/
			
		},
		onBeforeRendering: function() 
		{
		
		},
		onLineItemPressed: function(event)
		{
			var bindingContext = event.getSource().getBindingContextPath();
			var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
			oRouter.navTo("ProductDetail",{Productdetails:bindingContext.substr(1)});
		},
		onAddToCartHomePressed: function(oEvent){
			
			////get binded model
			var oModel = this.getView().getModel("Cart");
			var model = this.getView().getModel("EspmModel");
	        var path = oEvent.getSource().getParent().getBindingContextPath();
	        var productContext = model.getProperty(path);
	        formatter.onAddToCart(oModel,productContext);
	        this.getView().byId("btnProductListHeader").setText(formatter.onAddCountToCart(oModel));

		},
		onShoppingCartPressed: function(){
			
			var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
			oRouter.navTo("Shoppingcart");
			
		},
		/// Table Operations
		onSearchPressed : function(event){
 
			var searchString = event.mParameters.query;
			var oTable = this.getView().byId("catalogTable");
			var binding = oTable.getBinding("items");
			var enabledFilters;// = binding.aFilters; 
			enabledFilters = [];
			
			var newFilters = []; 
			enabledFilters.forEach(function(enabledFilter) {
			
			
			 newFilters.push(enabledFilter);
			
			});
			
			var filter = new sap.ui.model.Filter("Name", sap.ui.model.FilterOperator.Contains, searchString);
			//var oFilter = new sap.ui.model.Filter("Category", sap.ui.model.FilterOperator.Contains, searchString);  
			newFilters.push(filter);
			if(this._oCombobox.getValue().length === 0){
				binding.filter(filter);
			}
			else{
				
				binding.filter( [ new sap.ui.model.Filter([
				                                           new sap.ui.model.Filter("Category", sap.ui.model.FilterOperator.EQ, this._oCombobox.getValue()),
				                                           new sap.ui.model.Filter("Name", sap.ui.model.FilterOperator.Contains, searchString)
				                                        ],true)]);
			}
			
			
			//

		},
		_createDialog: function(sDialog) {
			var oDialog = sap.ui.xmlfragment(sDialog, this);
			jQuery.sap.syncStyleClass("sapUiSizeCompact", this._oView, oDialog);
			this.getView().addDependent(oDialog);
			return oDialog;
		},
		onSortPressed: function() {
			if (!this._oSortDialog) {
				this._oSortDialog = this._createDialog("com.sap.espm.shop.view.fragment.ProductSortDialog");
			}
			this._oSortDialog.open();
		},

		// Handler for the Confirm button of the sort dialog. Depending on the selections made on the sort
		// dialog, the respective sorters are created and stored in the _oTableOperations object.
		// The actual setting of the sorters on the binding is done by the callback method that is handed over to
		// the constructor of the _oTableOperations object.
		onSortDialogConfirmed: function(oEvent) {
			
			var oView = this.getView();
			var oTable = oView.byId("catalogTable");

			var mParams = oEvent.getParameters();
			var oBinding = oTable.getBinding("items");
			
			var sPath = mParams.sortItem.getKey();
			var bDescending = mParams.sortDescending;
			var sorters = new sap.ui.model.Sorter(sPath, bDescending);
			oBinding.sort(sorters);

		},
		
		onGroupPressed: function() {
			if (!this._oGroupDialog) {
				this._oGroupDialog = this._createDialog("com.sap.espm.shop.view.fragment.ProductGroupingDialog");
			}
			this._oGroupDialog.open();
		},
		onGroupingDialogConfirmed: function(oEvent) {
			
			var oView = this.getView();
			var oTable = oView.byId("catalogTable");

			var mParams = oEvent.getParameters();
			var oBinding = oTable.getBinding("items");
			var sorters = [];
			if (mParams.groupItem) {
				var sPath = mParams.groupItem.getKey();
				var bDescending = mParams.groupDescending;
				var vGroup = this.mGroupFunctions[sPath];
				sorters = new sap.ui.model.Sorter(sPath, bDescending, vGroup);
				
				//aSorters.push(new Sorter(sPath, bDescending, vGroup));
			}
			oBinding.sort(sorters);
			
			
		},
		handleSelectionFinish: function(oEvent){
			
			var oTable = this.getView().byId("catalogTable");
			var binding = oTable.getBinding("items");
			
			var oFilter = new sap.ui.model.Filter("Category", sap.ui.model.FilterOperator.EQ, oEvent.getSource().getValue());  
			binding.filter(oFilter);
			
			
		},
		
		onResetPressed: function(){
			
			var oTable = this.getView().byId("catalogTable");
			oTable.getBinding("items").filter(null);
			oTable.getBinding("items").sort(null);
			
			this.getView().byId("searchField").setValue("");
			this._oCombobox.setValue("");
			
			
			//this._resetSortingState();
			
		},
		
		onOrdersButtonPressed: function(){
			
			var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
			oRouter.navTo("SalesOrder");
		}
		
		
		

		

	});

});