package com.nashss.se.yodaservice.activity;

import com.amazonaws.services.s3.AmazonS3;
import com.nashss.se.yodaservice.activity.UpdateDictationActivity;

import com.nashss.se.yodaservice.activity.requests.UpdateDictationRequest;
import com.nashss.se.yodaservice.dynamodb.DictationDAO;
import com.nashss.se.yodaservice.dynamodb.PHRDAO;
import com.nashss.se.yodaservice.dynamodb.ProviderDAO;
import com.nashss.se.yodaservice.dynamodb.models.Dictation;
import com.nashss.se.yodaservice.dynamodb.models.PHR;
import com.nashss.se.yodaservice.dynamodb.models.Provider;
import org.mockito.InjectMocks;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.transcribe.model.MedicalTranscriptionJob;
import software.amazon.awssdk.services.transcribe.model.StartMedicalTranscriptionJobResponse;

import java.net.URL;
import java.time.Instant;

import static org.mockito.Mockito.*;

public class UpdateDictationActivityTest {

    @Mock
    private PHRDAO phrdao;

    @Mock
    private DictationDAO dicDao;

    @Mock
    private ProviderDAO providerDAO;

    @Mock
    private AmazonS3 s3client;

    @InjectMocks
    private UpdateDictationActivity updateDictationActivity;

    private UpdateDictationRequest request;
    private PHR existingRecord;
    private Dictation dictation;
    private Provider provider;
    private StartMedicalTranscriptionJobResponse response;
    private MedicalTranscriptionJob medicalTransJob;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        request = mock(UpdateDictationRequest.class);
        dictation = new Dictation();
        existingRecord = mock(PHR.class);
        provider = mock(Provider.class);
        response = mock(StartMedicalTranscriptionJobResponse.class);
        medicalTransJob = mock(MedicalTranscriptionJob.class);
    }

    @Test
    public void testHandleRequest() throws Exception {
        // Given
        String phrId ="phrId";
        String phrDate="date";
        String type = "testType";
        String fileName = "testFileName";
        String providerName = "testProviderName";
        String bucketName = "nss-s3-c02-capstone-darek";
        String jobName = "text/" + providerName + phrId + phrDate;
        String audioFileUrl = "http://localhost/test.mp3";
        String medicalSpecialty = "testMedicalSpecialty";
        when(request.getPhrId()).thenReturn(phrId);
        when(request.getPhrDate()).thenReturn(phrDate);
        when(request.getFileName()).thenReturn(fileName);
        when(existingRecord.getDate()).thenReturn(phrDate);
        when(existingRecord.getProviderName()).thenReturn(providerName);
        when(provider.getMedicalSpecialty()).thenReturn(medicalSpecialty);
        when(phrdao.getPHR(phrId, phrDate)).thenReturn(existingRecord);
        when(dicDao.getDictation(fileName, phrDate)).thenReturn(dictation);
        when(s3client.getUrl(bucketName, fileName)).thenReturn(new URL(audioFileUrl));
        when(dicDao.startTranscribe(any(), any(), any(), any(), any())).thenReturn(response);
        when(providerDAO.getProvider(any())).thenReturn(provider);
        when(response.medicalTranscriptionJob()).thenReturn(medicalTransJob);
        when(response.medicalTranscriptionJob().medicalTranscriptionJobName()).thenReturn("JobName");
        when(response.medicalTranscriptionJob().completionTime()).thenReturn(Instant.now());


        // When
        updateDictationActivity.handleRequest(request);

        // Then
        verify(phrdao).getPHR(phrId, phrDate);
        verify(dicDao).getDictation(fileName, phrDate);
        verify(s3client).getUrl(bucketName, fileName);
        verify(dicDao).startTranscribe(jobName, audioFileUrl, bucketName, "en-US", medicalSpecialty);
        verify(phrdao).savePHR(existingRecord);
        verify(dicDao).afterTranscriptionUpdate(dictation);
    }
}



