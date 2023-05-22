package com.nashss.se.yodaservice.activity;

import com.amazonaws.HttpMethod;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.nashss.se.yodaservice.activity.requests.GetPresigneds3Request;
import com.nashss.se.yodaservice.activity.results.GetPresigneds3Result;
import com.nashss.se.yodaservice.dynamodb.DictationDAO;
import com.nashss.se.yodaservice.dynamodb.PHRDAO;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import com.nashss.se.yodaservice.dynamodb.models.PHR;
import com.nashss.se.yodaservice.enums.PHRStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.net.URL;

public class GetPresigneds3Activity{
    private final String bucketName = "nss-s3-c02-capstone-darek";
    private final Logger log = LogManager.getLogger();
    private final PatientDAO patientDAO;
    private final PHRDAO phrdao;
    private final DictationDAO dicDao;
    private final AmazonS3 s3client;


    @Inject
    public GetPresigneds3Activity(PatientDAO patientDAO, PHRDAO phrdao, DictationDAO dicDao) {
        this.patientDAO = patientDAO;
        this.dicDao = dicDao;
        this.phrdao = phrdao;
        this.s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_2).build();
    }

    public GetPresigneds3Result handleRequest(final GetPresigneds3Request request){
        //Get the PHR we are talking about
        PHR updatePHR = phrdao.getPHR(request.getPhrId(), request.getDate());
        //create the dictation record
        dicDao.createDictation(request.getFileName(), request.getPhrId(), request.getDate());

        String objectKey = "audio/" + request.getFileName();
        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60; // Add 1 hour.
        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, objectKey)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(expiration);
        generatePresignedUrlRequest.addRequestParameter("Content-Type", "audio/mpg");


        URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);
        log.info("Pre-Signed URL: " + url.toString() +" : " + request.getPhrId());
        //set the status of the PHR
        updatePHR.setStatus(PHRStatus.PRESIGNED.toString());
        phrdao.savePHR(updatePHR);
        //return the URL
        return GetPresigneds3Result.builder()
                .withURL(url.toString())
                .build();
    }
}