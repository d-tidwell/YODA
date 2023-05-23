package com.nashss.se.yodaservice.activity;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.nashss.se.yodaservice.enums.PHRStatus;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.transcribe.TranscribeClient;
import software.amazon.awssdk.services.transcribe.model.StartMedicalTranscriptionJobRequest;
import software.amazon.awssdk.services.transcribe.model.StartMedicalTranscriptionJobResponse;
import software.amazon.awssdk.services.transcribe.model.Media;
import com.nashss.se.yodaservice.dynamodb.ProviderDAO;
import com.nashss.se.yodaservice.activity.requests.UpdateDictationRequest;
import com.nashss.se.yodaservice.activity.results.UpdateDictationResult;
import com.nashss.se.yodaservice.dynamodb.DictationDAO;
import com.nashss.se.yodaservice.dynamodb.PHRDAO;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import com.nashss.se.yodaservice.dynamodb.models.Dictation;
import com.nashss.se.yodaservice.dynamodb.models.PHR;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

public class UpdateDictationActivity {

    private final Logger log = LogManager.getLogger();
    private final PHRDAO phrdao;
    private final DictationDAO dicDao;
    private final ProviderDAO providerDAO;
    private final AmazonS3 s3client;
    private final TranscribeClient transcribeClient;
    private final String bucketName = "nss-s3-c02-capstone-darek";
    private final String languageCode = "en-US";
    @Inject
    public UpdateDictationActivity(PHRDAO phrdao, DictationDAO dicDao, ProviderDAO providerDAO) {
        this.dicDao = dicDao;
        this.phrdao = phrdao;
        this.providerDAO = providerDAO;
        this.s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_2).build();
        this.transcribeClient = TranscribeClient.builder().region(Region.US_EAST_2).build();
    }

    public UpdateDictationResult handleRequest(final UpdateDictationRequest request) {
        //which record test validity
        PHR existingRecord = phrdao.getPHR(request.getPhrId(), request.getPhrDate());
        //which dictation test it is there
        Dictation dictation = dicDao.getDictation(request.getFileName(), existingRecord.getDate());
        //create a job name
        String transcribeJobName = "text/" + existingRecord.getProviderName() + request.getPhrId() +
                request.getPhrDate();
        //get a URL
        String audioFileUrl = s3client.getUrl(bucketName, request.getFileName()).toString();
        //build a job
        StartMedicalTranscriptionJobRequest jobRequest = StartMedicalTranscriptionJobRequest.builder()
                .medicalTranscriptionJobName(transcribeJobName)
                .specialty(providerDAO.getProvider(existingRecord.getProviderName()).getMedicalSpecialty())
                .media(Media.builder().mediaFileUri(audioFileUrl).build())
                .languageCode(languageCode)
                .outputBucketName(bucketName)
                .build();

        StartMedicalTranscriptionJobResponse response = transcribeClient.startMedicalTranscriptionJob(jobRequest);
        //indicate it is transcribing
        existingRecord.setStatus(PHRStatus.TRANSCRIBING.toString());
        //save the phr change
        phrdao.savePHR(existingRecord);
        //set the url for the text file & type
        dictation.setDictationText(transcribeJobName);
        dictation.setType(request.getType());
        //save the dictation changes
        dicDao.afterTranscriptionUpdate(dictation);
        return UpdateDictationResult.builder()
                .withStatus(response.medicalTranscriptionJob() +
                        response.medicalTranscriptionJob().medicalTranscriptionJobName() +
                        response.medicalTranscriptionJob().completionTime())
                .build();
    }
}
