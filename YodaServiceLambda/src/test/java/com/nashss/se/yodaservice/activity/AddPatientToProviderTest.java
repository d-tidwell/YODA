package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.AddPatientToProviderActivity;
import com.nashss.se.yodaservice.activity.requests.AddPatientToProviderRequest;
import com.nashss.se.yodaservice.activity.results.AddPatientToProviderResult;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import com.nashss.se.yodaservice.dynamodb.ProviderDAO;
import com.nashss.se.yodaservice.dynamodb.models.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddPatientToProviderTest {

    @Mock
    private PatientDAO patientDAO;

    @Mock
    private ProviderDAO providerDAO;

    @InjectMocks
    private AddPatientToProviderActivity handler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHandleRequest() {
        // setup
        String patientId = "patientId";
        String providerName = "providerName";
        String testPatient = "TEST3";
        AddPatientToProviderRequest request = new AddPatientToProviderRequest(patientId, providerName);
        Provider provider = new Provider();
        provider.setName(providerName);
        List<String> q = new ArrayList<>(Arrays.asList("TEST1","TEST2"));
        q.add(testPatient);
        provider.setPendingPatients(q);
        when(providerDAO.getProvider(providerName)).thenReturn(provider);
        when(providerDAO.updatePending(provider)).thenReturn(true);

        // execute
        AddPatientToProviderResult result = handler.handleRequest(request);

        // verify
        verify(patientDAO, times(1)).getPatient(patientId);
        verify(providerDAO, times(1)).getProvider(providerName);
        verify(providerDAO, times(1)).updatePending(provider);
        assertTrue(result.getSuccess());
        assertEquals(testPatient, provider.getPendingPatients().get(2));
    }
}