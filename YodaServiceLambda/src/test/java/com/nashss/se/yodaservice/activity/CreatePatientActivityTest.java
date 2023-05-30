package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.CreatePatientActivity;
import com.nashss.se.yodaservice.activity.requests.CreatePatientRequest;
import com.nashss.se.yodaservice.activity.results.CreatePatientResult;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import com.nashss.se.yodaservice.dynamodb.models.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreatePatientActivityTest {

    @Mock
    private PatientDAO patientDAO;

    @InjectMocks
    private CreatePatientActivity handler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHandleRequest() {
        // setup
        String patientName = "John Doe";
        CreatePatientRequest request = CreatePatientRequest.builder()
                .withPatientName(patientName)
                .build();

        Patient madePatient = new Patient();
        madePatient.setName(patientName);
        // we can't set patientId and age here, because they are randomly generated

        when(patientDAO.savePatient(any(Patient.class))).thenReturn(true);

        // execute
        CreatePatientResult result = handler.handleRequest(request);

        // verify
        verify(patientDAO, times(1)).savePatient(any(Patient.class));
        assertTrue(Boolean.valueOf(result.getSuccess()));
    }
}
