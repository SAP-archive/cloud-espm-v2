jQuery.sap.require("com.sap.espm.shop.model.format");
sap.ui.define([
	"sap/ui/core/mvc/Controller",
	"com/sap/espm/shop/model/formatter"
], function(Controller, formatter) {
	"use strict";

	return Controller.extend("com.sap.espm.shop.controller.Shoppingcart", {
		
		formatter: formatter,
		onInit:function()
		{
			
			
			var that = this;
			
			this._oView = this.getView();
			this._oTotalFooter = this.byId("totalFooter");
			
			var oTemplate = new sap.m.ColumnListItem({
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
					new sap.m.Input({
						
						id : "quantityInput",
						maxLength : 3,
						textAlign : "End",
						type : "Number",
						value : "{path: 'Quantity', type: 'sap.ui.model.type.Integer'}",
						change:function(oEvent){
							that.onQuantityChanged(oEvent);
						}
					}),
					new sap.m.ObjectNumber({
						emphasized : false,
						id : "priceObjNumber",
						number : "{path: 'Price', formatter:'com.sap.espm.shop.model.format.formatAmount'}",
						unit : "{CurrencyCode}"
					}),
					new sap.m.ObjectNumber({
						id : "totalObjNumber",
						number : "{path: 'Total', formatter:'com.sap.espm.shop.model.format.formatAmount'}",
						unit : "{CurrencyCode}"
					})
					]
			});
			var oComponent = this.getOwnerComponent();
			var model = oComponent.getModel("Cart");
			var oTable = this.getView().byId("shoppingCartTable");
			oTable.setModel(model);
			oTable.bindItems("/ShoppingCart",oTemplate);
			this.getView().addEventDelegate({
				onAfterShow: function() {
					that.onAddCountToCart();
				}});
			
			
		},
		onAfterRendering: function() 
		{
			
		},
		onBeforeRendering: function() 
		{
 		},
		onLineItemPressed: function()
		{
			
		},
		
		onQuantityChanged: function(oEvent){
			
			var model=this.getView().getModel("Cart");
			var path = oEvent.getSource().getParent().getBindingContextPath();
			var data=model.getProperty(path);
			data.Total = data.Quantity * data.Price;
			this.onAddCountToCart();
			model.refresh();
			
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
			}
			else{
				this._oTotalFooter.setNumber("");
				this._oTotalFooter.setUnit("");
			}
			
			
			
		},
		onDeletePressed: function(oEvent){
			
			var m = oEvent.getParameter("listItem");
		    var idx = m.getBindingContextPath();
			idx = idx.charAt(idx.lastIndexOf('/')+1);
			if (idx !== -1) {
				var oModel = this.getView().getModel("Cart");             
				var data = oModel.getProperty('/ShoppingCart');
				data.splice(idx,1);
				oModel.setData({ShoppingCart : data});
				//oModel.refresh();
				
			}
			this.onAddCountToCart();
			
		},
		onCheckoutButtonPressed: function(){
			
			var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
			oRouter.navTo("Checkout");
		}

		

	});

});