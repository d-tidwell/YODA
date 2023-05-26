package com.nashss.se.yodaservice;

import com.nashss.se.yodaservice.activity.CreatePHRActivity;
import com.nashss.se.yodaservice.activity.requests.CreatePHRRequest;
import com.nashss.se.yodaservice.activity.results.CreatePHRResult;
import com.nashss.se.yodaservice.dynamodb.PHRDAO;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import com.nashss.se.yodaservice.dynamodb.ProviderDAO;
import com.nashss.se.yodaservice.dynamodb.models.PHR;
import com.nashss.se.yodaservice.dynamodb.models.Patient;
import com.nashss.se.yodaservice.dynamodb.models.Provider;
import com.nashss.se.yodaservice.enums.PHRStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreatePHRActivityTest {

    @Mock
    private PatientDAO patientDAO;

    @Mock
    private ProviderDAO providerDAO;

    @Mock
    private PHRDAO phrdao;

    @InjectMocks
    private CreatePHRActivity handler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHandleRequest() {
        // setup
        String patientId = "patientId";
        String providerName = "providerName";
        String date = "2023-05-26";
        CreatePHRRequest request = CreatePHRRequest.builder()
                .withPatientId(patientId)
                .withProviderName(providerName)
                .withDate(date)
                .build();

        PHR newPHR = new PHR();
        newPHR.setPatientId(patientId);
        newPHR.setProviderName(providerName);
        newPHR.setDate(date);
        newPHR.setStatus(PHRStatus.CREATED.toString());
        // we can't set phrId here, because it's generated using UUIDGenerator.generateUniqueId()

        when(patientDAO.getPatient(patientId)).thenReturn(new Patient());
        when(providerDAO.getProvider(providerName)).thenReturn(new Provider());
        when(phrdao.savePHR(any(PHR.class))).thenReturn(true);

        // execute
        CreatePHRResult result = handler.handleRequest(request);

        // verify
        verify(patientDAO, times(1)).getPatient(patientId);
        verify(providerDAO, times(1)).getProvider(providerName);
        verify(phrdao, times(1)).savePHR(any(PHR.class));
        assertEquals("true", result.getStatus());
    }
}

