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
    private final AmazonS3 s3client;

    private final Logger log = LogManager.getLogger();

    // Declare constant for bucket name
    private static final String BUCKET_NAME = "nss-s3-c02-capstone-darek-alternate-z-artifacts";

    // Declare constant for expiration time (12 minutes in milliseconds) No magic numbers
    private static final long EXPIRATION_TIME_IN_MS = 12 * 60 * 1000;

    @Inject
    public GetPresignedAudioActivity(AmazonS3 s3client) {
        this.s3client = s3client;
    }

    public GetPresigneds3Result handleRequest(final GetPresigneds3Request request) {

        String objectKey = request.getPhrId();
        Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += EXPIRATION_TIME_IN_MS;
        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(BUCKET_NAME, objectKey)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

        URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);

        // Log for debugging
        log.info("Generated presigned URL for PHR ID: " + request.getPhrId());

        return GetPresigneds3Result.builder()
                .withUrl(url.toString())
                .build();
    }
}


