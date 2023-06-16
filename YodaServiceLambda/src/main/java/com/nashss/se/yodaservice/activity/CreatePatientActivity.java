package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.CreatePatientRequest;
import com.nashss.se.yodaservice.activity.results.CreatePatientResult;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import com.nashss.se.yodaservice.dynamodb.models.Patient;
import com.nashss.se.yodaservice.utils.RandoAgeGenerator;
import com.nashss.se.yodaservice.utils.UUIDGenerator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

public class CreatePatientActivity {

    private final Logger log = LogManager.getLogger();

    private final PatientDAO patientDAO;

    @Inject
    public CreatePatientActivity(PatientDAO patientDAO) {
        this.patientDAO = patientDAO;
    }

    public CreatePatientResult handleRequest(final CreatePatientRequest request) {
        String newPatientId = request.getPatientName() + UUIDGenerator.generateUniqueId();
        Patient madePatient = new Patient();
        madePatient.setPatientId(newPatientId);
        madePatient.setName(request.getPatientName());
        madePatient.setAge(request.getPatientAge());
        boolean confirmation = patientDAO.savePatient(madePatient);
        return CreatePatientResult.builder()
                .withSuccess(confirmation)
                .build();
    }
}
