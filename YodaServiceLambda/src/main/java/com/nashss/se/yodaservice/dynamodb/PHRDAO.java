package com.nashss.se.yodaservice.dynamodb;

import com.nashss.se.yodaservice.dynamodb.models.PHR;
import com.nashss.se.yodaservice.enums.PHRStatus;
import com.nashss.se.yodaservice.exceptions.PHRNotFoundException;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.inject.Inject;

public class PHRDAO {
    private final Logger log = LogManager.getLogger();
    private final DynamoDBMapper dynamoDbMapper;

    @Inject
    public PHRDAO(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    public PHR getPHR(String phrId, String date) {
        PHR phr = this.dynamoDbMapper.load(PHR.class, phrId, date);

        if (Objects.isNull(phr)) {
            log.error(String.format("PHRNotFoundException-getPHR, %s", phrId));
            throw new PHRNotFoundException("Could not find PHR-getPHR request");
        }
        return phr;
    }
    public PHR getPHRsByPHRId(String phrId) {
        DynamoDBQueryExpression<PHR> queryExpression = new DynamoDBQueryExpression<PHR>()
                .withKeyConditionExpression("phrId = :phrId")
                .withExpressionAttributeValues(Collections.singletonMap(":phrId", new AttributeValue().withS(phrId)));

        PaginatedQueryList<PHR> result = this.dynamoDbMapper.query(PHR.class, queryExpression);

        if (result.isEmpty()) {
            log.error(String.format("PHRNotFoundException-getPHRByPHRID, %s", phrId));
            throw new PHRNotFoundException("Could not find PHR-getPHRByPHRID");
        }

        return result.get(0);
    }

    public boolean savePHR(PHR newPHR) {
        try {
            this.dynamoDbMapper.save(newPHR);
        } catch (AmazonDynamoDBException e) {
            log.error("Save PHR Error", e);
            return false;
        }
        return true;
    }

    public List<PHR> getPhrsForPatient(String patientId) {
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
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
                .withKeyConditionExpression("patientId = :patientIdVal and recordDate between :dateVal1 and :dateVal2")
                .withExpressionAttributeValues(eav);

        PaginatedQueryList<PHR> result = this.dynamoDbMapper.query(PHR.class, queryExpression);

        if (result.isEmpty()) {
            log.info(String.format("PHRNotFoundException, %s", patientId));
            throw new PHRNotFoundException("Could not find PHR");
        }

        return result;
    }

    public List<PHR> getUncompletedPHRsByProvider(String providerName) {
        List<String> allStatuses = Arrays.asList(PHRStatus.COMPLETED.toString(), PHRStatus.CREATED.toString(), PHRStatus.FAILED.toString(), PHRStatus.PENDING_SIGNATURE.toString(), PHRStatus.TRANSCRIBING.toString());
        List<String> statusesToQuery = new ArrayList<>(allStatuses);
    
        List<PHR> results = new ArrayList<>();
    
        for (String status : statusesToQuery) {
            Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
            eav.put(":v1", new AttributeValue().withS(providerName));
            eav.put(":v2", new AttributeValue().withS(status));
    
            Map<String, String> ean = new HashMap<>();
            ean.put("#s", "status");
    
            DynamoDBQueryExpression<PHR> queryExpression = new DynamoDBQueryExpression<PHR>()
                    .withIndexName("ProviderStatusIndex")
                    .withConsistentRead(false)
                    .withKeyConditionExpression("providerName = :v1 and #s = :v2")
                    .withExpressionAttributeValues(eav)
                    .withExpressionAttributeNames(ean);
    
            List<PHR> queryResults = this.dynamoDbMapper.query(PHR.class, queryExpression);
            results.addAll(queryResults);
        }
    
        if (results.isEmpty()) {
            log.info(String.format("PHRNotFoundException, %s", providerName));
            throw new PHRNotFoundException("Could not find PHR");
        }
    
        return results;
    }
    
    
}
