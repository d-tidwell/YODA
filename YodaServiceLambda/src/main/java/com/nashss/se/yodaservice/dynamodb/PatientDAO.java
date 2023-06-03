package com.nashss.se.yodaservice.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.nashss.se.yodaservice.dynamodb.models.Patient;
import com.nashss.se.yodaservice.exceptions.PatientNotFoundException;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.inject.Inject;

public class PatientDAO {
    private final Logger log = LogManager.getLogger();
    private final DynamoDBMapper dynamoDbMapper;

    @Inject
    public PatientDAO(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    public Patient getPatient(String patientId) {
        Patient patient = this.dynamoDbMapper.load(Patient.class, patientId);

        if (Objects.isNull(patient)) {
            log.info(String.format("PatientNotFoundException, %s", patientId));
            throw new PatientNotFoundException("Could not find patient");
        }
        return patient;
    }

    public boolean savePatient(Patient newPatient) {
        try {
            this.dynamoDbMapper.save(newPatient);
        } catch (AmazonDynamoDBException e) {
            log.error("Save Patient Error", e);
            return false;
        }
        return true;
    }

    public List<Patient> getAllPatients() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        PaginatedScanList<Patient> patients = dynamoDbMapper.scan(Patient.class, scanExpression);
        return patients.stream().collect(Collectors.toList());
    }

}


