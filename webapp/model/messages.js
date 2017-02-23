sap.ui.define([
	"sap/m/MessageBox",
	"sap/ui/model/json/JSONModel"
], function(MessageBox, JSONModel) {
	"use strict";

	var fnParseError = function(oParameter) {
		var sMessage = "",
			sDetails = "",
			oParameters = null,
			oError = {};
		oParameters = oParameter.getParameters ? oParameter.getParameters() : oParameter;
		sMessage = oParameters.message || oParameters.response.message;
		sDetails = oParameters.responseText || oParameters.response.responseText;

		if (jQuery.sap.startsWith(sDetails || "", "{\"error\":")) {
			var oErrModel = new JSONModel();
			oErrModel.setJSON(sDetails);
			sMessage = oErrModel.getProperty("/error/message/value");
		}

		oError.sDetails = sDetails;
		oError.sMessage = sMessage;
		return oError;
	};

	return {
		// Show an error dialog with information from the oData response object.
		// oParameter - The object containing error information
		showErrorMessage: function(oParameter, sTitle) {
			var oErrorDetails = fnParseError(oParameter);
			MessageBox.show(oErrorDetails.sMessage, {
				icon: MessageBox.Icon.ERROR,
				title: sTitle,
				details: oErrorDetails.sDetails,
				actions: MessageBox.Action.CLOSE,
				onClose: null
			});
		}
	};
});