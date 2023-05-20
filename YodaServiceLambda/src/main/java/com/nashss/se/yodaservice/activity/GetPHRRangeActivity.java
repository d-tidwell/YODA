package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.GetPHRRangeRequest;
import com.nashss.se.yodaservice.activity.results.GetPHRRangeResult;
import com.nashss.se.yodaservice.dynamodb.PHRDAO;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import com.nashss.se.yodaservice.dynamodb.ProviderDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

public class GetPHRRangeActivity{

    private final Logger log = LogManager.getLogger();

    private final PatientDAO patientDAO;

    private final ProviderDAO providerDAO;

    private final PHRDAO phrdao;

    @Inject
    public GetPHRRangeActivity(PatientDAO patientDAO, ProviderDAO providerDAO, PHRDAO phrdao) {
        this.patientDAO = patientDAO;
        this.providerDAO = providerDAO;
        this.phrdao = phrdao;
    }

    public GetPHRRangeResult handleRequest(final GetPHRRangeRequest request){

        return GetPHRRangeResult.builder()
                .build();
    }
}

