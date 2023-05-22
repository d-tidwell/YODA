package com.nashss.se.yodaservice.activity;

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

    private final PatientDAO patientDAO;
    private final PHRDAO phrdao;

    private final DictationDAO dicDao;

    @Inject
    public UpdateDictationActivity(PatientDAO patientDAO, PHRDAO phrdao, DictationDAO dicDao) {
        this.patientDAO = patientDAO;
        this.dicDao = dicDao;
        this.phrdao = phrdao;
    }

    public UpdateDictationResult handleRequest(final UpdateDictationRequest request) {
        PHR existingRecord = phrdao.getPHR(request.getPhrId(), request.getPhrDate());
        Dictation dictation = dicDao.getDictation(request.getFileName(), existingRecord.getDate());

//        existingRecord.setStatus(PHRStatus.TRANSCRIBING.toString());
        //pending presigned flow and some R&D
        return UpdateDictationResult.builder()
                .build();
    }
}
