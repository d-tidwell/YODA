package com.nashss.se.yodaservice.dynamodb;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.transcribe.TranscribeClient;

public class AmazonS3AndTranscribeProviders {

    private static AmazonS3 s3client;
    private static TranscribeClient transcribeClient;

    public static AmazonS3 getAmazonS3Client() {
        if(s3client == null) {
            s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_2).build();
        }
        return s3client;
    }

    public static TranscribeClient getTranscribeClient() {
        if(transcribeClient == null) {
            transcribeClient = TranscribeClient.builder().region(Region.US_EAST_2).build();
        }
        return transcribeClient;
    }
}
