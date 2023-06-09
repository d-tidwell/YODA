package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.CreatePatientRequest;
import com.nashss.se.yodaservice.activity.results.CreatePatientResult;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import com.nashss.se.yodaservice.dynamodb.models.Patient;
import com.nashss.se.yodaservice.utils.Sanitizer;
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
        String patientName = request.getPatientName().replaceAll("\\s+","");
        System.out.println("CREATE REQUEST ISSUEE !!!!!!!!!!!!!!!!!!!!");
        System.out.println(patientName);
        System.out.println(request.getPatientName());
        System.out.println(request.getAddress());
        System.out.println(request.getPatientAge());
        System.out.println(request.getPhoneNumber());
        madePatient.setName(Sanitizer.sanitizeField(patientName));
        madePatient.setAge(Sanitizer.sanitizeField(request.getPatientAge()));
        madePatient.setSex( Sanitizer.sanitizeField(request.getSex()));
        madePatient.setAddress(Sanitizer.sanitizeField(request.getAddress()));
        madePatient.setPhoneNumber(Sanitizer.sanitizeField(request.getPhoneNumber()));
        
        boolean confirmation = patientDAO.savePatient(madePatient);

        return CreatePatientResult.builder()
                .withSuccess(confirmation)
                .build();
    }
}
