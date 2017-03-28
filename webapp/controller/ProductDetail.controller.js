sap.ui.define([
	"sap/ui/core/mvc/Controller",
	"com/sap/espm/shop/model/formatter",
	"sap/ui/core/UIComponent",
	"sap/ui/core/routing/History"
], function(Controller, formatter, UIComponent, History) {
	"use strict";
	var bindingObject, bindingPath;
	return Controller.extend("com.sap.espm.shop.controller.ProductDetail", {

		formatter: formatter,
		sortReviewDesc: true,
		onInit: function() {
			var that = this;
			var oComponent = this.getOwnerComponent();
			var oModel = oComponent.getModel("Cart");
			this.getView().addEventDelegate({
				onAfterShow: function() {
					that.getView().byId("btnProductHeader").setText(formatter.onAddCountToCart(oModel));
				}
			});

			var oRouter = UIComponent.getRouterFor(this);
			oRouter.getRoute("ProductDetail").attachPatternMatched(this._onObjectMatched, this);

			this._oReviewDialog = null;

		},
		_onObjectMatched: function(oEvent) {
			bindingObject = oEvent.getParameter("arguments").Productdetails;
			bindingPath = "/" + bindingObject;
			bindingObject = "EspmModel>/" + bindingObject;
			this.getView().bindElement(bindingObject);
			this.byId("reviewTable").bindItems({
				path: bindingObject + "/CustomerReview",
				template: this.byId("reviewListItem")
			});
			var oView = this.getView();
			var oTable = oView.byId("reviewTable");

			var oBinding = oTable.getBinding("items");
			//Sorter part commented	
			//	var sorters = new sap.ui.model.Sorter("CreationDate", true);
			//	oBinding.sort(sorters);
		},

		onAfterRendering: function() {

		},
		onBeforeRendering: function() {

		},
		onNavBack: function() {
			var oHistory = History.getInstance();
			var sPreviousHash = oHistory.getPreviousHash();

			if (sPreviousHash !== undefined) {
				window.history.go(-1);
			} else {
				var oRouter = UIComponent.getRouterFor(this);
				oRouter.navTo("Home", true);
			}
		},
		onSupplierPressed: function(oEvent) {
			var oLink = oEvent.getSource();
			if (!this._oSupplierCard) {
				this._initializeSupplierCard();
			} else {
				this._oSupplierCard.bindElement({
					path: bindingObject + "/Supplier"

				});

			}
			this._oSupplierCard.openBy(oLink);

		},
		_initializeSupplierCard: function() {
			this._oSupplierCard = sap.ui.xmlfragment("com.sap.espm.shop.view.fragment.SupplierCard", this.getView());
			this._oSupplierCard.bindElement({
				path: bindingObject + "/Supplier"
			});
			this.getView().addDependent(this._oSupplierCard);
		},
		onAddToCartPressed: function() {

			var oModel = this.getView().getModel("Cart");
			var model = this.getView().getModel("EspmModel");
			var productContext = model.getProperty(bindingPath);
			formatter.onAddToCart(oModel, productContext);
			this.getView().byId("btnProductHeader").setText(formatter.onAddCountToCart(oModel));

		},
		onShoppingCartPressed: function() {

			var oRouter = UIComponent.getRouterFor(this);
			oRouter.navTo("Shoppingcart");

		},
		_createDialog: function(sDialog) {
			var oDialog = sap.ui.xmlfragment(sDialog, this);
			jQuery.sap.syncStyleClass("sapUiSizeCompact", this._oView, oDialog);
			this.getView().addDependent(oDialog);
			return oDialog;
		},
		onEditReviewPressed: function() {

			if (!this._oReviewDialog) {
				this._oReviewDialog = this._createDialog("com.sap.espm.shop.view.fragment.ReviewDialog");
			}
			this._oReviewDialog.open();
		},
		onReviewDialogOKPressed: function(oEvent) {
			var that = this;
			var oBundle = this.getView().getModel('i18n').getResourceBundle();

			this._oReviewDialog.close();
			var iRatingCount = sap.ui.getCore().byId("ratingIndicator", "reviewDialog").getValue();
			var sReviewComment = sap.ui.getCore().byId("textArea", "reviewDialog").getValue();
			var firstName = sap.ui.getCore().byId("firstNameId", "reviewDialog").getValue();
			var lastName = sap.ui.getCore().byId("lastNameId", "reviewDialog").getValue();
			var createtionDate = new Date().getTime();
			var prodId = this.byId("productId").getText();

			var sFunctionImportReviewParam = "ProductId='" + prodId + "'&FirstName='" + firstName + "'&LastName='" + lastName + "'&Rating='" +
				iRatingCount + "'&CreationDate='" + createtionDate + "'&Comment='" + sReviewComment + "'";

			var aParams = [];
			aParams.push(sFunctionImportReviewParam);

			var oModel = this.getView().getModel("EspmModel");

			$.ajax({
				type: "GET",
				async: true,
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				url: "/ESPM/espm-cloud-web/espm.svc/CreateCustomerReview?" + sFunctionImportReviewParam,
				success: function(data) {

					if (data) {
						sap.m.MessageToast.show(oBundle.getText("detail.reviewSuccess"));
						// Clear review dialog
						that.clearReviewDialogForm();
						oModel.refresh(true);

					}

				},
				error: function() {
					sap.m.MessageToast.show(oBundle.getText("detail.reviewFailed"));
				}
			});


			var oView = this.getView();
			var oTable = oView.byId("reviewTable");

			var oBinding = oTable.getBinding("items");

			var sorters = new sap.ui.model.Sorter("CreationDate", true);
			oBinding.sort(sorters);

		},

		// Close the Review Dialog
		onReviewDialogCancelPressed: function() {
			this._oReviewDialog.close();
			this.clearReviewDialogForm();
		},

		clearReviewDialogForm: function() {
			sap.ui.getCore().byId("ratingIndicator", "reviewDialog").setValue(0);
			sap.ui.getCore().byId("textArea", "reviewDialog").setValue("");
			sap.ui.getCore().byId("firstNameId", "reviewDialog").setValue("");
			sap.ui.getCore().byId("lastNameId", "reviewDialog").setValue("");
		},
		onTextAreaChanged: function() {
			sap.ui.getCore().byId("btnOK", "reviewDialog").setEnabled(false);
			var iRatingCount = sap.ui.getCore().byId("ratingIndicator", "reviewDialog").getValue();
			var sReviewComment = sap.ui.getCore().byId("textArea", "reviewDialog").getValue();
			if (iRatingCount > 0 && sReviewComment) {
				sap.ui.getCore().byId("btnOK", "reviewDialog").setEnabled(true);
			}
		},

		validateReviewForm: function() {
			var iRatingCount = sap.ui.getCore().byId("ratingIndicator", "reviewDialog").getValue();
			var sReviewComment = sap.ui.getCore().byId("textArea", "reviewDialog").getValue();
			var firstName = sap.ui.getCore().byId("firstNameId", "reviewDialog").getValue();
			var lastName = sap.ui.getCore().byId("lastNameId", "reviewDialog").getValue();

			if (iRatingCount > 0 && sReviewComment !== "" && firstName !== "" && lastName !== "") {
				sap.ui.getCore().byId("btnOK", "reviewDialog").setEnabled(true);
			} else {
				sap.ui.getCore().byId("btnOK", "reviewDialog").setEnabled(false);
			}
		},
		onTableSettingsPressed: function() {
			var oBinding = this.byId("reviewTable").getBinding("items");
			var aSorters = [];
			var aDescending = this.sortReviewDesc;
			this.sortReviewDesc = !this.sortReviewDesc;

			aSorters.push(new sap.ui.model.Sorter('Rating', aDescending));
			oBinding.sort(aSorters);

		}

	});

});