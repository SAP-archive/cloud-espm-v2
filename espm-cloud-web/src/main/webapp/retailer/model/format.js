jQuery.sap.declare("com.sap.espm.retailer.model.format");
jQuery.sap.require("sap.ui.core.format.DateFormat");
jQuery.sap.require("sap.ui.core.format.NumberFormat");

com.sap.espm.retailer.model.format = { 
		_statusStateMap : { 
			"P" : "Success", 
			"N" : "Warning",
			"X" : "Error"
		},
		statusText : function (value) { 
			var bundle = this.getModel("i18n").getResourceBundle(); 
			return bundle.getText("StatusText" + value, "?"); 
		}, 
			
		statusState : function (value) { 
			var map = com.sap.espm.retailer.model.format._statusStateMap; 
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
		},
		quantity : function (value) { 
			try { 
				return (value) ? parseFloat(value).toFixed(0) : value; 
				} 
			catch (err) { 
				return "Not-A-Number"; 
				} 
		},
		
		formatAmount: function(fAmount) {
			if (!fAmount) {
				return "";
			}
			var numFormat = sap.ui.core.format.NumberFormat.getCurrencyInstance();
			return numFormat.format(fAmount);
		}
			
};