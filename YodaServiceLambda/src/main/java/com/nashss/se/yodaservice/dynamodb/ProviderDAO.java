package com.nashss.se.yodaservice.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.nashss.se.yodaservice.dynamodb.models.Patient;
import com.nashss.se.yodaservice.dynamodb.models.Provider;
import com.nashss.se.yodaservice.exceptions.PatientNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.Objects;

public class ProviderDAO {
    private final Logger log = LogManager.getLogger();
    private final DynamoDBMapper dynamoDbMapper;

    @Inject
    public ProviderDAO(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    public Provider getProvider(String providerName){
        Provider provider = this.dynamoDbMapper.load(Provider.class, providerName);

        if(Objects.isNull(provider)){
            log.info(String.format("ProviderNotFoundException, %s", providerName));
        }
        return provider;
    }

    public boolean updatePending(Provider provider){
        try{
            this.dynamoDbMapper.save(provider);
        } catch (AmazonDynamoDBException e) {
            log.error("Save Provider Error", e);
            return false;
        }
        return true;
    }
}
