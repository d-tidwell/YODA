package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.GetPresigneds3Request;
import com.nashss.se.yodaservice.activity.results.GetPresigneds3Result;


import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Date;
import javax.inject.Inject;

public class GetPresignedAudioActivity {
    private final String bucketName = "nss-s3-c02-capstone-darek-alternate-z-artifacts";
    private final Logger log = LogManager.getLogger();
    private final AmazonS3 s3client;


    @Inject
    public GetPresignedAudioActivity(AmazonS3 s3client) {
        this.s3client = s3client;
    }

    public GetPresigneds3Result handleRequest(final GetPresigneds3Request request) {

        String objectKey = request.getPhrId();
        Date expiration = new java.util.Date();

        long expTimeMillis = expiration.getTime();
        // Add 1 hour.
        expTimeMillis += 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, objectKey)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);
        
        URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);

        return GetPresigneds3Result.builder()
                .withUrl(url.toString())
                .build();
    }
}

