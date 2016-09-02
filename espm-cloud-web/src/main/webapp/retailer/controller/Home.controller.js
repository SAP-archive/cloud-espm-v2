sap.ui.define([
	"sap/ui/core/mvc/Controller"
], function(Controller) {
	"use strict";

	return Controller.extend("com.sap.espm.retailer.controller.Home", {
		
		onInit : function () {
			// set mock model
			
/*			var oModel = new JSONModel("/espm-cloud-web/retailer/model/tileData.json");
			this.getView().setModel(oModel);*/
		},
		onRoleButtonPressed: function(){
			
			var userId;
			var bundle = this.getView().getModel("i18n").getResourceBundle();
			var dialog = new sap.m.Dialog({
				id:"roleDialogId",
				title: bundle.getText("home.roleTitle"),
				type: "Message",
				content: [
					new sap.ui.layout.form.SimpleForm({
						content:[
						        new sap.m.Label({text:bundle.getText("home.roleUserName")}),
								new sap.m.Input({
									id:"userInputId",
									value:"",
									liveChange:function(oEvent){
										userId = oEvent.getSource().getValue();
									}
								})
						         
						      ]
					})
				],
				beginButton: new sap.m.Button({
					text: bundle.getText("home.grantRole"),
					press: function () {
						
						
						if(userId){
							
							dialog.close();
						/*	var userIdJson = {
									"userId":userId
								};*/
							var userIdJson = {
									 "users": [
									           {
									             "name" : userId
									           }
									           ]
								};
								
								$.ajax({
					            type: "POST",
					            async: true,
					            contentType:"application/json; charset=utf-8",
					            dataType: "json",
					            url: "/espm-cloud-web/grantUserRole?roleName=Retailer",
					            data : JSON.stringify(userIdJson),
					            success: function() {
					            	sap.m.MessageToast.show(bundle.getText("home.AssignRoleSuccess"));
					            		//sap.m.MessageToast.show("success");
					            },
					            error: function(err) {
					            	if(err.status === 200){
					            		sap.m.MessageToast.show(bundle.getText("home.AssignRoleSuccess"));
					            	}
					            	else{
					            		sap.m.MessageToast.show(bundle.getText("home.AssignRoleFailed"));
					            	}
					                
					            }
					        	});
						}
						else{
							sap.m.MessageToast.show(bundle.getText("home.UserNameRequest"));
						}
						
					}
				}),
				endButton: new sap.m.Button({
					text: bundle.getText("home.cancel"),
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
		
		approveTilePressed: function(){
			
			var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
			oRouter.navTo("SalesOrder");
		},
		stockTilePressed: function(){
			
			var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
			oRouter.navTo("StockInformation");
		}

	});

});