package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.GetProviderRequest;
import com.nashss.se.yodaservice.activity.results.GetProviderResult;
import com.nashss.se.yodaservice.dynamodb.ProviderDAO;
import com.nashss.se.yodaservice.dynamodb.models.Provider;
import com.nashss.se.yodaservice.utils.Sanitizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Optional;
import javax.inject.Inject;

public class GetProviderActivity {

    private final ProviderDAO providerDAO;

    @Inject
    public GetProviderActivity(ProviderDAO providerDAO) {
        this.providerDAO = providerDAO;
    }

    public GetProviderResult handleRequest(final GetProviderRequest request) {
        String cleanProviderId = request.getProviderName();
        cleanProviderId = cleanProviderId.replaceAll("\\s+","");
        cleanProviderId = Sanitizer.sanitizeField(cleanProviderId);
        Optional<Provider> providerOpt = providerDAO.getProvider(cleanProviderId);

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