package com.nashss.se.yodaservice.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "phrs")
public class PHR {
    private String phrId;
    private String patientId;
    private String providerName;
    private String date;
    private String status;
    private String dictationId;
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
    @DynamoDBRangeKey(attributeName = "date")
    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "PatientDateIndex", attributeName = "date")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    @DynamoDBAttribute(attributeName = "status")
    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "ProviderStatusIndex", attributeName = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @DynamoDBAttribute(attributeName = "dictationId")
    public String getDictationId() {
        return dictationId;
    }

    public void setDictationId(String dictationId) {
        this.dictationId = dictationId;
    }
}
