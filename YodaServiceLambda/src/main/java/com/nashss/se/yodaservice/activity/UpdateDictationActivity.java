package com.nashss.se.yodaservice.activity;

import com.amazonaws.services.s3.AmazonS3;
import com.nashss.se.yodaservice.enums.PHRStatus;
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
        //Dictation dictation = dicDao.getDictation(request.getFileName(), existingRecord.getDate());
        //create a job name
        String transcribeJobName = existingRecord.getProviderName() + request.getPhrId() +
                request.getPhrDate();
        //get a URL
        String bucketName = "nss-s3-c02-capstone-darek-alternate-z-artifacts";
        String audioFileUrl = s3client.getUrl(bucketName, request.getFileName()).toString();
        //build a job
        String languageCode = "en-US";

        StartMedicalTranscriptionJobResponse response = dicDao.startTranscribe(transcribeJobName, audioFileUrl,
                bucketName, languageCode,
                providerDAO.getProvider(existingRecord.getProviderName()).getMedicalSpecialty());
        //indicate it is transcribing
        //existingRecord.setStatus(PHRStatus.TRANSCRIBING.toString());
        //save the phr change
        //phrdao.savePHR(existingRecord);
        //set the url for the text file & type
        //dictation.setDictationText(transcribeJobName);
        //dictation.setType(request.getType());
        //save the dictation changes
        //dicDao.afterTranscriptionUpdate(dictation);
        return UpdateDictationResult.builder()
                .withStatus(response.medicalTranscriptionJob() +
                        response.medicalTranscriptionJob().medicalTranscriptionJobName() +
                        response.medicalTranscriptionJob().completionTime())
                .build();
    }
}
