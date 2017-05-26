jQuery.sap.require("com.sap.espm.shop.model.format");
sap.ui.define([
	"sap/ui/core/mvc/Controller",
	"com/sap/espm/shop/model/formatter",
	"sap/ui/core/UIComponent",
	"sap/ui/model/odata/ODataModel",
	"sap/ui/model/json/JSONModel",
	"sap/ui/core/routing/History",
	"sap/m/MessageBox"
], function(Controller, formatter, UIComponent, ODataModel, JSONModel, History, MessageBox) {
	"use strict";
		var oDataModel;
		var customerId = "";
		
	return Controller.extend("com.sap.espm.shop.controller.Checkout", {
		
		formatter: formatter,
		
		cardType: "american",
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

            var today = new Date();
			this.getView().byId("birthId").setMaxDate(today);
		},
		onAfterRendering: function() 
		{
			this.getView().byId("twitterId").setValue(sap.app.espm.screenName);
			
			
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
			var data = oModel.getProperty("/ShoppingCart");
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
			
			if(this.byId("firstNameId").getValue().length === 0 || this.byId("lastnameId").getValue().length === 0 || this.byId("birthId").getValue().length === 0 || this.byId("newEmailId").getValue().length === 0 ||
			this.byId("streetId").getValue().length === 0 || this.byId("cityId").getValue().length === 0 || this.byId("postalId").getValue().length === 0 || this.byId("countryListId").getSelectedKey().length === 0 ||
			this.byId("nameId").getValue().length === 0 || this.byId("numberId").getValue().length === 0)
			{
				sap.m.MessageToast.show("Please enter all missing details");
			}
			else{
				sap.ui.getCore().byId("firstname").setText(this.byId("firstNameId").getValue());
				sap.ui.getCore().byId("lastName").setText(this.byId("lastnameId").getValue());
				sap.ui.getCore().byId("dateBirth").setText(this.byId("birthId").getValue());
				sap.ui.getCore().byId("emailAddress").setText(this.byId("newEmailId").getValue());
				sap.ui.getCore().byId("street").setText(this.byId("streetId").getValue());
				sap.ui.getCore().byId("houseNumber").setText(this.byId("houseNumberId").getValue());
				sap.ui.getCore().byId("city").setText(this.byId("cityId").getValue());
				sap.ui.getCore().byId("postalCode").setText(this.byId("postalId").getValue());
				sap.ui.getCore().byId("country").setText(this.byId("countryListId").getSelectedKey());
				sap.ui.getCore().byId("cardOwner").setText(this.byId("nameId").getValue());
				sap.ui.getCore().byId("cardNumber").setText(this.byId("numberId").getValue());
				
				sap.ui.getCore().byId("cardImg").setSrc("Images/"+ this.cardType+ ".png");
				
				this._oNavContainer.to(this._oWizardReviewPage);
			}
			
		},
		handleWizardCancel: function(){
			
			this._oNavContainer.backToPage(this._oWizardContentPage.getId());
			var oRouter = UIComponent.getRouterFor(this);
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
            var oBundle = this.getView().getModel("i18n").getResourceBundle();
            var that = this;
            var date = this.byId("birthId").getValue();
            var utctime = Date.parse(date);                                
            date = "/Date("+utctime+")/";
			var customer = {
				"EmailAddress":this.byId("newEmailId").getValue(),
				"LastName":this.byId("lastnameId").getValue(),
				"FirstName":this.byId("firstNameId").getValue(),
				"HouseNumber":this.byId("houseNumberId").getValue(),
				"DateOfBirth":date,
				"PostalCode":this.byId("postalId").getValue(),
				"City":this.byId("cityId").getValue(),
				"Street":this.byId("streetId").getValue(),
				"Country":this.byId("countryListId").getSelectedKey(),
				"Twitterid":sap.app.espm.screenName
				
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
            error: function() {
                sap.m.MessageToast.show(oBundle.getText("check.customerCreateFailed"));
            }
        	});			
		},
		createSalesOrder: function(){
			
			var that = this;
			var sCustomerId = customerId;
			var SalesOrderHeader = {};
            var oBundle = this.getView().getModel("i18n").getResourceBundle();                     
			SalesOrderHeader.CustomerId = sCustomerId;
		
			var items = [];
			var cartModel = this.getView().getModel("Cart");
			var products = cartModel.getProperty("/ShoppingCart");
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
            success: function(oData) {
            		
            		
            		var dataJson = {
            			"uri" : "Customers('" + oData.d.CustomerId + "')"
            		};
            		$.ajax({
			            type: "PUT",
			            async: true,
			            contentType:"application/json; charset=utf-8",
			             url: "/espm-cloud-web/espm.svc/SalesOrderHeaders('" + oData.d.SalesOrderId + "')/$links/Customer",
			            data :JSON.stringify(dataJson),
			            success: function() {
			            	
			            	$.ajax({
					            type: "GET",
					            async: true,
					            contentType:"application/json; charset=utf-8",
					            dataType: "json",
					            url: "/espm-cloud-web/espm.svc/GetSalesOrderItemsById?SalesOrderId='"+ oData.d.SalesOrderId +"'",
					            success: function(data) {
					            	var length = data.d.results.length;
					            	for(var j=0;j<length;j++){
					            		var productDataJson = {
					            				"uri" : "Products('" + data.d.results[j].ProductId + "')"
					                		};
					            		$.ajax({
								            type: "PUT",
								            async: true,
								            dataType: "json",
								            contentType:"application/json; charset=utf-8",
								            url: "/espm-cloud-web/espm.svc/SalesOrderItems(ItemNumber=" + data.d.results[j].ItemNumber + ",SalesOrderId='"+ data.d.results[j].SalesOrderId + "')/$links/Product",
								            data :JSON.stringify(productDataJson),
								            success: function() {
								            	
								            	
								            },
								            error: function() {
								            	sap.m.MessageToast.show(oBundle.getText("soPopup.errorMessage"));
								                that._oWizardReviewPage.setBusy(false);
								            }});
					            	}
					            	that._oWizardReviewPage.setBusy(false);
					            	that.showOrderSuccessDialog(oData.d.SalesOrderId);
					            },
					            error: function() {
					            	sap.m.MessageToast.show(oBundle.getText("soPopup.errorMessage"));
					                that._oWizardReviewPage.setBusy(false);
					            }
					        });
			            	
			            	
			            		
			            },
			            error: function() {
			            	sap.m.MessageToast.show(oBundle.getText("soPopup.errorMessage"));
			                that._oWizardReviewPage.setBusy(false);
			            }
			        	});
            },
            error: function() {
            	sap.m.MessageToast.show(oBundle.getText("soPopup.errorMessage"));
                that._oWizardReviewPage.setBusy(false);
            }
        	});
			
				
		},
		showOrderSuccessDialog: function(orderId){
            var oBundle = this.getView().getModel("i18n").getResourceBundle();
			var that = this;
			var dialog = new sap.m.Dialog({
				id:"orderDialogId",
				title: oBundle.getText("tweet.title"),
				type: "Message",
				content: [
					new sap.ui.layout.form.SimpleForm({
						content:[
						        new sap.m.Text({text:oBundle.getText("check.soIDCreated", [orderId])})
						      ]
					})
				],
				beginButton: new sap.m.Button({
					text: oBundle.getText("tweet.btnText"),
					press: function () {
						
						dialog.close();
						
						var model = that.getView().getModel("Cart");
						var aData = model.getProperty("/ShoppingCart");
						var tweetStr;
						for(var i=0;i<aData.length;i++){
							
							var prodName = aData[i].Name;
							if(!tweetStr){
								tweetStr = prodName;
							}
							else{
								tweetStr = tweetStr+" , "+prodName;
							}
							 
						}
						var tweetMsg = {
								"status": oBundle.getText("tweet.message", [tweetStr])
								
							};
						
							$.ajax({
				            type: "POST",
				            async: true,
				            contentType:"application/json; charset=utf-8",
				            dataType: "json",
				            url: "/espm-cloud-web/TwitterUpdateWs",
				            data : JSON.stringify(tweetMsg),
				            success: function() {
				            	 sap.m.MessageToast.show(oBundle.getText("tweet-successMessage"));
				            },
				            error: function(err) {
				            	if(err.status === 200){
				            		sap.m.MessageToast.show(oBundle.getText("tweet.successMessage"));
				            	}
				            	else{
				            		sap.m.MessageToast.show(oBundle.getText("tweet.checkMessage"));
				            	}
				                
				            }
				        	});
							
						that._oNavContainer.backToPage(that._oWizardContentPage.getId());
	            		
						var newArray = [];
						model.setData({ShoppingCart : newArray});
						sap.ui.getCore().byId("totalFooter").setNumber("");
						sap.ui.getCore().byId("totalFooter").setUnit("");
						var oRouter = UIComponent.getRouterFor(that);
						oRouter.navTo("Home", true);
							
					}
				}),
				endButton: new sap.m.Button({
					text: oBundle.getText("tweet.btnOk"),
					press: function () {
						
						dialog.close();
						
						that._oNavContainer.backToPage(that._oWizardContentPage.getId());
	            		var model = that.getView().getModel("Cart");
						var newArray = [];
						model.setData({ShoppingCart : newArray});
						sap.ui.getCore().byId("totalFooter").setNumber("");
						sap.ui.getCore().byId("totalFooter").setUnit("");
						var oRouter = UIComponent.getRouterFor(that);
						oRouter.navTo("Home", true);
					}
				}),
				afterClose: function() {
					dialog.destroy();
				}
			});
 
			dialog.open();
		},
		backToWizardContent : function () {
			this._oNavContainer.backToPage(this._oWizardContentPage.getId());
		},
		radioButtonSelected: function(oEvent){
			
			var buttonId = oEvent.getSource().getSelectedIndex();
			if(buttonId === 0){
				this._oExistingForm.setVisible(true);
				this._oNewForm.setVisible(false);
			}
			else{
				this._oExistingForm.setVisible(false);
				this._oNewForm.setVisible(true);
			}
			
		},
		checkExistingCustomerAccount: function(){
            var oBundle = this.getView().getModel("i18n").getResourceBundle();
			var that = this;
			if(sap.app.espm.screenName){
				
				var sFunctionImportEmailParam = "twitterid='" + sap.app.espm.screenName + "'";
				var aParams = [];
				aParams.push(sFunctionImportEmailParam);
				
				oDataModel = this.getView().getModel("EspmModel");
				
				oDataModel.setHeaders({  
	            "Content-Type": "application/json",
	            "Accept": "application/json"
	        	}); 
				oDataModel.read("/GetCustomerByTwitterId",null, aParams , false, function(data)
				{
	 				if(data.results.length === 0){
	 					sap.m.MessageToast.show(oBundle.getText("check.enterDetails"));
						that._oNewForm.setVisible(true);
	 					customerId = "";
	 				}
	 				else{
	 					var result = data.results;
	 					that.byId("firstNameId").setValue(result[0].FirstName);
	 					that.byId("houseNumberId").setValue(result[0].HouseNumber);
	 					that.byId("lastnameId").setValue(result[0].LastName);
	 					that.byId("newEmailId").setValue(result[0].EmailAddress);
	 					that.byId("birthId").setDateValue(new Date(result[0].DateOfBirth));
	 					that.byId("streetId").setValue(result[0].Street);
	 					that.byId("cityId").setValue(result[0].City);
	 					that.byId("countryListId").setSelectedKey(result[0].Country);
	 					that.byId("postalId").setValue(result[0].PostalCode);
	 					that.byId("nameId").setValue(result[0].FirstName+" "+result[0].LastName);
						that._oNewForm.setVisible(true);	 					
						customerId = result[0].CustomerId;
	 					
	 				}
	 			},function(){
	 				sap.m.MessageToast.show(oBundle.getText("tweet.checkMessage"));
	 				
	 			});
				
			}
			
			
     
		},
		validateEmail: function(mail){
            var oBundle = this.getView().getModel("i18n").getResourceBundle();
			if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(mail)) {
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
		
		valueChanged: function(){
			
			this.byId("nameId").setValue(this.byId("firstNameId").getValue()+" "+this.byId("lastnameId").getValue());
			this.checkCustomerInformation();
		},
		
		checkCustomerInformation: function(){
			
			if(this.byId("firstNameId").getValue().length === 0 || this.byId("lastnameId").getValue().length === 0 || this.byId("birthId").getValue().length === 0 || this.byId("newEmailId").getValue().length === 0 ||
					this.byId("streetId").getValue().length === 0 || this.byId("houseNumberId").getValue().length === 0 || this.byId("cityId").getValue().length === 0 || this.byId("postalId").getValue().length === 0 || this.byId("countryListId").getSelectedKey().length === 0 ||
					this.byId("nameId").getValue().length === 0 ){
				this._wizard.invalidateStep(this.getView().byId("creditCardStep"));
			}
			else{
				
				this._wizard.validateStep(this.getView().byId("creditCardStep"));
			}
		},
		
		addMoreProducts: function(){
			var oRouter = UIComponent.getRouterFor(this);
			oRouter.navTo("Home", true);
		},
		onRbChange: function(oEvent){
			this.cardType = oEvent.getSource().data("cardtype");
  		}
		

	});

});