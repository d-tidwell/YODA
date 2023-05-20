package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.CreatePHRRequest;
import com.nashss.se.yodaservice.activity.results.CreatePHRResult;
import com.nashss.se.yodaservice.dynamodb.PHRDAO;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import com.nashss.se.yodaservice.dynamodb.ProviderDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
public class CreatePHRActivity{

    private final Logger log = LogManager.getLogger();

    private final PatientDAO patientDAO;

    private final ProviderDAO providerDAO;

    private final PHRDAO phrdao;

    @Inject
    public CreatePHRActivity(PatientDAO patientDAO, ProviderDAO providerDAO, PHRDAO phrdao) {
        this.patientDAO = patientDAO;
        this.providerDAO = providerDAO;
        this.phrdao = phrdao;
    }

    public CreatePHRResult handleRequest(final CreatePHRRequest request){

        return CreatePHRResult.builder()
                .build();
    }
}