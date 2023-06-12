package com.nashss.se.yodaservice.dependency;

import com.amazonaws.services.s3.AmazonS3;
import com.nashss.se.yodaservice.dynamodb.AmazonS3AndTranscribeProviders;
import com.nashss.se.yodaservice.dynamodb.DynamoDbClientProvider;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import dagger.Module;
import dagger.Provides;
import software.amazon.awssdk.services.comprehendmedical.ComprehendMedicalClient;
import software.amazon.awssdk.services.transcribe.TranscribeClient;
import software.amazon.awssdk.services.comprehendmedical.ComprehendMedicalClient;


import javax.inject.Singleton;

/**
 * Dagger Module providing dependencies for DAO classes.
 */
@Module
public class DaoModule {
    /**
     * Provides a DynamoDBMapper singleton instance.
     *
     * @return DynamoDBMapper object
     */
    @Singleton
    @Provides
    public DynamoDBMapper provideDynamoDBMapper() {
        return new DynamoDBMapper(DynamoDbClientProvider.getDynamoDBClient(Regions.US_EAST_2));
    }

    @Provides
    @Singleton
    public AmazonS3 provideAmazonS3Client() {
        return AmazonS3AndTranscribeProviders.getAmazonS3Client();
    }

    @Provides
    @Singleton
    public TranscribeClient provideTranscribeClient() {
        return AmazonS3AndTranscribeProviders.getTranscribeClient();
    }

    @Provides
    @Singleton
    public ComprehendMedicalClient provideComprehendClient() {
        return AmazonS3AndTranscribeProviders.getComprehendClient();
    }
}

