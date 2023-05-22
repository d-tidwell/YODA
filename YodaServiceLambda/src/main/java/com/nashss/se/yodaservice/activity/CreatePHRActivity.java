package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.CreatePHRRequest;
import com.nashss.se.yodaservice.activity.results.CreatePHRResult;
import com.nashss.se.yodaservice.dynamodb.PHRDAO;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import com.nashss.se.yodaservice.dynamodb.ProviderDAO;
import com.nashss.se.yodaservice.dynamodb.models.PHR;
import com.nashss.se.yodaservice.enums.PHRStatus;
import com.nashss.se.yodaservice.utils.UUIDGenerator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
public class CreatePHRActivity {

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

    public CreatePHRResult handleRequest(final CreatePHRRequest request) {
        patientDAO.getPatient(request.getPatientId());
        providerDAO.getProvider(request.getProviderName());
        String phrId = request.getPatientId() + "_" + request.getDate() + "_" + UUIDGenerator.generateUniqueId();
        PHR newPHR = new PHR();
        newPHR.setPhrId(phrId);
        newPHR.setPatientId(request.getPatientId());
        newPHR.setProviderName(request.getProviderName());
        newPHR.setDate(request.getDate());
        newPHR.setStatus(PHRStatus.CREATED.toString());
        boolean status = phrdao.savePHR(newPHR);
        return CreatePHRResult.builder()
                .withstatus(String.valueOf(status))
                .build();
    }
}

