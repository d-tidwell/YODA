package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.AddPatientToProviderRequest;
import com.nashss.se.yodaservice.activity.results.AddPatientToProviderResult;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import com.nashss.se.yodaservice.dynamodb.ProviderDAO;
import com.nashss.se.yodaservice.dynamodb.models.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Deque;
import java.util.List;
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
        patientDAO.getPatient(request.getPatientId());
        Optional<Provider> provider = providerDAO.getProvider(request.getProviderName());
        List<String> q = provider.get().getPendingPatients();
        q.add(request.getPatientId());
        provider.get().setPendingPatients(q);
        boolean success = providerDAO.updateProvider(provider.get());
        return AddPatientToProviderResult.builder()
                .withSuccess(success)
                .build();
    }
}
