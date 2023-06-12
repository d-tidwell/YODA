package com.nashss.se.yodaservice.activity;


import com.amazonaws.HttpMethod;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.s3.AmazonS3;

import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nashss.se.yodaservice.enums.PHRStatus;
import com.nashss.se.yodaservice.models.ApiResponse;
import com.nashss.se.yodaservice.models.TranscriptJSON;
import software.amazon.awssdk.services.comprehendmedical.ComprehendMedicalClient;
import software.amazon.awssdk.services.comprehendmedical.model.Attribute;
import software.amazon.awssdk.services.comprehendmedical.model.DetectEntitiesRequest;
import software.amazon.awssdk.services.comprehendmedical.model.DetectEntitiesResponse;
import software.amazon.awssdk.services.comprehendmedical.model.DetectEntitiesV2Request;
import software.amazon.awssdk.services.comprehendmedical.model.DetectEntitiesV2Response;
import software.amazon.awssdk.services.comprehendmedical.model.Entity;
import software.amazon.awssdk.services.comprehendmedical.model.Trait;


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
import java.util.ArrayList;
import java.util.Arrays;
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
    private final ComprehendMedicalClient comprehendClient;

    @Inject
    public UpdateDictationActivity(PHRDAO phrdao, DictationDAO dicDao, ProviderDAO providerDAO, AmazonS3 s3client, ComprehendMedicalClient comprehendClient) {
        this.dicDao = dicDao;
        this.phrdao = phrdao;
        this.providerDAO = providerDAO;
        this.s3client = s3client;
        this.comprehendClient = comprehendClient;
    }

    public UpdateDictationResult handleRequest(final UpdateDictationRequest request) {

//        //which record test validity
        PHR existingRecord = phrdao.getPHR("TEST_PHRID2", "2023-03-23");
//
//        //which dictation test it is there
//        Dictation dictation = dicDao.getDictation(existingRecord.getPhrId(), existingRecord.getDate());
//
//        //create a job name which ends up the filename in s3
//        String transcribeJobName =  request.getPhrId() + "_" + existingRecord.getProviderName();
//        //get a URL
//        String bucketName = "nss-s3-c02-capstone-darek-alternate-z-artifacts";
//        String audioFileUrl = s3client.getUrl(bucketName, request.getFileName()).toString();
//        //build a job
//        String languageCode = "en-US";
//        StartMedicalTranscriptionJobResponse startResponse = dicDao.startTranscribe(transcribeJobName, audioFileUrl,
//                bucketName, languageCode,
//                providerDAO.getProvider(existingRecord.getProviderName()).getMedicalSpecialty());
//        String jobStatus = startResponse.medicalTranscriptionJob().transcriptionJobStatus().toString();
//
//        try {
//            if (jobStatus == null || jobStatus.isEmpty()) {
//                throw new TranscribeActionException("There was an error starting the requested Job");
//            }
//        } catch (TranscribeActionException e){
//            log.error("Transcribe Job Initialization returned empty or null job");
//        }
//
//        //indicate it is transcribing
//        existingRecord.setStatus(PHRStatus.TRANSCRIBING.toString());
//        //save  phr here as state
//        phrdao.savePHR(existingRecord);
//        //retries in while loop to confirm completion or fail
//        int maxTries = 30;
//        while(maxTries > 0) {
//                maxTries--;
//                try {
//                        TimeUnit.SECONDS.sleep(10);
//                        jobStatus = dicDao.getTranscribeJobStatus(transcribeJobName);
//                        if(jobStatus.equals("COMPLETED")) {
//                                      //indicate it is transcribing
//                                    System.out.println("SUCCESS " + transcribeJobName + ". Current status is " + jobStatus + ".");
//                                    existingRecord.setStatus(PHRStatus.AWAITING_ANALYSIS.toString());
//                                    break;
//                        } else if (jobStatus.equals("FAILED"))  {
//                                    System.out.println("FAILED " + transcribeJobName + ". Current status is " + jobStatus + ".");
//                                    existingRecord.setStatus("FAILED");
//                                    break;
//                        } else {
//                                System.out.println("Waiting for " + transcribeJobName + ". Current status is " + jobStatus + ".");
//                                    existingRecord.setStatus(PHRStatus.TRANSCRIBING.toString());
//                        }
//                    } catch(InterruptedException e) {
//                        existingRecord.setStatus(PHRStatus.FAILED.toString());
//                        log.error("Error transcribe line 73 UpdateDictation",e);
//                        e.printStackTrace();
//                    }
//                }
//        phrdao.savePHR(existingRecord);
        //testing!!!!!!!!!!!!!!!!!!
           existingRecord.setStatus(PHRStatus.AWAITING_ANALYSIS.toString());
//         if the jobStatus in Completed make presignedURL and return the .json object from the /medical bucket
            if (existingRecord.getStatus().equals(PHRStatus.AWAITING_ANALYSIS.toString())) {
//            System.out.println("Entering Text Retrieval for " + transcribeJobName);
//                Date expiration = new java.util.Date();
//                long expTimeMillis = expiration.getTime();
//                // Add 10 mins.
//                expTimeMillis += 1000 * 10;
//                expiration.setTime(expTimeMillis);
//                // Get the presigned URL
//                String objectKey = "medical/" + transcribeJobName + ".json";
//                GeneratePresignedUrlRequest generatePresignedUrlRequest =
//                    new GeneratePresignedUrlRequest(bucketName, objectKey)
//                        .withMethod(HttpMethod.GET)
//                        .withExpiration(expiration);
//
//                URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);
//                System.out.println("Generating S3 Get URL for " + transcribeJobName);
//                HttpURLConnection connection;
            try {
//                make the connection
//                    connection = (HttpURLConnection) url.openConnection();
//                    connection.setRequestMethod("GET");
//                System.out.println("S3 Read Text Connection Response Code " + connection.getResponseCode());
                boolean testing = true;
//                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                 process the response
                if (testing == true) {
//                     Parse the inputStream using Jackson directly
//                        ObjectMapper mapper = new ObjectMapper();
//                        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//                        ApiResponse apiResponse = mapper.readValue(connection.getInputStream(), ApiResponse.class);
//
//                        // Retrieve the transcripts
//                        List<TranscriptJSON> transcripts = apiResponse.getTextTranscribedResults().getTranscripts();
                    TranscriptJSON transcriptsRaw = new TranscriptJSON();
                    transcriptsRaw.setTranscript("A 23-year-old white female presents with complaint of allergies., Allergy / Immunology, Allergic Rhinitis ,\"SUBJECTIVE:,  This 23-year-old white female presents with complaint of allergies.  She used to have allergies when she lived in Seattle but she thinks they are worse here.  In the past, she has tried Claritin, and Zyrtec.  Both worked for short time but then seemed to lose effectiveness.  She has used Allegra also.  She used that last summer and she began using it again two weeks ago.  It does not appear to be working very well.  She has used over-the-counter sprays but no prescription nasal sprays.  She does have asthma but doest not require daily medication for this and does not think it is flaring up.,MEDICATIONS: , Her only medication currently is Ortho Tri-Cyclen and the Allegra.,ALLERGIES: , She has no known medicine allergies.,OBJECTIVE:,Vitals:  Weight was 130 pounds and blood pressure 124/78.,HEENT:  Her throat was mildly erythematous without exudate.  Nasal mucosa was erythematous and swollen.  Only clear drainage was seen.  TMs were clear.,Neck:  Supple without adenopathy.,Lungs:  Clear.,ASSESSMENT:,  Allergic rhinitis.,PLAN:,1.  She will try Zyrtec instead of Allegra again.  Another option will be to use loratadine.  She does not think she has prescription coverage so that might be cheaper.,2.  Samples of Nasonex two sprays in each nostril given for three weeks.  A prescription was written as well.");
                    List<TranscriptJSON> transcripts = new ArrayList<>(Arrays.asList(transcriptsRaw));
                    // Print out each transcript
                    for (TranscriptJSON transcript : transcripts) {
                        System.out.println(transcript.getTranscript());
                        //make the comprehend logic stream here
                        DetectEntitiesV2Request requestDetect = DetectEntitiesV2Request.builder()
                                .text(transcript.getTranscript())
                                .build();

                        DetectEntitiesV2Response  responseDetect = comprehendClient.detectEntitiesV2(requestDetect);

                        System.out.println("Entities found:" + responseDetect.toString());
                        for (Entity e : responseDetect.entities()) {
                            System.out.println(" - Category: " + e.category());
                            System.out.println(" - Type: " + e.type());
                            System.out.println(" - Text: " + e.text());


                            for (Trait t : e.traits()) {
                                System.out.println("   - Trait Name: " + t.name());

                            }

                            for (Attribute a : e.attributes()) {
                                System.out.println("   - Attribute Type: " + a.type());
                                System.out.println("   - Attribute Text: " + a.text());
                            }
                        }
                         System.out.println("PUT OPERATION STARTED");
                         dicDao.putComprehendToTable(responseDetect, existingRecord);
                         PHR returnsGood = phrdao.getPHR(existingRecord.getPhrId(), existingRecord.getDate());
                         System.out.println(returnsGood.toString());
                    }
                } else {
                    // If the object wasn't downloaded successfully, print an error message
                    //log.error("HTTP GET failed with error code " + connection.getResponseCode());
                    log.error("try block");
                }
//                } catch (IOException e) {
//                    log.error("Error occurred", e);
//                }
            } catch (RuntimeException e) {
                log.error("delete me");
            }
        }


        //set the url for the text file & type
//        dictation.setDictationText(transcribeJobName);
//        //save the dictation changes
//        dicDao.afterTranscriptionUpdate(dictation);
//        return UpdateDictationResult.builder()
//                .withStatus(startResponse.medicalTranscriptionJob() +
//                        startResponse.medicalTranscriptionJob().medicalTranscriptionJobName() +
//                        startResponse.medicalTranscriptionJob().completionTime())
//                .build();
//        }

        return UpdateDictationResult.builder()
                .withStatus("all good in the hoood")
                .build();
//                return null;
    }
}
