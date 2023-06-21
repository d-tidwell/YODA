package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.AddPatientToProviderRequest;
import com.nashss.se.yodaservice.activity.results.AddPatientToProviderResult;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import com.nashss.se.yodaservice.dynamodb.ProviderDAO;
import com.nashss.se.yodaservice.dynamodb.models.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.inject.Inject;



public class AddPatientToProviderActivity {

    private final Logger log = LogManager.getLogger();

    private final PatientDAO patientDAO;

    private final ProviderDAO providerDAO;

    @Inject
    public AddPatientToProviderActivity(PatientDAO patientDAO, ProviderDAO providerDAO) {
        this.patientDAO = patientDAO;
        this.providerDAO = providerDAO;
    }

    public AddPatientToProviderResult handleRequest(final AddPatientToProviderRequest request) {
        if (request == null || request.getPatientId() == null || request.getProviderName() == null) {
            throw new IllegalArgumentException("Invalid request or request parameters");
        }

        // Verify that the patient exists
        patientDAO.getPatient(request.getPatientId());

        Provider provider = providerDAO.getProvider(request.getProviderName()).get();


        List<String> pendingPatients = provider.getPendingPatients();
        pendingPatients.add(request.getPatientId());
        provider.setPendingPatients(pendingPatients);

        log.info("Successful Add Patient to Provider");

        boolean success = providerDAO.updateProvider(provider);

        return AddPatientToProviderResult.builder()
                .withSuccess(success)
                .build();
    }

