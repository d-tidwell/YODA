package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.CreatePHRRequest;
import com.nashss.se.yodaservice.activity.results.CreatePHRResult;
import com.nashss.se.yodaservice.converters.ModelConverter;
import com.nashss.se.yodaservice.dynamodb.DictationDAO;
import com.nashss.se.yodaservice.dynamodb.PHRDAO;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import com.nashss.se.yodaservice.dynamodb.ProviderDAO;
import com.nashss.se.yodaservice.dynamodb.models.Dictation;
import com.nashss.se.yodaservice.dynamodb.models.PHR;
import com.nashss.se.yodaservice.enums.PHRStatus;
import com.nashss.se.yodaservice.utils.UUIDGenerator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
public class CreatePHRActivity {

    private final Logger log = LogManager.getLogger();

    private final PatientDAO patientDAO;

    private final ProviderDAO providerDAO;

    private final PHRDAO phrdao;

    private final DictationDAO dictationDAO;

    @Inject
    public CreatePHRActivity(PatientDAO patientDAO, ProviderDAO providerDAO, PHRDAO phrdao, DictationDAO dictationDAO) {
        this.patientDAO = patientDAO;
        this.providerDAO = providerDAO;
        this.phrdao = phrdao;
        this.dictationDAO = dictationDAO;
    }

    public CreatePHRResult handleRequest(final CreatePHRRequest request) {
        patientDAO.getPatient(request.getPatientId());
        providerDAO.getProvider(request.getProviderName());
        String phrId = request.getType() + "_" + request.getPatientId() + "_" + request.getDate() + "_" + UUIDGenerator.generateUniqueId();
        PHR newPHR = new PHR();
        newPHR.setPhrId(phrId);
        newPHR.setPatientId(request.getPatientId());
        newPHR.setProviderName(request.getProviderName());
        newPHR.setDate(request.getDate());
        newPHR.setStatus(PHRStatus.CREATED.toString());
        phrdao.savePHR(newPHR);
        //we need to create a dictation object as well so that it exists on update dict loop
        dictationDAO.createDictation(phrId,newPHR.getPhrId(),request.getDate(), request.getType());
        log.error(phrId + newPHR.getPhrId() + request.getDate() + request.getType());
        return CreatePHRResult.builder()
                .withPHR(ModelConverter.phrConvertSingle(newPHR))
                .build();
    }
}

