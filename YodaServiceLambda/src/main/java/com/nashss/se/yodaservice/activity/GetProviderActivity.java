package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.GetProviderRequest;
import com.nashss.se.yodaservice.activity.results.GetProviderResult;
import com.nashss.se.yodaservice.dynamodb.ProviderDAO;
import com.nashss.se.yodaservice.dynamodb.models.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import javax.inject.Inject;



public class GetProviderActivity {

    private final Logger log = LogManager.getLogger();
    private final ProviderDAO providerDAO;

    @Inject
    public GetProviderActivity(ProviderDAO providerDAO) {
        this.providerDAO = providerDAO;
    }

    public GetProviderResult handleRequest(final GetProviderRequest request) {
        Provider provider = providerDAO.getProvider(request.getProviderId());
        return GetProviderResult.builder()
                .withName(provider.getName())
                .withMedicalSpecialty(provider.getMedicalSpecialty())
                .withPendingPatients(new ArrayList<>(provider.getPendingPatients()))
                .build();
    }
}
