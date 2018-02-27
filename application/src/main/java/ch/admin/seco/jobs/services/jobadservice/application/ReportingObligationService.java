package ch.admin.seco.jobs.services.jobadservice.application;

import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionCodeType;

public interface ReportingObligationService {

    boolean hasReportingObligation(ProfessionCodeType professionCodeType, String professionCode, String cantonCode);

}
