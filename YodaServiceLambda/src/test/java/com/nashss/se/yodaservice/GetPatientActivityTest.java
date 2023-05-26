package com.nashss.se.yodaservice;

import com.nashss.se.yodaservice.activity.GetPatientActivity;
import com.nashss.se.yodaservice.activity.requests.GetPatientRequest;
import com.nashss.se.yodaservice.activity.results.GetPatientResult;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import com.nashss.se.yodaservice.dynamodb.models.Patient;
import com.nashss.se.yodaservice.exceptions.PatientNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetPatientActivityTest {

    @Mock
    private PatientDAO patientDAO;

    @InjectMocks
    private GetPatientActivity handler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHandleRequest() {
        // setup
        String patientId = "patientId";
        GetPatientRequest request = GetPatientRequest.builder()
                .withPatientId(patientId)
                .build();

        // Mock a Patient
        String name = "John";
        String age = "25";
        Patient mockPatient = new Patient();
        mockPatient.setPatientId(patientId);
        mockPatient.setName(name);
        mockPatient.setAge(age);

        when(patientDAO.getPatient(patientId)).thenReturn(mockPatient);

        // execute
        GetPatientResult result = handler.handleRequest(request);

        // verify
        verify(patientDAO, times(1)).getPatient(patientId);
        assertEquals(name, result.getName());
        assertEquals(age, result.getAge());
    }


    @Test
    public void testHandleRequestWhenGetPatientThrowsException() {
        // setup
        String patientId = "patientId";
        GetPatientRequest request = new GetPatientRequest(patientId);

        when(patientDAO.getPatient(patientId)).thenThrow(new PatientNotFoundException());

        // execute and verify
        assertThrows(PatientNotFoundException.class, () -> handler.handleRequest(request));
        verify(patientDAO, times(1)).getPatient(patientId);
    }

}

