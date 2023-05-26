package com.nashss.se.yodaservice;

import com.nashss.se.yodaservice.activity.GetPHRActivity;
import com.nashss.se.yodaservice.activity.GetProviderActivity;
import com.nashss.se.yodaservice.activity.requests.GetProviderRequest;
import com.nashss.se.yodaservice.activity.results.GetProviderResult;
import com.nashss.se.yodaservice.dynamodb.PHRDAO;
import com.nashss.se.yodaservice.dynamodb.ProviderDAO;
import com.nashss.se.yodaservice.dynamodb.models.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetProviderActivityTest {

    @Mock
    private ProviderDAO providerDao;

    @InjectMocks
    private GetProviderActivity handler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHandleRequest() {
        // setup
        String providerName = "providerName";
        GetProviderRequest request = new GetProviderRequest(providerName);
        Provider provider = new Provider();
        provider.setName(providerName);
        provider.setMedicalSpecialty("specialty");
        provider.setPendingPatients(new ArrayList<>(Arrays.asList("patient1", "patient2")));

        when(providerDao.getProvider(providerName)).thenReturn(provider);

        // execute
        GetProviderResult result = handler.handleRequest(request);

        // verify
        verify(providerDao, times(1)).getProvider(request.getProviderName());
        assertEquals(provider.getName(), result.getName());
        assertEquals(provider.getMedicalSpecialty(), result.getMedicalSpecialty());
        assertEquals(provider.getPendingPatients(), result.getPendingPatients());
    }

}
