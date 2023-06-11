package com.nashss.se.yodaservice.activity;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nashss.se.yodaservice.enums.PHRStatus;
import com.nashss.se.yodaservice.models.ApiResponse;
import com.nashss.se.yodaservice.models.TranscriptJSON;
import software.amazon.awssdk.services.transcribe.model.GetMedicalTranscriptionJobResponse;
import software.amazon.awssdk.services.transcribe.model.StartMedicalTranscriptionJobResponse;

import com.nashss.se.yodaservice.dynamodb.ProviderDAO;
import com.nashss.se.yodaservice.activity.requests.UpdateDictationRequest;
import com.nashss.se.yodaservice.activity.results.UpdateDictationResult;
import com.nashss.se.yodaservice.dynamodb.DictationDAO;
import com.nashss.se.yodaservice.dynamodb.PHRDAO;
import com.nashss.se.yodaservice.dynamodb.models.Dictation;
import com.nashss.se.yodaservice.dynamodb.models.PHR;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.net.URL;

import javax.inject.Inject;

public class UpdateDictationActivity {

    private final Logger log = LogManager.getLogger();
    private final PHRDAO phrdao;
    private final DictationDAO dicDao;
    private final ProviderDAO providerDAO;
    private final AmazonS3 s3client;

    @Inject
    public UpdateDictationActivity(PHRDAO phrdao, DictationDAO dicDao, ProviderDAO providerDAO,  AmazonS3 s3client) {
        this.dicDao = dicDao;
        this.phrdao = phrdao;
        this.providerDAO = providerDAO;
        this.s3client = s3client;
    }

    public UpdateDictationResult handleRequest(final UpdateDictationRequest request) {
     
        //which record test validity
        PHR existingRecord = phrdao.getPHR(request.getPhrId(), request.getPhrDate());
    
        //which dictation test it is there
        Dictation dictation = dicDao.getDictation(existingRecord.getPhrId(), existingRecord.getDate());
        
        //create a job name which ends up the filename in s3
        String transcribeJobName =  request.getPhrId() + "_" + existingRecord.getProviderName();
        //get a URL
        String bucketName = "nss-s3-c02-capstone-darek-alternate-z-artifacts";
        String audioFileUrl = s3client.getUrl(bucketName, request.getFileName()).toString();
        //build a job
        String languageCode = "en-US";
        StartMedicalTranscriptionJobResponse startResponse = dicDao.startTranscribe(transcribeJobName, audioFileUrl,
                bucketName, languageCode,
                providerDAO.getProvider(existingRecord.getProviderName()).getMedicalSpecialty());
        String jobStatus = startResponse.medicalTranscriptionJob().transcriptionJobStatus().toString();

        try {
            if (jobStatus == null || jobStatus.isEmpty()) {
                throw new TranscribeActionException("There was an error starting the requested Job");
            }
        } catch (TranscribeActionException e){
            log.error("Transcribe Job Initialization returned empty or null job");
        }

        //indicate it is transcribing
        existingRecord.setStatus(PHRStatus.TRANSCRIBING.toString());
        //save  phr here as state
        phrdao.savePHR(existingRecord);
        //retries in while loop to confirm completion or fail
        int maxTries = 30;
        while(maxTries > 0) {
                maxTries--;
                try {
                        TimeUnit.SECONDS.sleep(10);
                        jobStatus = dicDao.getTranscribeJobStatus(transcribeJobName);
                        if(jobStatus.equals("COMPLETED")) {
                                      //indicate it is transcribing
                                    System.out.println("SUCCESS " + transcribeJobName + ". Current status is " + jobStatus + ".");
                                    existingRecord.setStatus(PHRStatus.AWAITING_ANALYSIS.toString());
                                    break;
                        } else if (jobStatus.equals("FAILED"))  {
                                    System.out.println("FAILED " + transcribeJobName + ". Current status is " + jobStatus + ".");
                                    existingRecord.setStatus("FAILED");
                                    break;
                        } else {
                                System.out.println("Waiting for " + transcribeJobName + ". Current status is " + jobStatus + ".");
                                    existingRecord.setStatus(PHRStatus.TRANSCRIBING.toString());
                        }
                    } catch(InterruptedException e) {
                        existingRecord.setStatus(PHRStatus.FAILED.toString());
                        log.error("Error transcribe line 73 UpdateDictation",e);
                        e.printStackTrace();
                    }
                }
        phrdao.savePHR(existingRecord);
        // if the jobStatus in Completed make presignedURL and return the .json object from the /medical bucket
        if (existingRecord.getStatus().equals(PHRStatus.AWAITING_ANALYSIS.toString())) {
                System.out.println("Entering Text Retrieval for " + transcribeJobName);
                Date expiration = new java.util.Date();
                long expTimeMillis = expiration.getTime();
                // Add 10 mins.
                expTimeMillis += 1000 * 10;
                expiration.setTime(expTimeMillis);
                // Get the presigned URL
                String objectKey = "medical/" + transcribeJobName + ".json";
                GeneratePresignedUrlRequest generatePresignedUrlRequest = 
                    new GeneratePresignedUrlRequest(bucketName, objectKey)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

                URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);
                System.out.println("Generating S3 Get URL for " + transcribeJobName);
                HttpURLConnection connection;
                try {
                    //make the connection
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    System.out.println("Connection Response Code " + connection.getResponseCode());
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        // process the response

                        // Parse the inputStream using Jackson directly
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        ApiResponse apiResponse = mapper.readValue(connection.getInputStream(), ApiResponse.class);

                        // Retrieve the transcripts
                        List<TranscriptJSON> transcripts = apiResponse.getTextTranscribedResults().getTranscripts();

                        // Print out each transcript
                        for (TranscriptJSON transcript : transcripts) {
                            System.out.println(transcript.getTranscript());
                        }
                    }
                    else {
                        // If the object wasn't downloaded successfully, print an error message
                        log.error("HTTP GET failed with error code " + connection.getResponseCode());
                    }
                } catch (IOException e) {
                    log.error("Error occurred", e);
                }
            }
    

        //set the url for the text file & type
        dictation.setDictationText(transcribeJobName);
        //save the dictation changes
        dicDao.afterTranscriptionUpdate(dictation);
        return UpdateDictationResult.builder()
                .withStatus(startResponse.medicalTranscriptionJob() +
                        startResponse.medicalTranscriptionJob().medicalTranscriptionJobName() +
                        startResponse.medicalTranscriptionJob().completionTime())
                .build();
        }
}
