package com.nashss.se.yodaservice.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;


@DynamoDBTable(tableName = "providers")
public class Provider {
    private String providerId;
    private String name;
    private String medicalSpecialty;
    private List<String> pendingPatients;
    
    @DynamoDBHashKey(attributeName = "providerId")
    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @DynamoDBAttribute(attributeName = "medicalSpecialty")
    public String getMedicalSpecialty() {
        return medicalSpecialty;
    }

    public void setMedicalSpecialty(String medicalSpecialty) {
        this.medicalSpecialty = medicalSpecialty;
    }
    @DynamoDBAttribute(attributeName = "pendingPatients")
    public Deque<String> getPendingPatients() {
        return new ArrayDeque<>(pendingPatients);
    }

    public void setPendingPatients(Deque<String> pendingPatients) {
        this.pendingPatients = new ArrayList<String>(pendingPatients);
    }
}
