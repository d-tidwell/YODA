package com.nashss.se.yodaservice.dynamodb;

import com.nashss.se.yodaservice.dynamodb.models.Dictation;
import com.nashss.se.yodaservice.exceptions.DictationNotFoundException;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.awssdk.services.transcribe.TranscribeClient;
import software.amazon.awssdk.services.transcribe.model.GetMedicalTranscriptionJobRequest;
import software.amazon.awssdk.services.transcribe.model.GetMedicalTranscriptionJobResponse;
import software.amazon.awssdk.services.transcribe.model.Media;
import software.amazon.awssdk.services.transcribe.model.StartMedicalTranscriptionJobRequest;
import software.amazon.awssdk.services.transcribe.model.StartMedicalTranscriptionJobResponse;

import java.util.Objects;
import javax.inject.Inject;

public class DictationDAO {
    private final Logger log = LogManager.getLogger();
    private final DynamoDBMapper dynamoDbMapper;
    private final TranscribeClient transcribeClient;
    @Inject
    public DictationDAO(DynamoDBMapper dynamoDbMapper, TranscribeClient transcribeClient) {
        this.dynamoDbMapper = dynamoDbMapper;
        this.transcribeClient = transcribeClient;
    }

    public Dictation getDictation(String dictationId, String date) {
        Dictation dic = this.dynamoDbMapper.load(Dictation.class, dictationId, date);

        if (Objects.isNull(dic)) {
            log.error(String.format("DictationNotFoundException-getDictation, %s", dic));
            throw new DictationNotFoundException("Could not find Dic dictation DAO");
        }
        return dic;
    }

    public boolean createDictation(String dictationId, String phRid, String date, String type) {
        Dictation newDic = new Dictation();
        newDic.setDictationId(dictationId);
        newDic.setPhrId(phRid);
        newDic.setDate(date);
        newDic.setDictationAudio(dictationId);
        newDic.setType(type);
      
        try {
            this.dynamoDbMapper.save(newDic);
            return true;
        } catch (AmazonDynamoDBException e) {
            log.error(String.format("Create Dictation Error %s, %s", newDic.toString(), e));
        }
        return false;
    }

    public String getTranscribeJobStatus(String jobName) {
        GetMedicalTranscriptionJobRequest request = GetMedicalTranscriptionJobRequest.builder()
                .medicalTranscriptionJobName(jobName)
                .build();
        GetMedicalTranscriptionJobResponse result = transcribeClient.getMedicalTranscriptionJob(request);
        return result.medicalTranscriptionJob().transcriptionJobStatus().toString();
    }

    public StartMedicalTranscriptionJobResponse startTranscribe(String transcribeJobName, String audioFileUrl,
                                                                String bucketName, String languageCode,
                                                                String medicalSpecialty){
        StartMedicalTranscriptionJobRequest jobRequest = StartMedicalTranscriptionJobRequest.builder()
                .medicalTranscriptionJobName(transcribeJobName)
                .specialty(medicalSpecialty)
                .media(Media.builder().mediaFileUri(audioFileUrl).build())
                .languageCode(languageCode)
                .outputBucketName(bucketName)
                .type("DICTATION")
                .build();
        StartMedicalTranscriptionJobResponse response = transcribeClient.startMedicalTranscriptionJob(jobRequest);
        return response;
    }



    public boolean afterTranscriptionUpdate(Dictation dic){
        try {
            this.dynamoDbMapper.save(dic);
            return true;
        } catch (AmazonDynamoDBException e) {
            log.error(String.format("Create Dictation Error %s, %s", dic.toString(), e));
        }
        return false;
    }

}


