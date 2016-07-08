sap.ui.define([
	"sap/ui/core/mvc/Controller",
	"com/sap/espm/shop/model/formatter",
	"sap/ui/core/UIComponent",
	"sap/ui/core/mvc/ViewType",
	"sap/ui/core/routing/History",
	"sap/ui/model/odata/ODataModel"
], function(Controller, formatter,ODataModel,UIComponent, History) {
	"use strict";
	var bindingObject, bindingPath;
	return Controller.extend("com.sap.espm.shop.controller.ProductDetail", {
		
		formatter: formatter,
		
		onInit:function()
		{
			var that = this;
			var oComponent = this.getOwnerComponent();
			var oModel = oComponent.getModel("Cart");
		  	this.getView().addEventDelegate({
				onAfterShow: function() {
					 that.getView().byId("btnProductHeader").setText(formatter.onAddCountToCart(oModel));
				}});
			
			var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
			oRouter.getRoute("ProductDetail").attachPatternMatched(this._onObjectMatched, this);
			
			this._oReviewDialog = null;

		},
		_onObjectMatched: function (oEvent) {
			bindingObject = oEvent.getParameter("arguments").Productdetails;
			bindingPath = "/"+bindingObject;
			bindingObject = "EspmModel>/"+bindingObject;
			this.getView().bindElement(bindingObject);
			this.byId("reviewTable").bindItems({
				path: bindingObject + "/CustomerReview",
				template: this.byId("reviewListItem")
			});
			var oView = this.getView();
			var oTable = oView.byId("reviewTable");

			var mParams = oEvent.getParameters();
			var oBinding = oTable.getBinding("items");
			
			var sorters = new sap.ui.model.Sorter("CreationDate", true);
			oBinding.sort(sorters);
		},

		onAfterRendering: function() 
		{
			
		},
		onBeforeRendering: function() 
		{
			
		},
		onNavBack: function () {
			var oHistory = History.getInstance();
			var sPreviousHash = oHistory.getPreviousHash();

			if (sPreviousHash !== undefined) {
				window.history.go(-1);
			} else {
				var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
				oRouter.navTo("Home", true);
			}
		},
		onSupplierPressed : function(oEvent){
			var oLink = oEvent.getSource();
			if (!this._oSupplierCard) {
				this._initializeSupplierCard();
			}
			else{
				this._oSupplierCard.bindElement({
				path: bindingObject + "/Supplier"
				
			});
			
			}
			this._oSupplierCard.openBy(oLink);
			
		},
		_initializeSupplierCard: function() {
			this._oSupplierCard = sap.ui.xmlfragment("com.sap.espm.shop.view.fragment.SupplierCard",this.getView());
			this._oSupplierCard.bindElement({
				path: bindingObject + "/Supplier"
			});
			this.getView().addDependent(this._oSupplierCard);
		},
		onAddToCartPressed: function(oEvent){
			
			var oModel = this.getView().getModel("Cart");
			var model = this.getView().getModel("EspmModel");
	        var productContext = model.getProperty(bindingPath);
	        formatter.onAddToCart(oModel,productContext);
	        this.getView().byId("btnProductHeader").setText(formatter.onAddCountToCart(oModel));
			
		},
		onShoppingCartPressed: function(){
			
			var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
			oRouter.navTo("Shoppingcart");
			
		},
		_createDialog: function(sDialog) {
			var oDialog = sap.ui.xmlfragment(sDialog, this);
			jQuery.sap.syncStyleClass("sapUiSizeCompact", this._oView, oDialog);
			this.getView().addDependent(oDialog);
			return oDialog;
		},
		onEditReviewPressed: function(oEvent) {
			
			if (!this._oReviewDialog) {
				this._oReviewDialog = this._createDialog("com.sap.espm.shop.view.fragment.ReviewDialog");
			}
			this._oReviewDialog.open();
		},
		onReviewDialogOKPressed: function(oEvent) {
			var oBundle = this.getView().getModel('i18n').getResourceBundle();
			this._oReviewDialog.close();
			var iRatingCount = sap.ui.getCore().byId("ratingIndicator", "reviewDialog").getValue();
			var sReviewComment = sap.ui.getCore().byId("textArea", "reviewDialog").getValue();
			var firstName = sap.ui.getCore().byId("firstNameId", "reviewDialog").getValue();
			var lastName = sap.ui.getCore().byId("lastNameId", "reviewDialog").getValue();
			var createtionDate = new Date().getTime(); 
			var prodId = this.byId("productId").getText();
			
			var sFunctionImportReviewParam = "ProductId='" + prodId + "'&FirstName='" + firstName + "'&LastName='" + lastName + "'&Rating='" + iRatingCount + "'&CreationDate='" + createtionDate + "'&Comment='" + sReviewComment + "'";
		
			var aParams = [];
			aParams.push(sFunctionImportReviewParam);
			
			var oModel = this.getView().getModel("EspmModel");
			
			oModel.setHeaders({ 
				"APIKey" : window.secretKey,
				"Content-Type": "application/json",
				"Accept": "application/json"
        	});
			oModel.read('/CreateCustomerReview',null, aParams , false, function(data)
			{
 				if(data){
 					sap.m.MessageToast.show(oBundle.getText("detail.reviewSuccess"));
 					
 				}
 			},function(){
 				sap.m.MessageToast.show(oBundle.getText("detail.reviewFailed")); 
 			});
			
			oModel.refresh(true);
			
			var oView = this.getView();
			var oTable = oView.byId("reviewTable");

			var mParams = oEvent.getParameters();
			var oBinding = oTable.getBinding("items");
			
			var sorters = new sap.ui.model.Sorter("CreationDate", true);
			oBinding.sort(sorters);
			
		},

		// Close the Review Dialog
		onReviewDialogCancelPressed: function() {
			this._oReviewDialog.close();
		},
		onTextAreaChanged: function() {
			sap.ui.getCore().byId("btnOK", "reviewDialog").setEnabled(false);
			var iRatingCount = sap.ui.getCore().byId("ratingIndicator", "reviewDialog").getValue();
			var sReviewComment = sap.ui.getCore().byId("textArea", "reviewDialog").getValue();
			if (iRatingCount > 0 && sReviewComment) {
				sap.ui.getCore().byId("btnOK", "reviewDialog").setEnabled(true);
			}
		}
		 

		

	});

});