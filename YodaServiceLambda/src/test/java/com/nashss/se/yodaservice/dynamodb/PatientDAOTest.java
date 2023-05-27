package com.nashss.se.yodaservice.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.nashss.se.yodaservice.dynamodb.models.Patient;
import com.nashss.se.yodaservice.exceptions.PatientNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PatientDAOTest {
    @Mock
    private DynamoDBMapper dynamoDbMapper;
    @InjectMocks
    private PatientDAO patientDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetPatient() {
        String testPatientId = "testId";
        Patient testPatient = new Patient();

        when(dynamoDbMapper.load(Patient.class, testPatientId)).thenReturn(testPatient);

        Patient patient = patientDAO.getPatient(testPatientId);
        assertEquals(testPatient, patient, "Expected the returned patient to be the test patient");

        verify(dynamoDbMapper, times(1)).load(Patient.class, testPatientId);
    }

    @Test
    public void testGetPatientNotFound() {
        String testPatientId = "testId";

        when(dynamoDbMapper.load(Patient.class, testPatientId)).thenReturn(null);

        assertThrows(PatientNotFoundException.class, () -> {
            patientDAO.getPatient(testPatientId);
        });

        verify(dynamoDbMapper, times(1)).load(Patient.class, testPatientId);
    }

    @Test
    public void testSavePatient() {
        Patient testPatient = new Patient();

        doNothing().when(dynamoDbMapper).save(testPatient);

        assertTrue(patientDAO.savePatient(testPatient), "Expected patient saving to be successful");

        verify(dynamoDbMapper, times(1)).save(testPatient);
    }

    @Test
    public void testSavePatientFailure() {
        Patient testPatient = new Patient();

        doThrow(new AmazonDynamoDBException("Error")).when(dynamoDbMapper).save(testPatient);

        assertFalse(patientDAO.savePatient(testPatient), "Expected patient saving to be unsuccessful");

        verify(dynamoDbMapper, times(1)).save(testPatient);
    }
}
