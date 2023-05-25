package com.nashss.se.yodaservice.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;


import java.util.ArrayList;
import java.util.List;


@DynamoDBTable(tableName = "providersZ")
public class Provider {
    private String providerId;
    private String name;
    private String medicalSpecialty;
    private List<String> pendingPatients;

    public Provider() {
        this.pendingPatients = new ArrayList<>();  // Initialize pendingPatients as an empty ArrayList
    }

    @DynamoDBAttribute(attributeName = "providerId")
    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
    @DynamoDBHashKey(attributeName = "name")
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
    public List<String> getPendingPatients() {
        return pendingPatients;
    }

    public void setPendingPatients(List<String> pendingPatients) {
        this.pendingPatients = pendingPatients;
    }
}
