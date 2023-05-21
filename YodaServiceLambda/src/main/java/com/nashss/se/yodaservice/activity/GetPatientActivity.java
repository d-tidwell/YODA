package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.GetPatientRequest;
import com.nashss.se.yodaservice.activity.results.GetPatientResult;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

public class GetPatientActivity {
    private final Logger log = LogManager.getLogger();
    private final PatientDAO patientDAO;

    @Inject
    public GetPatientActivity(PatientDAO patientDAO) {
        this.patientDAO = patientDAO;
    }

    public GetPatientResult handleRequest(final GetPatientRequest request){

        return GetPatientResult.builder()
                .build();
    }
}
