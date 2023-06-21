package com.nashss.se.yodaservice.dynamodb;

import com.nashss.se.yodaservice.dynamodb.models.Provider;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.ProviderNotFoundException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;

public class ProviderDAO {
    private final Logger log = LogManager.getLogger();
    private final DynamoDBMapper dynamoDbMapper;

    @Inject
    public ProviderDAO(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    public Optional<Provider> getProvider(String providerName) {
        Provider provider = this.dynamoDbMapper.load(Provider.class, providerName);

        if (Objects.isNull(provider)) {
            log.error(String.format("ProviderNotFoundException, %s", providerName));
            throw new NoSuchElementException("Provider not found: " + providerName);
        }
       return Optional.ofNullable((Provider) provider);
    }

    public boolean updateProvider(Provider provider) {
        try {
            this.dynamoDbMapper.save(provider);
            return true; 
        } catch (AmazonDynamoDBException e) {
            log.error("Save Provider Error", e);
            return false;
        }
    }
    
}
