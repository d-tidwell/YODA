package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.GetAllPHRRequest;
import com.nashss.se.yodaservice.activity.results.GetAllPHRResult;
import com.nashss.se.yodaservice.dynamodb.PHRDAO;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import com.nashss.se.yodaservice.dynamodb.ProviderDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

public class GetAllPHRActivity{

    private final Logger log = LogManager.getLogger();

    private final PatientDAO patientDAO;

    private final ProviderDAO providerDAO;

    private final PHRDAO phrdao;

    @Inject
    public GetAllPHRActivity(PatientDAO patientDAO, ProviderDAO providerDAO, PHRDAO phrdao) {
        this.patientDAO = patientDAO;
        this.providerDAO = providerDAO;
        this.phrdao = phrdao;
    }

    public GetAllPHRResult handleRequest(final GetAllPHRRequest request){

        return GetAllPHRResult.builder()
                .build();
    }
}
