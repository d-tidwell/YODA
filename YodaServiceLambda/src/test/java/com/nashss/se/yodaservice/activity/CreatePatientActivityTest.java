package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.CreatePatientActivity;
import com.nashss.se.yodaservice.activity.requests.CreatePatientRequest;
import com.nashss.se.yodaservice.activity.results.CreatePatientResult;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import com.nashss.se.yodaservice.dynamodb.models.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
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
        String patientAge = "30";
        String sex = "M";
        String phoneNumber = "1234567890";
        String address = "123 Main St";

        CreatePatientRequest request = CreatePatientRequest.builder()
                .withPatientName(patientName)
                .withPatientAge(patientAge)
                .withSex(sex)
                .withPhoneNumber(phoneNumber)
                .withAddress(address)
                .build();

        Patient madePatient = new Patient();
        String patientNameClean = request.getPatientName().replaceAll("\\s+","");
        madePatient.setName(patientNameClean);
        madePatient.setAge(patientAge);
        madePatient.setSex(sex);
        madePatient.setPhoneNumber(phoneNumber);
        madePatient.setAddress(address);
        // we can't set patientId here, because it is randomly generated

        when(patientDAO.savePatient(any(Patient.class))).thenReturn(true);

        // execute
        CreatePatientResult result = handler.handleRequest(request);

        // ...

        ArgumentMatcher<Patient> patientMatcher = new ArgumentMatcher<Patient>() {
            @Override
            public boolean matches(Patient argument) {
                // Define what it means for two Patient objects to be equal
                return argument.getName().equals(madePatient.getName())
                        && argument.getAge() == madePatient.getAge()
                        && argument.getSex().equals(madePatient.getSex())
                        && argument.getPhoneNumber().equals(madePatient.getPhoneNumber())
                        && argument.getAddress().equals(madePatient.getAddress());
            }
        };

        // Use the custom matcher in the verify call
        verify(patientDAO, times(1)).savePatient(argThat(patientMatcher));
        assertTrue(Boolean.valueOf(result.getSuccess()));
    }
}
