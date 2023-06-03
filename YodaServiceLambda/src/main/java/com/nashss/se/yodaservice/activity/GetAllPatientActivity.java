package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.results.GetAllPatientResult;
import com.nashss.se.yodaservice.converters.ModelConverter;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;

import javax.inject.Inject;

public class GetAllPatientActivity {

    private final PatientDAO patientDAO;

    @Inject
    public GetAllPatientActivity(PatientDAO patientDAO) {
        this.patientDAO = patientDAO;
    }

    public GetAllPatientResult handleRequest() {
        return GetAllPatientResult.builder()
                .withPatients(ModelConverter.convertListPatientoModels(patientDAO.getAllPatients()))
                .build();
    }
}
