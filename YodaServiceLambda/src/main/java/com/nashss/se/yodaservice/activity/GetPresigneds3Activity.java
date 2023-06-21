package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.GetPresigneds3Request;
import com.nashss.se.yodaservice.activity.results.GetPresigneds3Result;
import com.nashss.se.yodaservice.dynamodb.DictationDAO;
import com.nashss.se.yodaservice.dynamodb.PHRDAO;


import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Date;
import javax.inject.Inject;

public class GetPresigneds3Activity {
    private static final String BUCKET_NAME = "nss-s3-c02-capstone-darek-alternate-z-artifacts";
    private static final long EXPIRATION_TIME_IN_MS = 20 * 60 * 1000;  // 20 minutes
    private static final Logger log = LogManager.getLogger();
    private final AmazonS3 s3client;

    @Inject
    public GetPresigneds3Activity(AmazonS3 s3client) {
        this.s3client = s3client;
    }

    public GetPresigneds3Result handleRequest(final GetPresigneds3Request request) {

        String objectKey = request.getFileName();

        Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += EXPIRATION_TIME_IN_MS;
        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(BUCKET_NAME, objectKey)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(expiration);
        generatePresignedUrlRequest.addRequestParameter("Content-Type", "audio/webm");

        URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);

        log.info("Generated presigned URL for file name: " + request.getFileName());

        return GetPresigneds3Result.builder()
                .withUrl(url.toString())
                .build();
    }
}

