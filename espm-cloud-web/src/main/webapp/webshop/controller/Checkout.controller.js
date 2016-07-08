jQuery.sap.require("com.sap.espm.shop.model.format");
sap.ui.define([
	"sap/ui/core/mvc/Controller",
	"com/sap/espm/shop/model/formatter",
	"sap/ui/core/UIComponent",
	"sap/ui/core/mvc/ViewType",
	"sap/ui/model/odata/ODataModel",
	"sap/ui/model/json/JSONModel",
	"sap/ui/core/routing/History",
	"sap/m/MessageBox"
], function(Controller, formatter, UIComponent, ViewType, ODataModel, JSONModel, History, MessageBox) {
	"use strict";
		var oDataModel;
		var customerId = "";
		
	return Controller.extend("com.sap.espm.shop.controller.Checkout", {
		
		formatter: formatter,
		
		onInit:function()
		{
			
			
			var that = this;
			
			var oCheckoutTemplate = new sap.m.ColumnListItem({
				cells:[
					new sap.m.Image({
						src : "/espm-cloud-web/images/{PictureUrl}",
						decorative : false,
						densityAware : false,
						height : "3rem",
						width : "3rem"
						
					}),
					new sap.m.ObjectIdentifier({
						title : "{Name}"
					}),
					new sap.m.Text({
						text : "{Quantity}"
						
					}), 
					new sap.m.ObjectNumber({
						emphasized : false,
						id : "checkoutPrice",
						number : "{path: 'Price', formatter:'com.sap.espm.shop.model.format.formatAmount'}",
						unit : "{CurrencyCode}"
					}),
					new sap.m.ObjectNumber({
						number : "{path: 'Total', formatter:'com.sap.espm.shop.model.format.formatAmount'}",
						id : "checkoutCurrency",
						unit : "{CurrencyCode}"
					})
					]
			});
			var oComponent = this.getOwnerComponent();
			var model = oComponent.getModel("Cart");
			var oCheckoutTable = this.getView().byId("checkoutCartTable");
			oCheckoutTable.setModel(model);
			oCheckoutTable.bindItems("/ShoppingCart",oCheckoutTemplate);
			
			this.getView().addEventDelegate({
				onAfterShow: function() {
					that.onAddCountToCart();
				}});
			this._oView = this.getView();
			this._oTotalFooter = this.byId("totalFooter");
			this._oExistingForm = this.byId("existingFormId");
			this._oNewForm = this.byId("newFormId");
			////wizard 
			this._wizard = this.getView().byId("checkoutWizard");
			this._oNavContainer = this.getView().byId("wizardNavContainer");
			this._oWizardReviewPage = sap.ui.xmlfragment("com.sap.espm.shop.view.fragment.ReviewPage", this);
			this._oNavContainer.addPage(this._oWizardReviewPage);
			this._oWizardContentPage = this.getView().byId("checkoutContentPage");
			
			var oreviewTable = sap.ui.getCore().byId("reviewCartTable");
			oreviewTable.setModel(model);
			oreviewTable.bindItems("/ShoppingCart",oCheckoutTemplate);
			
			//model for countries in the UI
			var ocountryModel = new JSONModel(); 
			ocountryModel.loadData("/espm-cloud-web/webshop/model/countries.json");
			this.getView().byId("countryListId").setModel(ocountryModel, "countryModel");
			
			
		},
		onAfterRendering: function() 
		{
		
		},
		onBeforeRendering: function() 
		{
		
		},
		onNavBack: function () {
			window.history.go(-1);
		},
		onAddCountToCart: function(){
			
			var totalQuantity = 0;
			var subTotal = 0;
			var currency;
			var oModel = this.getView().getModel("Cart");
			var data = oModel.getProperty('/ShoppingCart');
			if(data){
				for(var i=0; i<data.length; i++){
					var prodId = data[i];
					totalQuantity += prodId.Quantity;
					subTotal += prodId.Total;
					currency = "EUR";
				}
			this.getView().byId("btnShoppingCartHeader").setText(totalQuantity);
			
			subTotal = formatter.formatAmount(subTotal);
			this._oTotalFooter.setNumber(subTotal);
			this._oTotalFooter.setUnit(currency);
			sap.ui.getCore().byId("totalFooter").setNumber(subTotal);
			sap.ui.getCore().byId("totalFooter").setUnit(currency);
			}
			else{
				this._oTotalFooter.setNumber("");
				this._oTotalFooter.setUnit("");
			}
			
			
			
		},
		_handleMessageBoxOpen : function (sMessage, sMessageBoxType) {
			var that = this;
			MessageBox[sMessageBoxType](sMessage, {
				actions: [MessageBox.Action.YES, MessageBox.Action.NO],
				onClose: function (oAction) {
					if (oAction === MessageBox.Action.YES) {
						that._handleNavigationToStep(0);
						that._wizard.discardProgress(that._wizard.getSteps()[0]);
					}
				}
			});
		},
		_handleNavigationToStep : function (iStepNumber) {
			var that = this;
			function fnAfterNavigate () {
				that._wizard.goToStep(that._wizard.getSteps()[iStepNumber]);
				that._oNavContainer.detachAfterNavigate(fnAfterNavigate);
			}

			this._oNavContainer.attachAfterNavigate(fnAfterNavigate);
			this.backToWizardContent();
		},
		discardProgress: function () {
			this._wizard.discardProgress(this.getView().byId("ProductTypeStep"));

			var clearContent = function (content) {
				for (var i = 0; i < content.length ; i++) {
					if (content[i].setValue) {
						content[i].setValue("");
					}

					if (content[i].getContent) {
						clearContent(content[i].getContent());
					}
				}
			};
			clearContent(this._wizard.getSteps());
		},
		wizardCompletedHandler : function () {
			
			var oBundle = this.getView().getModel('i18n').getResourceBundle();
			var validationFlag = true;
			var myInteger = (/^-?\d*(\.\d+)?$/);
			var mailregex = /^\w+[\w-+\.]*\@\w+([-\.]\w+)*\.[a-zA-Z]{2,}$/;	
			var firstName = this.byId("firstNameId").getValue();
			var lastName = this.byId("lastnameId").getValue();
			var birthDate = this.byId("birthId").getValue();
			var eMail = this.byId("newEmailId").getValue();
			var street = this.byId("streetId").getValue();
			var city = this.byId("cityId").getValue();
			var postalCode = this.byId("postalId").getValue();
			var country = this.byId("countryListId").getSelectedKey();
			var name = this.byId("nameId").getValue();
			var SecNumber = this.byId("numberId").getValue();
			
			if (!eMail.match(mailregex)) {
				validationFlag = false;
			}
			
			if(validationFlag === false || (!street.match(myInteger)) === true || (!postalCode.match(myInteger)) === true || (name.match(myInteger)) === true || (!SecNumber.match(myInteger)) === true || firstName.match(myInteger) === true || lastName.match(myInteger) === true || city.match(myInteger) === true || country.match(myInteger) === true){
				validationFlag = false;
			}
			
			if(validationFlag === false || this.byId("firstNameId").getValue().length === 0 || this.byId("lastnameId").getValue().length === 0 || this.byId("birthId").getValue().length === 0 || this.byId("newEmailId").getValue().length === 0 ||
					this.byId("streetId").getValue().length === 0 || this.byId("cityId").getValue().length === 0 || this.byId("postalId").getValue().length === 0 || this.byId("countryListId").getSelectedKey().length === 0 ||
					this.byId("nameId").getValue().length === 0 || this.byId("numberId").getValue().length === 0)
			{
				sap.m.MessageToast.show(oBundle.getText("soPopup.errorMessage"));
			}
			else{
				sap.ui.getCore().byId("firstname").setText(this.byId("firstNameId").getValue());
				sap.ui.getCore().byId("lastName").setText(this.byId("lastnameId").getValue());
				sap.ui.getCore().byId("dateBirth").setText(this.byId("birthId").getValue());
				sap.ui.getCore().byId("emailAddress").setText(this.byId("newEmailId").getValue());
				sap.ui.getCore().byId("street").setText(this.byId("streetId").getValue());
				sap.ui.getCore().byId("city").setText(this.byId("cityId").getValue());
				sap.ui.getCore().byId("postalCode").setText(this.byId("postalId").getValue());
				sap.ui.getCore().byId("country").setText(this.byId("countryListId").getSelectedKey());
				sap.ui.getCore().byId("cardOwner").setText(this.byId("nameId").getValue());
				sap.ui.getCore().byId("cardNumber").setText(this.byId("numberId").getValue());
				
				this._oNavContainer.to(this._oWizardReviewPage);
			}
			
		},
		handleWizardCancel: function(){
			
			this._oNavContainer.backToPage(this._oWizardContentPage.getId());
			var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
			oRouter.navTo("Home", true);
		},
		handleWizardSubmit : function () {
			
			if(customerId.length === 0){
				this._oWizardReviewPage.setBusy(true);
				this.createCustomer();
			}
			else{
				this._oWizardReviewPage.setBusy(true);
				this.createSalesOrder();
			}
			
			
		},
		createCustomer: function(){
			var oBundle = this.getView().getModel('i18n').getResourceBundle();
			var that = this;
			var date = this.byId("birthId").getValue();
			var utctime = Date.parse(date);		
			date = "/Date("+utctime+")/";
			var customer = {
				"EmailAddress":this.byId("newEmailId").getValue(),
				"LastName":this.byId("lastnameId").getValue(),
				"FirstName":this.byId("firstNameId").getValue(),
				"DateOfBirth":date,
				"PostalCode":this.byId("postalId").getValue(),
				"City":this.byId("cityId").getValue(),
				"Street":this.byId("streetId").getValue(),
				"Country":this.byId("countryListId").getSelectedKey()
				
			};
			
			$.ajax({
            type: "POST",
            async: true,
            contentType:"application/json; charset=utf-8",
            dataType: "json",
            url: "/espm-cloud-web/espm.svc/Customers",
            data : JSON.stringify(customer),
            success: function(responsedata) {
            		customerId = responsedata.d.CustomerId;
 					that.createSalesOrder();
            },
            error: function(err) {
                sap.m.MessageToast.show(oBundle.getText("check.customerCreateFailed"));
            }
        	});
        	
			
		},
		createSalesOrder: function(){
			
			var that = this;
			var sCustomerId = customerId;
			var SalesOrderHeader = {};
	
			SalesOrderHeader.CustomerId = sCustomerId;
			var oBundle = this.getView().getModel('i18n').getResourceBundle();
		
			var items = [];
			var cartModel = this.getView().getModel("Cart");
			var products = cartModel.getProperty('/ShoppingCart');
			for (var i = 0; i < products.length; i++) {
				var product = products[i];
				var item = {
					"ProductId" : product.ProductId,
					"ItemNumber" : ((i + 1) * 10),
					"Quantity" : product.Quantity + "",
					"QuantityUnit" : product.QuantityUnit,
					"DeliveryDate" :  "2017-02-01T11:55:00"
				};
				items.push(item);
			}
			SalesOrderHeader.SalesOrderItems = items;
			
			
			$.ajax({
            type: "POST",
            async: true,
            contentType:"application/json; charset=utf-8",
            dataType: "json",
            url: "/espm-cloud-web/espm.svc/SalesOrderHeaders",
            data : JSON.stringify(SalesOrderHeader),
            success: function(oData, responsedata) {
            		
            		
            		var dataJson = {
            			"uri" : "Customers('" + oData.d.CustomerId + "')"
            		};
            		$.ajax({
			            type: "PUT",
			            async: true,
			            contentType:"application/json; charset=utf-8",
			            url: oData.d.__metadata.id + "/$links/Customer",
			            data :JSON.stringify(dataJson),
			            success: function(data, response) {
			            	
			            	$.ajax({
					            type: "GET",
					            async: true,
					            contentType:"application/json; charset=utf-8",
					            dataType: "json",
					            url: "/espm-cloud-web/espm.svc/GetSalesOrderItemsById?SalesOrderId='"+ oData.d.SalesOrderId +"'",
					            success: function(data, response) {
					            	var length = data.d.results.length;
					            	for(var i=0;i<length;i++){
					            		var productDataJson = {
					            				"uri" : "Products('" + data.d.results[i].ProductId + "')"
					                		};
					            		$.ajax({
								            type: "PUT",
								            async: true,
								            dataType: "json",
								            contentType:"application/json; charset=utf-8",
								            url: data.d.results[i].__metadata.id + "/$links/Product",
								            data :JSON.stringify(productDataJson),
								            success: function(data, response) {
								            	
								            	
								            },
								            error: function(err) {
								                sap.m.MessageToast.show(oBundle.getText("soPopup.errorMessage"));
								                that._oWizardReviewPage.setBusy(false);
								            }});
					            	}
					            	that._oWizardReviewPage.setBusy(false);
					            	sap.m.MessageBox.information(oBundle.getText("check.soIDCreated", [oData.d.SalesOrderId]),{
						            	onClose: function(oAction){
						            			
						            		that._oNavContainer.backToPage(that._oWizardContentPage.getId());
						            		var model = that.getView().getModel("Cart");
											var newArray = [];
											model.setData({ShoppingCart : newArray});
											sap.ui.getCore().byId("totalFooter").setNumber("");
											sap.ui.getCore().byId("totalFooter").setUnit("");
											that.getView().byId("existingCustomerId").setSelected(true);
											var oRouter = sap.ui.core.UIComponent.getRouterFor(that);
											oRouter.navTo("Home", true);
					            		}
						            });
					            },
					            error: function(err) {
					            	sap.m.MessageToast.show(oBundle.getText("soPopup.errorMessage"));
					                that._oWizardReviewPage.setBusy(false);
					            }
					        });
			            	
			            	
			            		
			            },
			            error: function(err) {
			            	sap.m.MessageToast.show(oBundle.getText("soPopup.errorMessage"));
			                that._oWizardReviewPage.setBusy(false);
			            }
			        	});
            },
            error: function(err) {
            	sap.m.MessageToast.show(oBundle.getText("soPopup.errorMessage"));
                that._oWizardReviewPage.setBusy(false);
            }
        	});
			
				
		},
		backToWizardContent : function () {
			this._oNavContainer.backToPage(this._oWizardContentPage.getId());
		},
		radioButtonSelected: function(oEvent){
			
			var buttonId = oEvent.getSource().getSelectedIndex();
			if(buttonId === 0){
				this._oExistingForm.setVisible(true);
				this._oNewForm.setVisible(false);
				this._wizard.validateStep(this.getView().byId("creditCardStep"));
			}
			else{
				this._oExistingForm.setVisible(false);
				this._oNewForm.setVisible(true);
				this._wizard.validateStep(this.getView().byId("creditCardStep"));
			}
			
		},
		checkExistingEmailId: function(oEvent){
			
			var that = this;
			var oBundle = this.getView().getModel('i18n').getResourceBundle();
			if(this.validateEmail(this.byId("existingEmailId").getValue()) === true){
				
				var sFunctionImportEmailParam = "EmailAddress='" + this.byId("existingEmailId").getValue() + "'";
				var aParams = [];
				aParams.push(sFunctionImportEmailParam);
				
				oDataModel = this.getView().getModel("EspmModel");
				
				oDataModel.setHeaders({  
	            "Content-Type": "application/json",
	            "Accept": "application/json"
	        	}); 
				oDataModel.read('/GetCustomerByEmailAddress',null, aParams , false, function(data)
				{
	 				if(data.results.length == 0){
	 					sap.m.MessageToast.show(oBundle.getText("check.customerNotExist"));
	 					
	 					that.byId("radioButtonGroupId").setSelectedIndex(1);// = 1;
	 					that._oExistingForm.setVisible(false);
						that._oNewForm.setVisible(true);
						that._wizard.validateStep(that.getView().byId("creditCardStep"));
	 					that.byId("newEmailId").setValue(that.byId("existingEmailId").getValue());
	 					
	 					customerId = "";
	 				}
	 				else{
	 					var result = data.results;
	 					that.byId("firstNameId").setValue(result[0].FirstName);
	 					that.byId("lastnameId").setValue(result[0].LastName);
	 					that.byId("newEmailId").setValue(result[0].EmailAddress);
	 					that.byId("birthId").setDateValue(new Date(result[0].DateOfBirth));
	 					that.byId("streetId").setValue(result[0].Street);
	 					that.byId("cityId").setValue(result[0].City);
	 					that.byId("countryListId").setSelectedKey(result[0].Country);
	 					that.byId("postalId").setValue(result[0].PostalCode);
	 					that.byId("nameId").setValue(result[0].FirstName+" "+result[0].LastName);
	 					
	 					that.byId("radioButtonGroupId").setSelectedIndex(1);// = 1;
	 					that._oExistingForm.setVisible(false);
						that._oNewForm.setVisible(true);
						that._wizard.validateStep(that.getView().byId("creditCardStep"));
	 					
						customerId = result[0].CustomerId;
	 					
	 				}
	 			},function(){
	 				sap.m.MessageToast.show(oBundle.getText("soPopup.errorMessage"));
	 				
	 			});
				
			}
			
			
     
		},
		validateEmail: function(mail){
			
			var mailregex = /^\w+[\w-+\.]*\@\w+([-\.]\w+)*\.[a-zA-Z]{2,}$/;	
			var oBundle = this.getView().getModel('i18n').getResourceBundle();
			if(typeof(mail) === "object"){
				mail = mail.getSource().getValue();
			}

			if (mail.match(mailregex)) {
				return (true);
			}else{
				sap.m.MessageToast.show(oBundle.getText("soPopup.invalidEmailAddress")); 

			}
		},
		validateNumberInputField: function(oEvent){
			
			var myInteger = (/^-?\d*(\.\d+)?$/);
			if( !oEvent.getSource().getValue().match(myInteger) ){
				oEvent.getSource().setValueState(sap.ui.core.ValueState.Error);
			}
			else{
				oEvent.getSource().setValueState(sap.ui.core.ValueState.None);
			}
			
		},
		validateStringInputField: function(oEvent){
			
			var myInteger = (/^-?\d*(\.\d+)?$/);
			if( oEvent.getSource().getValue().match(myInteger) ){
				oEvent.getSource().setValueState(sap.ui.core.ValueState.Error);
			}
			else{
				oEvent.getSource().setValueState(sap.ui.core.ValueState.None);
			}
			
			
		},
		
		valueChanged: function(oEvent){
			
			this.byId("nameId").setValue(this.byId("firstNameId").getValue()+" "+this.byId("lastnameId").getValue());
		},
		
		checkCustomerInformation: function(){
			if(this.byId("firstNameId").getValue().length === 0 || this.byId("lastnameId").getValue().length === 0 || this.byId("birthId").getValue().length === 0 || this.byId("newEmailId").getValue().length === 0 ||
					this.byId("streetId").getValue().length === 0 || this.byId("cityId").getValue().length === 0 || this.byId("postalId").getValue().length === 0 || this.byId("countryListId").getSelectedKey().length === 0){
				
				this._wizard.invalidateStep(this.getView().byId("creditCardStep"));
			}
			else{
				
				this._wizard.validateStep(this.getView().byId("creditCardStep"));
			}
		},
		
		addMoreProducts: function(){
			var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
			oRouter.navTo("Home", true);
		},
		
		formatCustomerLocation: function(location){
			 return formatter.formatCountryName(location);
		}
		

	});

});