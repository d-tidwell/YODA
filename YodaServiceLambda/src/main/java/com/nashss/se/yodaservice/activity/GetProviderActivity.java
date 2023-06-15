package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.GetProviderRequest;
import com.nashss.se.yodaservice.activity.results.GetProviderResult;
import com.nashss.se.yodaservice.dynamodb.ProviderDAO;
import com.nashss.se.yodaservice.dynamodb.models.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Optional;
import javax.inject.Inject;



public class GetProviderActivity {

    private final Logger log = LogManager.getLogger();
    private final ProviderDAO providerDAO;

    @Inject
    public GetProviderActivity(ProviderDAO providerDAO) {
        this.providerDAO = providerDAO;
    }

    public GetProviderResult handleRequest(final GetProviderRequest request) {
        Optional<Provider> providerOpt = providerDAO.getProvider(request.getProviderName());
    
        if (providerOpt.isPresent()) {
            Provider provider = providerOpt.get();
            return GetProviderResult.builder()
                    .withName(provider.getName())
                    .withMedicalSpecialty(provider.getMedicalSpecialty())
                    .withPendingPatients(new ArrayList<>(provider.getPendingPatients()))
                    .build();
        } else {
            // Return a GetProviderResult with null values if no provider was found
            return GetProviderResult.builder()
                    .withName(null)
                    .withMedicalSpecialty(null)
                    .withPendingPatients(null)
                    .build();
        }
    }
}