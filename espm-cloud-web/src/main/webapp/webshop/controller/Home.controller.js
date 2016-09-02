sap.ui.define([
	"sap/ui/core/mvc/Controller",
	"com/sap/espm/shop/model/formatter",
	"com/sap/espm/shop/model/utility",
	"sap/ui/core/UIComponent",
	"sap/ui/model/Sorter",
	"sap/ui/model/Filter",
	"sap/ui/model/FilterOperator"
], function(Controller, formatter, utility, UIComponent, Sorter, Filter, FilterOperator) {
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
			 
		},
		onBeforeRendering: function() 
		{
		
		},
		onLineItemPressed: function(event)
		{
			var bindingContext = event.getSource().getBindingContextPath();
			var oRouter = UIComponent.getRouterFor(this);
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
			
			var oRouter = UIComponent.getRouterFor(this);
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
			
			var filter = new Filter("Name", FilterOperator.Contains, searchString);
			newFilters.push(filter);
			if(this._oCombobox.getValue().length === 0){
				binding.filter(filter);
			}
			else{
				
				binding.filter( [ new Filter([
	                                           new Filter("Category", FilterOperator.EQ, this._oCombobox.getValue()),
	                                           new Filter("Name", FilterOperator.Contains, searchString)
	                                        ],true)]);
			}

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
			var sorters = new Sorter(sPath, bDescending);
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
				sorters = new Sorter(sPath, bDescending, vGroup);
				
			}
			oBinding.sort(sorters);
			
			
		},
		handleSelectionFinish: function(oEvent){
			
			var oTable = this.getView().byId("catalogTable");
			var binding = oTable.getBinding("items");
			
			var oFilter = new Filter("Category", FilterOperator.EQ, oEvent.getSource().getValue());  
			binding.filter(oFilter);
			
			
		},
		
		onResetPressed: function(){
			
			var oTable = this.getView().byId("catalogTable");
			oTable.getBinding("items").filter(null);
			oTable.getBinding("items").sort(null);
			
			this.getView().byId("searchField").setValue("");
			this._oCombobox.setValue("");
			
		},
		
		onOrdersButtonPressed: function(){
			
			var oRouter = UIComponent.getRouterFor(this);
			oRouter.navTo("SalesOrder");
		}
		
		
		

		

	});

});