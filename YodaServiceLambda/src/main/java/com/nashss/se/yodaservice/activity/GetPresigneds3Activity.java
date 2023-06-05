package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.GetPresigneds3Request;
import com.nashss.se.yodaservice.activity.results.GetPresigneds3Result;
import com.nashss.se.yodaservice.dynamodb.DictationDAO;
import com.nashss.se.yodaservice.dynamodb.PHRDAO;
import com.nashss.se.yodaservice.dynamodb.models.PHR;
import com.nashss.se.yodaservice.enums.PHRStatus;

import com.amazonaws.HttpMethod;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import javax.inject.Inject;

public class GetPresigneds3Activity {
    private final String bucketName = "nss-s3-c02-capstone-darek.tidwell-nodelete";
    private final Logger log = LogManager.getLogger();
    private final PHRDAO phrdao;
    private final DictationDAO dicDao;
    private final AmazonS3 s3client;


    @Inject
    public GetPresigneds3Activity(PHRDAO phrdao, DictationDAO dicDao, AmazonS3 s3client) {
        this.dicDao = dicDao;
        this.phrdao = phrdao;
        this.s3client = s3client;
    }

    public GetPresigneds3Result handleRequest(final GetPresigneds3Request request) {
        //Get the PHR we are talking about
        //PHR updatePHR = phrdao.getPHR(request.getPhrId(), request.getDate());
        //create the dictation record
        log.error("HITS THE HANDLE REQUEST");
        System.out.println("HandleRequest");
        dicDao.createDictation(request.getFileName(), request.getPhrId(), request.getDate());

        String objectKey = "audio/" + request.getFileName();
        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        // Add 1 hour.
        expTimeMillis += 1000 * 60;
        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, objectKey)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(expiration);
        generatePresignedUrlRequest.addRequestParameter("Content-Type", "audio/ogg");

        log.error("HITS THE request generator");
        System.out.println("Hits generator");
        URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);
        log.error("Pre-Signed URL: " + url.toString() + " : " + request.getPhrId());
        System.out.println("Pre-Signed URL: " + url.toString() + " : " + request.getPhrId());

        //set the status of the PHR
        //updatePHR.setStatus(PHRStatus.PRESIGNED.toString());
        //phrdao.savePHR(updatePHR);
        //return the URL
        return GetPresigneds3Result.builder()
                .withUrl(url.toString())
                .build();
    }
}

