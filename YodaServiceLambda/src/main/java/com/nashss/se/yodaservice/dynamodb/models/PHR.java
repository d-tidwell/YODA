package com.nashss.se.yodaservice.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "phrs_")
public class PHR {
    private String phrId;
    private String patientId;
    private String providerName;
    private String recordDate;
    private String status;
    
    @DynamoDBHashKey(attributeName = "phrId")
    public String getPhrId() {
        return phrId;
    }

    public void setPhrId(String phrId) {
        this.phrId = phrId;
    }
    @DynamoDBAttribute(attributeName = "patientId")
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "PatientDateIndex")
    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
    @DynamoDBAttribute(attributeName = "providerName")
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "ProviderStatusIndex", attributeName = "providerName")
    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
    @DynamoDBRangeKey(attributeName = "recordDate")
    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "PatientDateIndex", attributeName = "recordDate")
    public String getDate() {
        return recordDate;
    }

    public void setDate(String recordDate) {
        this.recordDate = recordDate;
    }
    @DynamoDBAttribute(attributeName = "status")
    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "ProviderStatusIndex", attributeName = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
