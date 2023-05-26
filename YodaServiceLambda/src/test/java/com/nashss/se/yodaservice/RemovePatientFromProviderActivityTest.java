package com.nashss.se.yodaservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nashss.se.yodaservice.activity.RemovePatientFromProviderActivity;
import com.nashss.se.yodaservice.activity.requests.RemovePatientFromProviderRequest;
import com.nashss.se.yodaservice.activity.results.RemovePatientFromProviderResult;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import com.nashss.se.yodaservice.dynamodb.ProviderDAO;
import com.nashss.se.yodaservice.dynamodb.models.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RemovePatientFromProviderActivityTest {

    @Mock
    private PatientDAO patientDAO;

    @Mock
    private ProviderDAO providerDAO;

    @InjectMocks
    private RemovePatientFromProviderActivity handler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHandleRequest() {
        // setup
        String patientId = "patientId";
        String providerName = "providerName";
        RemovePatientFromProviderRequest request = RemovePatientFromProviderRequest.builder()
                .withPatientId(patientId)
                .withProviderName(providerName)
                .build();
        Provider provider = new Provider();
        provider.setName(providerName);
        List<String> q = new ArrayList<>(Arrays.asList("TEST1", patientId, "TEST2"));
        provider.setPendingPatients(q);
        when(providerDAO.getProvider(providerName)).thenReturn(provider);
        when(providerDAO.updatePending(provider)).thenReturn(true);

        // execute
        RemovePatientFromProviderResult result = handler.handleRequest(request);

        // verify
        verify(patientDAO, times(1)).getPatient(patientId);
        verify(providerDAO, times(1)).getProvider(providerName);
        verify(providerDAO, times(1)).updatePending(provider);
        assertTrue(result.getSuccess());
        assertFalse(provider.getPendingPatients().contains(patientId));
    }
}

