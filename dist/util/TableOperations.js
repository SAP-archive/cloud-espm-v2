sap.ui.define([
	"sap/ui/base/Object",
	"sap/ui/model/Sorter",
	"sap/ui/model/Filter",
	"sap/ui/model/FilterType",
	"sap/ui/model/FilterOperator"
], function(Object, Sorter, Filter, FilterType, FilterOperator) {
	"use strict";

	return Object.extend("com.sap.e.shop.util.TableOperations", {
		// Object holding the active sorters of a list. It is used to make sure that
		// setting a new sorter with "sort list" does not break a sorting that was
		// previously set by "grouping".
		// When the list is sorted or grouped the list of sorters that is applied to
		// the binding is built by concatenating oGrouper and aSortList of this
		// object into one array.
		// Sorting and grouping is done with the following rules:
		// 1. selecting a sorter on the table adds the new sorter as the main sorter
		// to all existing sorters
		// 2. if grouping and sorting are both set for the same attribute then the
		// direction (ascending/descending) has to be aligned
		// The search related attributes are public because there is no special
		// logic for setting them so they can be used directly.

		constructor: function(oTable, aSearchableFields, oDefaultSorter) {
			// Storage of the active grouping and sorting is private because
			// of their interdependency
			var sSearchTerm = "",
				oGrouper = null,
				aFilterList = [],
				aSearchFilter = [],
				bGroupingChanged = false,
				bSearchChanged = false,
				bFilterChanged = false,
				bSortChanged = false,
				aSortList = [(oDefaultSorter) ? oDefaultSorter : new Sorter("Name", false)],
				oFilterDict = {};

			this._resetChangeIndicators = function() {
				bGroupingChanged = false;
				bSearchChanged = false;
				bFilterChanged = false;
				bSortChanged = false;
			};

			this.addSorter = function(oSorter) {
				// Delete any existing sorter for the path specified
				var i = this._getSortListIndexByPath(oSorter.sPath);
				if (i !== -1) {
					aSortList.splice(i, 1);
				}
				// The latest sorter is always the "main" sorter -> add it to the
				// beginning of the array
				aSortList.unshift(oSorter);
				// Copy the sort order of the new sorter to the grouper if they
				// refer to the same path
				if (oGrouper && oGrouper.sPath === oSorter.sPath) {
					oGrouper.bDescending = oSorter.bDescending;
				}
				bSortChanged = true;
			};
			this.setGrouping = function(oNewGrouper) {
				// If there is already a sorter for the path specified, the sorting order
				// must be the same as in the new grouper
				var i = this._getSortListIndexByPath(oNewGrouper.sPath);
				if (i !== -1) {
					aSortList[i].bDescending = oNewGrouper.bDescending;
				}
				oGrouper = oNewGrouper;
				bGroupingChanged = true;
			};

			this._getSortListIndexByPath = function(sPath) {
				var i;
				for (i = 0; i < aSortList.length; i++) {
					if (aSortList[i].sPath === sPath) {
						return i;
					}
				}
				return -1;
			};
			this.removeGrouping = function() {
				oGrouper = null;
				bGroupingChanged = true;
			};
			this.getGrouping = function() {
				return oGrouper;
			};
			this.getSorter = function() {
				return aSortList;
			};
			this.resetFilters = function() {
				aFilterList.length = 0;
				oFilterDict = {};
				bFilterChanged = true;
			};
			this.addSFBFilters = function(oSFBFilters) {
				var sFilterOperation = "";
				for (var prop in oSFBFilters) {
					//create the filter an add it to the active filters by calling addFilter
					if (oSFBFilters[prop].ranges) {
						for (var i = 0; i < oSFBFilters[prop].ranges.length; i++) {
							sFilterOperation = oSFBFilters[prop].ranges[i].operation;
							//when single values are excluded the operator needs to be switched to "not equal"
							sFilterOperation = (sFilterOperation === FilterOperator.EQ && oSFBFilters[prop].ranges[i].exclude) ? FilterOperator.NE :
								sFilterOperation;
							this.addFilter(new Filter(oSFBFilters[prop].ranges[i].keyField, sFilterOperation, oSFBFilters[prop].ranges[
									i].value1, oSFBFilters[prop].ranges[i].value2),
								oSFBFilters[prop].ranges[i].keyField);
						}
					}
					if (oSFBFilters[prop].items) {
						for (var j = 0; j < oSFBFilters[prop].items.length; j++) {
							this.addFilter(new Filter(prop, FilterOperator.EQ, oSFBFilters[prop].items[j].key),
								prop);
						}
					}
				}
			};
			this.addFilter = function(oFilter, sFilterAttribute) {
				// This function creaets one list containing the filters for all attributes. In this list there is
				// one multiFilter for each attribute. The multiFilters contain two separate lists for including
				// and excluding filters. The including filters for one attribute are connect by the "or"
				// operator and the exluding filters are connect by the "and" operator. The two lists are connected
				// by the "and operator"
				if (oFilter && sFilterAttribute) {
					if (oFilterDict[sFilterAttribute]) {
						// there is already at least one filter for this attribute -> add the new filter to the
						// appropriate list depending on its filter operator
						// currently "not equal" is the only possible excluding filter operator
						if (oFilter.sOperator === FilterOperator.NE) {
							oFilterDict[sFilterAttribute].excludingFilters.push(oFilter);
						} else {
							oFilterDict[sFilterAttribute].includingFilters.push(oFilter);
						}
					} else {
						// there is no filter for this attribute yet -> add the new filter attribute to the dictionary
						oFilterDict[sFilterAttribute] = {
							includingFilters: [],
							excludingFilters: []
						};
						// currently "not equal" is the only possible excluding filter operator
						if (oFilter.sOperator === FilterOperator.NE) {
							oFilterDict[sFilterAttribute].excludingFilters = [oFilter];
						} else {
							oFilterDict[sFilterAttribute].includingFilters = [oFilter];
						}
					}
					// now merge the filters for all attributes into one list
					aFilterList.length = 0;
					for (var prop in oFilterDict) {
						if (oFilterDict.hasOwnProperty(prop)) {
							var aFilterListForProp = [];
							if (oFilterDict[prop].includingFilters.length > 0) {
								aFilterListForProp.push(new Filter(oFilterDict[prop].includingFilters, false));
							}
							if (oFilterDict[prop].excludingFilters.length > 0) {
								aFilterListForProp.push(new Filter(oFilterDict[prop].excludingFilters, true));
							}
							aFilterList.push(new Filter(aFilterListForProp, true));
						}
					}
					bFilterChanged = true;
				}
			};

			this.getFilterTable = function() {
				return (aFilterList && aFilterList.length > 0) ? aFilterList : null;
			};

			this.setSearchTerm = function(sNewSearchTerm) {
				// Searching may be done in more than one column - therefore a filter for
				// each of the searchable columns has to be created
				aSearchFilter.length = 0;
				if (sNewSearchTerm && sNewSearchTerm.length > 0) {
					sSearchTerm = sNewSearchTerm;
					for (var i = 0; i < aSearchableFields.length; i++) {
						aSearchFilter.push(new Filter(aSearchableFields[i], FilterOperator.Contains, sNewSearchTerm));
					}
				} else {
					//the search term is empty -> remove the search
					sSearchTerm = "";
					aSearchFilter = [];
				}
				bSearchChanged = true;
			};

			this.getSearchTerm = function() {
				return sSearchTerm;
			};

			this.applyTableOperations = function() {
				// Here the binding of the table items is updated with the currently active sorters and filters.
				// It is assumed that all changes done by the user are immediately reflected in the table.
				// That means there is always just one change at a time.
				var aActiveSorters = [],
					aActiveFilters = [],
					oTableBinding = oTable.getBinding("items");

				if (oTableBinding) {
					if (bGroupingChanged || bSortChanged) {
						// The grouping or sorting of the table has changed. The sorting on the binding needs to be updated.
						// Note that the sorter of the grouping has to be the first one in the list of sorters that is added
						// to the binding
						if (oGrouper) {
							aActiveSorters.push(oGrouper);
						}
						if (aSortList.length > 0) {
							aActiveSorters = aActiveSorters.concat(aSortList);
						}
						oTableBinding.sort(aActiveSorters);
					}
					if (bSearchChanged || bFilterChanged) {
						//the filters that origin from entries in a sarch field and the ones that are set e.g. by a
						// filter bar need to be applied together.
						// Note that if the search is done in more than one column then the corresponding filters have
						// to be connected using "or". All other filters are connected using "and" logic.

						if (aSearchFilter.length > 0) {
							aActiveFilters.push(new Filter(aSearchFilter, false));
						}
						if (aFilterList.length > 0) {
							aActiveFilters.push(new Filter(aFilterList, true));
						}
						if (aActiveFilters.length > 0) {
							oTableBinding.filter(new Filter(aActiveFilters, true));
						} else {
							oTableBinding.filter();
						}
					}
					this._resetChangeIndicators();
				}
			};
		}
	});
});