package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.RemovePatientFromProviderRequest;
import com.nashss.se.yodaservice.activity.results.RemovePatientFromProviderResult;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import com.nashss.se.yodaservice.dynamodb.ProviderDAO;
import com.nashss.se.yodaservice.dynamodb.models.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.Deque;

public class RemovePatientFromProviderActivity {

    private final Logger log = LogManager.getLogger();

    private final PatientDAO patientDAO;

    private final ProviderDAO providerDAO;

    @Inject
    public RemovePatientFromProviderActivity(PatientDAO patientDAO, ProviderDAO providerDAO) {
        this.patientDAO = patientDAO;
        this.providerDAO = providerDAO;
    }

    public RemovePatientFromProviderResult handleRequest(final RemovePatientFromProviderRequest request) {
        patientDAO.getPatient(request.getPatientId());
        Provider provider = providerDAO.getProvider(request.getProviderName());
        Deque<String> q = provider.getPendingPatients();
        q.remove(request.getPatientId());
        provider.setPendingPatients(q);
        boolean success = providerDAO.updatePending(provider);
        return RemovePatientFromProviderResult.builder()
                .withSuccess(success)
                .build();
    }
}
