sap.ui.define([
	"sap/ui/core/format/NumberFormat"
], function(NumberFormat) {
	"use strict";

	var fnAmountFormatter = NumberFormat.getCurrencyInstance();

	return {
		// Formatter for Helpful link - Returns concatenated string with Text and Helpful Count
		formatHelpfulCount: function(iHelpfulCount) {
			return this.getModel().getProperty("/#Review/HelpfulForCurrentUser/@sap:label") + " (" + iHelpfulCount + ")";
		},

		// Formatter for Availability - Displays text or text + number
		formatAvailabilityText: function(iAvailability) {
			var oResourceBundle = this.getModel("i18n").getResourceBundle();
			if (isNaN(iAvailability) || iAvailability < 1) {
				return oResourceBundle.getText("xfld.outOfStock");
			}
			if (iAvailability < 10) {
				return oResourceBundle.getText("xfld.inStockLeft", [iAvailability]);
			}
			return oResourceBundle.getText("xfld.inStock");
		},

		// Formatter for Measures - Returns concatenated string with measure and unit
		formatMeasure: function(fMeasure, sUnit) {
			if (fMeasure && sUnit) {
				var oResourceBundle = this.getModel("i18n").getResourceBundle();
				return oResourceBundle.getText("xfld.formatMeasure", [fMeasure, sUnit]);
			}
			return "";
		},

		// Formatter for amount
		formatAmount: function(fAmount) {
			if (!fAmount) {
				return "";
			}
			return fnAmountFormatter.format(fAmount);
		},
		formatRating: function(fRating){
			if(!fRating){
				return "";
			}
			var value = parseFloat(fRating);
			return value;
		},
		onAddToCart: function(oModel, productContext){
			
			var aData = oModel.getProperty("/ShoppingCart");
			sap.m.MessageToast.show(productContext.Name +" " + " added to the cart");
			
			for(var i=0; i<aData.length; i++){
					var prodId = aData[i];
					if(prodId.ProductId === productContext.ProductId){
						prodId.Quantity += 1;//prodId.Quantity++;
						prodId.Total = prodId.Quantity * prodId.Price;
						oModel.setData({ShoppingCart : aData});
						return;
					}
				}
				productContext.Quantity = 1;
				productContext.Total = productContext.Quantity * productContext.Price;
				aData.push(productContext);
				oModel.setData({ShoppingCart : aData});	

		},
		onAddCountToCart: function(oModel){
			
				var totalQuantity = 0;
				
				var data = oModel.getProperty("/ShoppingCart");
				for(var i=0; i<data.length; i++){
						var prodId = data[i];
						totalQuantity += prodId.Quantity;
					}
			return totalQuantity;		
		},

		/**
		 * Returns a configuration object for the {@link sap.ushell.ui.footerbar.AddBookMarkButton} "appData" property
		 * @public
		 * @param {string} sTitle the title for the "save as tile" dialog
		 * @returns {object} the configuration object
		 */
		formatShareTileData: function(sTitle) {
			return {
				title: sTitle
			};
		},
		
		formatCountryName: function(){
			var oResourceBundle = this.getModel("i18n").getResourceBundle();
			return oResourceBundle.getText("sText");
		}

	};
});