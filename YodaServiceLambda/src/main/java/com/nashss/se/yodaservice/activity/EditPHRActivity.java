package com.nashss.se.yodaservice.activity;

import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nashss.se.yodaservice.activity.requests.EditPHRRequest;
import com.nashss.se.yodaservice.activity.results.EditPHRResult;
import com.nashss.se.yodaservice.converters.HealthDataConverter;
import com.nashss.se.yodaservice.dynamodb.DictationDAO;
import com.nashss.se.yodaservice.dynamodb.PHRDAO;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import com.nashss.se.yodaservice.dynamodb.ProviderDAO;
import com.nashss.se.yodaservice.dynamodb.models.Dictation;
import com.nashss.se.yodaservice.dynamodb.models.PHR;

import com.nashss.se.yodaservice.enums.PHRStatus;
import com.nashss.se.yodaservice.exceptions.PHRNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.awssdk.services.comprehendmedical.ComprehendMedicalClient;
import software.amazon.awssdk.services.comprehendmedical.model.DetectEntitiesV2Request;
import software.amazon.awssdk.services.comprehendmedical.model.DetectEntitiesV2Response;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

public class EditPHRActivity {

    private final Logger log = LogManager.getLogger();
    private final PHRDAO phrdao;
    private final DictationDAO dicDao;
    private final ComprehendMedicalClient comprehendClient;

    @Inject
    public EditPHRActivity(PHRDAO phrdao, DictationDAO dicDao, ComprehendMedicalClient comprehendClient) {
        this.comprehendClient = comprehendClient;
        this.dicDao = dicDao;
        this.phrdao = phrdao;
    }

    public EditPHRResult handleRequest(final EditPHRRequest request) {
        //Pending Dictation link check for text in bucket idk??? just an edit criteria idk what we need yet
        PHR phr = phrdao.getPHRsByPHRId(request.getPhrId());
        Dictation oldDic = dicDao.getDictation(phr.getPhrId(), phr.getDate());
        //not in the s3 bucket bc its edited
        oldDic.setDictationText(request.getText());
        dicDao.afterTranscriptionUpdate(oldDic);
        //update the phr status to
        phr.setStatus(PHRStatus.EDITING_TEXTRECORD.toString());
        phr.setTranscription(request.getText());
        phrdao.savePHR(phr);
        //push to comprehend model
        DetectEntitiesV2Request requestDetect = DetectEntitiesV2Request.builder()
                .text(request.getText())
                .build();

        DetectEntitiesV2Response responseDetect = comprehendClient.detectEntitiesV2(requestDetect);
        HealthDataConverter tableMap = new HealthDataConverter();
        Map<String, Map<String, Map<String, List<Map<String, Object>>>>> mappedItems = tableMap.parse(responseDetect.entities());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonMap = objectMapper.writeValueAsString(mappedItems);
            dicDao.putComprehendToTable(jsonMap, phr);
            PHR returnsGood = phrdao.getPHR(phr.getPhrId(), phr.getDate());
            return EditPHRResult.builder()
                    .withComprehendData(returnsGood.getComprehendData())
                    .withTranscription(returnsGood.getTranscription())
                    .build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        throw new PHRNotFoundException("There was an error editing the PHR");
    }
}