sap.ui.define([
	"sap/m/RatingIndicator",
	"sap/m/Link",
	"sap/m/Label",
	"sap/ui/core/Control"
], function(RatingIndicator, Link, Label, Control) {
	"use strict";

	return Control.extend("com.sap.espm.shop.control.RatingAndCount", {
		// The rating indicator and the rating count are combined in one control in order to be able to put
		// them in one table column instead of having to let them occupy one column each.

		// API:
		metadata: {
			properties: {
				"maxRatingValue": "int",
				"value": "float",
				"enabled": "boolean",
				"iconSize": "sap.ui.core.CSSSize",
				"ratingCount": "float",
				"verticalAlignContent": "boolean",
				"verticalAdjustment": "int"
			},

			events: {
				"press": {}
			},

			aggregations: {
				"_ratingCountLink": {
					type: "sap.m.Link",
					multiple: false,
					visibility: "hidden"
				},
				"_ratingCountLabel": {
					type: "sap.m.Label",
					multiple: false,
					visibility: "hidden"
				},
				"_ratingIndicator": {
					type: "sap.m.RatingIndicator",
					multiple: false,
					visibility: "hidden"
				}
			}
		},

		init: function() {
			this._oRating = new RatingIndicator(this.getId() + "-rating");
			this._oRating.setEnabled(false);
			this.setAggregation("_ratingIndicator", this._oRating, true);
			// The decision on whether the rating count is an sap.m.Link or an
			// sap.m.Text can only be made once we know if a press handler is provided
			this._oRatingCountLink = new Link(this.getId() + "-ratingCountLink");
			this.setAggregation("_ratingCountLink", this._oRatingCountLink, true);
			this._oRatingCountLabel = new Label(this.getId() + "-ratingCountLabel");
			this.setAggregation("_ratingCountLabel", this._oRatingCountLabel, true);
		},

		_onclick: function() {
			if (this.getEnabled() === true) {
				this.firePress({
					source: this._oRatingCountLink
				});
			}
		},

		onclick: function() {
			this._onclick();
		},

		onsapspace: function() {
			this._onclick();
		},

		// Overwriting the setter method is done in order to hand down the values to the
		// inner control. The setter method is used by the binding to update the
		// control's value.
		setValue: function(sValue) {
			var fvalue = parseFloat(sValue);
			this._oRating.setValue(fvalue);
			return this.setProperty("value", fvalue, true);
		},

		// Overwriting the setter method is done in order to hand down the values to the
		// inner control. The setter method is used by the binding to update the
		// control's value.
		setMaxRatingValue: function(sMaxRatingValue) {
			this._oRating.setMaxValue(sMaxRatingValue);
			return this.setProperty("maxRatingValue", sMaxRatingValue, true);
		},

		// Overwriting the setter method is done in order to hand down the values to the
		// inner control. The setter method is used by the binding to update the
		// control's value.
		setIconSize: function(sIconSize) {
			this._oRating.setIconSize(sIconSize);
			return this.setProperty("iconSize", sIconSize, true);
		},

		// Overwriting the setter method is done in order to hand down the values to the
		// inner control. The setter method is used by the binding to update the
		// control's value.
		// Note that two controls are potentially affected in this case.
		setRatingCount: function(sRatingCount) {
			if (sRatingCount === undefined || sRatingCount === null) {
				sRatingCount = 0;
			}
			this._oRatingCountLabel.setText("(" + sRatingCount + ")");
			this._oRatingCountLink.setText("(" + sRatingCount + ")");
			return this.setProperty("ratingCount", sRatingCount);
		},

		// creating the HTML:
		renderer: function(oRm, oControl) {
			var oRatingCount = oControl.hasListeners("press") ? oControl.getAggregation("_ratingCountLink") : oControl.getAggregation(
				"_ratingCountLabel");

			if (oControl.getVerticalAdjustment() && oControl.getVerticalAdjustment() !== 0) {
				oRm.addStyle("-ms-transform", "translateY(" + oControl.getVerticalAdjustment() + "%)");
				oRm.addStyle("-webkit-transform", "translateY(" + oControl.getVerticalAdjustment() + "%)");
				oRm.addStyle("transform", "translateY(" + oControl.getVerticalAdjustment() + "%)");
			}
			if (oControl.getVerticalAlignContent()) {
				oRm.addStyle("line-height", oControl.getIconSize());
				oRatingCount.addStyleClass("nwEpmRefappsLibraryRatingAndCountVAlign");
			}

			oRm.write("<div");
			oRm.writeControlData(oControl); // write the Control ID and enable event
			// handling
			oRm.writeStyles();
			oRm.writeClasses();
			oRm.write(">");
			oRm.renderControl(oControl.getAggregation("_ratingIndicator"));
			oRm.renderControl(oRatingCount);
			oRm.write("</div>");
		}
	});
});