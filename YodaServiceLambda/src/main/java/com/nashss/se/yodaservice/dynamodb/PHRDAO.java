package com.nashss.se.yodaservice.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.nashss.se.yodaservice.dynamodb.models.PHR;
import com.nashss.se.yodaservice.exceptions.PHRNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.Objects;

public class PHRDAO {
    private final Logger log = LogManager.getLogger();
    private final DynamoDBMapper dynamoDbMapper;

    @Inject
    public PHRDAO(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    public PHR getPHR(String PHRId){
        PHR phr = this.dynamoDbMapper.load(PHR.class, PHRId);

        if(Objects.isNull(phr)){
            log.info(String.format("PHRNotFoundException, %s", PHRId));
            throw new PHRNotFoundException("Could not find PHR");
        }
        return phr;
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

}
