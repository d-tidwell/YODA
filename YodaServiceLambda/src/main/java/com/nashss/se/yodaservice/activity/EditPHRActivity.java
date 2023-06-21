package com.nashss.se.yodaservice.activity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nashss.se.yodaservice.activity.requests.EditPHRRequest;
import com.nashss.se.yodaservice.activity.results.EditPHRResult;
import com.nashss.se.yodaservice.converters.HealthDataConverter;
import com.nashss.se.yodaservice.dynamodb.DictationDAO;
import com.nashss.se.yodaservice.dynamodb.PHRDAO;
import com.nashss.se.yodaservice.dynamodb.models.Dictation;
import com.nashss.se.yodaservice.dynamodb.models.PHR;

import com.nashss.se.yodaservice.enums.PHRStatus;
import com.nashss.se.yodaservice.exceptions.PHRException;
import com.nashss.se.yodaservice.utils.Sanitizer;
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

    public EditPHRResult handleRequest(final EditPHRRequest request) throws PHRException {

        // Fetching PHR by PHR ID
        PHR phr = phrdao.getPHRsByPHRId(request.getPhrId());

        // Fetching the associated dictation
        Dictation oldDictation = dicDao.getDictation(phr.getPhrId(), phr.getDate());

        // Sanitizing and setting the text in the dictation
        oldDictation.setDictationText(Sanitizer.sanitizeField(request.getText()));
        dicDao.afterTranscriptionUpdate(oldDictation);

        // Updating the PHR status and transcription
        phr.setStatus(PHRStatus.EDITING_TEXTRECORD.toString());
        phr.setTranscription(request.getText());
        phrdao.savePHR(phr);

        // Detecting entities from the text
        DetectEntitiesV2Request detectEntitiesRequest = DetectEntitiesV2Request.builder()
                .text(request.getText())
                .build();

        DetectEntitiesV2Response detectEntitiesResponse = comprehendClient.detectEntitiesV2(detectEntitiesRequest);

        // Parsing and mapping the detected entities
        HealthDataConverter healthDataConverter = new HealthDataConverter();
        Map<String, Map<String, Map<String, List<Map<String, Object>>>>> parsedEntities = healthDataConverter.parse(detectEntitiesResponse.entities());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Converting the mapped entities to JSON
            String jsonEntities = objectMapper.writeValueAsString(parsedEntities);
            dicDao.putComprehendToTable(jsonEntities, phr);

            // Fetching the updated PHR and setting the status
            PHR updatedPHR = phrdao.getPHR(phr.getPhrId(), phr.getDate());
            updatedPHR.setStatus(PHRStatus.PENDING_SIGNATURE.toString());
            phrdao.savePHR(updatedPHR);

            log.info("Successfully edited PHR");

            // Returning the edited result
            return EditPHRResult.builder()
                    .withComprehendData(updatedPHR.getComprehendData())
                    .withTranscription(updatedPHR.getTranscription())
                    .build();

        } catch (JsonProcessingException e) {
            log.error("Error while processing JSON", e);
            throw new PHRException("There was an error editing the PHR", e);
        }

    }
}