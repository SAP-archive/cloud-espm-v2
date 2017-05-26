jQuery.sap.require("com.sap.espm.shop.model.format");
sap.ui.define([
	"sap/ui/core/mvc/Controller",
	"com/sap/espm/shop/model/formatter",
	"sap/m/MessageToast"
	
	
], function(Controller, formatter, MessageToast) {
	"use strict";

	var pdfURL;
	
	
	return Controller.extend("com.sap.espm.shop.controller.SalesOrder", {

		formatter: formatter,
		
		/**
		 * Called when a controller is instantiated and its View controls (if available) are already created.
		 * Can be used to modify the View before it is displayed, to bind event handlers and do other one-time initialization.
		 * @memberOf com.sap.espm.retailer.view.SalesOrder
		 */
			onInit: function() {
		
				this.pdfHTML = this.getView().byId("pdfHTMLId");
				var that = this;
				this.getView().addEventDelegate({
					onAfterShow: function() {
						that.showPopUpdialog();
					}});
				
				this.getView().byId("detailPageId").setVisible(false); 
				var deviceModel = new sap.ui.model.json.JSONModel({
				    isPhone: sap.ui.Device.system.phone
				});
				this.getView().setModel(deviceModel, "device");
				
			},

		/**
		 * Similar to onAfterRendering, but this hook is invoked before the controller's View is re-rendered
		 * (NOT before the first rendering! onInit() is used for that one!).
		 * @memberOf com.sap.espm.retailer.view.SalesOrder
		 */
		//	onBeforeRendering: function() {
		//
		//	},

		/**
		 * Called when the View has been rendered (so its HTML is part of the document). Post-rendering manipulations of the HTML could be done here.
		 * This hook is the same one that SAPUI5 controls get after being rendered.
		 * @memberOf com.sap.espm.retailer.view.SalesOrder
		 */
			onAfterRendering: function() {
			},

		/**
		 * Called when the Controller is destroyed. Use this one to free resources and finalize activities.
		 * @memberOf com.sap.espm.retailer.view.SalesOrder
		 */
		//	onExit: function() {
		//
		//	}
		
			showPopUpdialog: function(){
				
				var that = this;
				var emailId;
				var oBundle = this.getView().getModel("i18n").getResourceBundle();
				
				var dialog = new sap.m.Dialog({
					title: oBundle.getText("soPopup.pageTitle"),
					type: "Message",
					content: [
						new sap.ui.layout.form.SimpleForm({
							content:[
							        new sap.m.Label({text:oBundle.getText("soPopup.emailAddress")}),
									new sap.m.Input({
										liveChange:function(oEvent){
											emailId = oEvent.getSource().getValue().toLowerCase();
										}
									})
							         
							      ]
						})
					],
					beginButton: new sap.m.Button({
						text: oBundle.getText("soPopup.salesOrderList"),
						press: function () {
							
							if(emailId){
								$.ajax({
						            type: "GET",
						            async: true,
						            contentType:"application/json; charset=utf-8",
						            dataType: "json",
						            url: "/espm-cloud-web/espm.svc/GetSalesOrderInvoiceByEmail?EmailAddress='"+ emailId +"'&$expand=SalesOrderItems,Customer&$format=json",
						            success: function(data) {
						            	
						            	that.bindMasterPage(data);
						            },
						            error: function() {
						            	MessageToast.show(oBundle.getText("soPopup.errorMessage"));
						            }
						        	});
								dialog.close();
							}
							else{
								MessageToast.show(oBundle.getText("soPopup.fieldEmpty"));
							}
							
						}
					}),
					endButton: new sap.m.Button({
						text: oBundle.getText("soPopup.cancel"),
						press: function () {
							dialog.close();
						}
					}),
					afterClose: function() {
						dialog.destroy();
					}
				});
	 
				dialog.open();
		},
		bindMasterPage: function(data){
			
			var masterList = this.getView().byId("list");
			var that = this;
			
			var objectTemplate = new sap.m.ObjectListItem({
				
                title : "{SalesOrderId}",
                number : "{GrossAmount}",
                numberUnit :"{CurrencyCode}",
                type: "Active",
                press : function(event){
                	that.handleListItemPress(event);
                },
                attributes : [new sap.m.ObjectAttribute({
                          			text : "{Category}"
                    				}), 
                    		new sap.m.ObjectAttribute({
                    				text : "{SupplierName}"
                    		})],
                firstStatus : new sap.m.ObjectStatus({
                                    text : "{LifeCycleStatusName}"
                				})
			});
		  
			var jsonModel = new sap.ui.model.json.JSONModel();
		  	jsonModel.setData(data);
		  	this.getView().setModel(jsonModel);
			masterList.bindItems("/d/results",objectTemplate);
			
		},
		
		listUpdateFinished: function(){
			
			this.getView().byId("detailPageId").setVisible(false);  
		},
		
		handleListItemPress: function(event){
			
			pdfURL = "/espm-cloud-web/espm.svc/CmisRead?objectId="+event.getSource().getBindingContext().getObject("InvoiceLink");
			
			
			this.getView().byId("detailPageId").setVisible(true);
			
			var context = event.getSource().getBindingContextPath();
			
			this.getView().byId("detailObjectHeader").bindElement(context);
			
			var objectStatus = new sap.m.ObjectStatus({
				text : event.getSource().getFirstStatus().getText()
			});
			
			this.getView().byId("detailObjectHeader").setFirstStatus(objectStatus);
			
			this.getView().byId("customerForm").bindElement(context+"/Customer");
			
			var oTemplate = new sap.m.ColumnListItem({
				cells:[
					new sap.m.ObjectIdentifier({
						title : "{ProductId}"
					}),
					new sap.m.Text({
						text : "{path: 'DeliveryDate', formatter: 'com.sap.espm.shop.model.format.date'}"
					}),
					new sap.m.Text({
						text : "{path: 'Quantity', formatter: 'com.sap.espm.shop.model.format.quantity'}"
					}),
					new sap.m.ObjectNumber({
						emphasized : false,
						number : "{path: 'GrossAmount', formatter:'com.sap.espm.shop.model.format.formatAmount'}",
						unit : "{CurrencyCode}"
					})
					]
			});
			
			var oTable = this.getView().byId("lineItemsId");
			var bindString = context + "/SalesOrderItems/results";
			oTable.bindItems(bindString, oTemplate);

			if(this.getView().getModel("device").oData.isPhone){
				this.byId("splitContId").to(this.byId("detailPageId"));
			}
			
		},
		
		handleSearch : function (evt) 
		{ // create model filter 
			
			var filters = []; 
			var query = evt.getParameter("query"); 
			if (query && query.length > 0) { 
				var filter = new sap.ui.model.Filter("SalesOrderId", sap.ui.model.FilterOperator.Contains, query); 
				filters.push(filter); } // update list binding 
			var list = this.getView().byId("list"); 
			var binding = list.getBinding("items"); 
			binding.filter(filters); 
			
		},
		handleDownload: function(){
			window.open(pdfURL);
		},
		onNavBack: function(){
			window.history.go(-1);
		},
		handleNavButtonPress: function(){
    		var oSplitCont = this.byId("splitContId");
    		var oMaster = oSplitCont.getMasterPages()[0];
    		oSplitCont.toMaster(oMaster);
		}

	});

});
