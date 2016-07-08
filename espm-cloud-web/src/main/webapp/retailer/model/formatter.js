sap.ui.define([
	"sap/ui/core/format/NumberFormat",
	"sap/ui/core/format/DateFormat"
], function(NumberFormat, DateFormat) {
	"use strict";

	var fnAmountFormatter = NumberFormat.getCurrencyInstance();
	var fnQuantityFormatter = NumberFormat.getFloatInstance({
		maxFractionDigits : 0,
		minFractionDigits : 0,
		groupingEnabled : false,
	});

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

		formatQuantity : function(quantity) {
			if (quantity !== 0 && !quantity) {
				return;
			}
			return fnQuantityFormatter.format(quantity);
		},
		/**
		 * Returns a configuration object for the {@link sap.ushell.ui.footerbar.AddBookMarkButton} "appData" property
		 * @public
		 * @param {string} sTitle the title for the "save as tile" dialog
		 * @returns {object} the configuration object
		 */
		formatImage : function(value) {
			if (value === false) {
				return 'images/green_tick.png';
			} else {
				return 'images/red_cross.png';
			}
		},
		formatShareTileData: function(sTitle) {
			return {
				title: sTitle
			};
		},
		
		_statusStateMap : { 
			"P" : "Success", 
			"N" : "Warning" ,
			"X" : "Rejected"
		},
		statusText : function (value) {
			var bundle = this.getModel("i18n").getResourceBundle(); 
			return bundle.getText("StatusText" + value, "?"); 
		},
		statusState : function (value) { 
			var map = _statusStateMap; 
			return (value && map[value]) ? map[value] : "None"; 
		}, 
		date : function (value) { 
			if (value) { 
				var oDateFormat = sap.ui.core.format.DateFormat.getDateTimeInstance({pattern: "yyyy-MM-dd"}); 
				return oDateFormat.format(new Date(value)); 
				} 
			else {
				return value; 
				}
		}

	};
});