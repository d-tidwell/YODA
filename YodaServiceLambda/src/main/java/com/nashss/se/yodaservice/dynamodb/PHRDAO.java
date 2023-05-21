package com.nashss.se.yodaservice.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.nashss.se.yodaservice.dynamodb.models.PHR;
import com.nashss.se.yodaservice.exceptions.PHRNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PHRDAO {
    private final Logger log = LogManager.getLogger();
    private final DynamoDBMapper dynamoDbMapper;

    @Inject
    public PHRDAO(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    public PHR getPHR(String PHRId, String date){
        PHR phr = this.dynamoDbMapper.load(PHR.class, PHRId, date);

        if(Objects.isNull(phr)){
            log.info(String.format("PHRNotFoundException, %s", PHRId));
            throw new PHRNotFoundException("Could not find PHR");
        }
        return phr;
    }
    public PHR getPHRsByPHRId(String PHRId){
        DynamoDBQueryExpression<PHR> queryExpression = new DynamoDBQueryExpression<PHR>()
                .withKeyConditionExpression("PHRId = :phrId")
                .withExpressionAttributeValues(Collections.singletonMap(":phrId", new AttributeValue().withS(PHRId)));

        PaginatedQueryList<PHR> result = this.dynamoDbMapper.query(PHR.class, queryExpression);

        if(result.isEmpty()){
            log.info(String.format("PHRNotFoundException, %s", PHRId));
            throw new PHRNotFoundException("Could not find PHR");
        }

        return result.get(0);
    }

    public boolean savePHR(PHR newPHR){
        try{
            this.dynamoDbMapper.save(newPHR);
        } catch (AmazonDynamoDBException e) {
            log.error("Save PHR Error", e);
            return false;
        }
        return true;
    }

    public List<PHR> getPhrsForPatient(String patientId) {
        HashMap<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":v1", new AttributeValue().withS(patientId));

        DynamoDBQueryExpression<PHR> queryExpression = new DynamoDBQueryExpression<PHR>()
                .withIndexName("PatientDateIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("patientId = :v1")
                .withExpressionAttributeValues(eav);

        return dynamoDbMapper.query(PHR.class, queryExpression);
    }

    public List<PHR> getPHRsByPatientIdAndDateRange(String patientId, String startDate, String endDate) {
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":patientIdVal", new AttributeValue().withS(patientId));
        eav.put(":dateVal1", new AttributeValue().withS(startDate));
        eav.put(":dateVal2", new AttributeValue().withS(endDate));

        DynamoDBQueryExpression<PHR> queryExpression = new DynamoDBQueryExpression<PHR>()
                .withIndexName("PatientDateIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("patientId = :patientIdVal and date between :dateVal1 and :dateVal2")
                .withExpressionAttributeValues(eav);

        PaginatedQueryList<PHR> result = this.dynamoDbMapper.query(PHR.class, queryExpression);

        if(result.isEmpty()){
            log.info(String.format("PHRNotFoundException, %s", patientId));
            throw new PHRNotFoundException("Could not find PHR");
        }

        return result;
    }
}
