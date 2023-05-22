package com.nashss.se.yodaservice.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "dictations")
public class Dictation {
    private String dictationId;
    private String phrId;
    private String date;
    private String type;
    private String dictationText;
    private String dictationAudio;
    @DynamoDBHashKey(attributeName = "dictationId")
    public String getDictationId() {
        return dictationId;
    }
    @DynamoDBAttribute(attributeName = "phrId")
    public String getPhrId() {
        return this.phrId;
    }

    public void setPhrId(String phrId) {
        this.phrId = phrId;
    }

    public void setDictationId(String dictationId) {
        this.dictationId = dictationId;
    }
    @DynamoDBRangeKey(attributeName = "date")
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "DateTypeIndex", attributeName = "date")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    @DynamoDBAttribute(attributeName = "type")
    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "DateTypeIndex", attributeName = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @DynamoDBAttribute(attributeName = "dictationText")
    public String getDictationText() {
        return dictationText;
    }

    public void setDictationText(String dictationText) {
        this.dictationText = dictationText;
    }
    @DynamoDBAttribute(attributeName = "dictationAudio")
    public String getDictationAudio() {
        return dictationAudio;
    }

    public void setDictationAudio(String dictationAudio) {
        this.dictationAudio = dictationAudio;
    }
}
