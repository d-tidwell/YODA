package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.CreateProviderRequest;
import com.nashss.se.yodaservice.activity.results.CreateProviderResult;
import com.nashss.se.yodaservice.dynamodb.PHRDAO;
import com.nashss.se.yodaservice.dynamodb.ProviderDAO;
import com.nashss.se.yodaservice.dynamodb.models.PHR;
import com.nashss.se.yodaservice.dynamodb.models.Provider;
import com.nashss.se.yodaservice.enums.COMP_TEST_DATA;
import com.nashss.se.yodaservice.enums.PHRStatus;
import com.nashss.se.yodaservice.utils.Sanitizer;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Objects;

public class CreateProviderActivity {

    private final ProviderDAO providerDAO;
    private final PHRDAO phrdao;

    @Inject
    public CreateProviderActivity(ProviderDAO providerDAO, PHRDAO phrdao) {
        this.providerDAO = providerDAO;
        this.phrdao = phrdao;
    }

    public CreateProviderResult handleRequest(final CreateProviderRequest request) {
        String newProviderId = request.getProviderName();
        newProviderId.trim();
        newProviderId.replaceAll("\\s+","");
        Sanitizer.sanitizeField(newProviderId);
        boolean confirmation;
        if (!Objects.isNull(providerDAO.getProvider(newProviderId))) {
            Provider madeProvider = new Provider();
            madeProvider.setName(newProviderId);
            madeProvider.setMedicalSpecialty("PRIMARYCARE");
            madeProvider.setPendingPatients(Arrays.asList("TEST_PATIENT1", "TEST_PATIENT2", "TEST_PATIENT3"));
            madeProvider.setProviderId(request.getProviderEmail());
            confirmation = providerDAO.updateProvider(madeProvider);
            PHR phr = new PHR();
            phr.setPhrId("postVisit_TEST_PATIENT1_2023-06-10_0000000000");
            phr.setDate("2023-10-22");
            phr.setStatus(PHRStatus.CREATED.toString());
            phr.setProviderName(newProviderId);
            phr.setPatientId("TEST_PATIENT2");
            phr.setComprehendData(COMP_TEST_DATA.getData());
            phrdao.savePHR(phr);
        } else {
            confirmation = false;
        }
        return CreateProviderResult.builder()
                .withSuccess(confirmation)
                .build();
    }
}
