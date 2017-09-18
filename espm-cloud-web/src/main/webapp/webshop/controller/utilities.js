sap.ui.define([
	"sap/ui/Device"
], function(Device) {
	"use strict";

	var sCompactCozyClass = Device.support.touch ? "" : "sapUiSizeCompact";

	return {
		getContentDensityClass: function() {
			return sCompactCozyClass;
		},

		attachControl: function(oView, oControl) {
			if (sCompactCozyClass) {
				jQuery.sap.syncStyleClass(sCompactCozyClass, oView, oControl);
			}
			oView.addDependent(oControl);
		}
	};
});