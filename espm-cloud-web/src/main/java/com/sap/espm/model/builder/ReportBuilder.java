package com.sap.espm.model.builder;

import java.util.List;

public interface ReportBuilder<INPUT, RETURNTYPE> {

	RETURNTYPE generateReportData(List<INPUT> inputList);

}
