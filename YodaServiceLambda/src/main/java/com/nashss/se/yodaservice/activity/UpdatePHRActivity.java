package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.UpdatePHRRequest;
import com.nashss.se.yodaservice.activity.results.UpdatePHRResult;
import com.nashss.se.yodaservice.dynamodb.DictationDAO;
import com.nashss.se.yodaservice.dynamodb.PHRDAO;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import com.nashss.se.yodaservice.dynamodb.models.PHR;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

public class UpdatePHRActivity {

    private final Logger log = LogManager.getLogger();

    private final PatientDAO patientDAO;
    private final PHRDAO phrdao;

    private final DictationDAO dicDao;

    @Inject
    public UpdatePHRActivity(PatientDAO patientDAO, PHRDAO phrdao, DictationDAO dicDao) {
        this.patientDAO = patientDAO;
        this.dicDao = dicDao;
        this.phrdao = phrdao;
    }

    public UpdatePHRResult handleRequest(final UpdatePHRRequest request) {
        //Pending Dictation link check for text in bucket idk??? just an edit criteria idk what we need yet
        PHR phr = phrdao.getPHRsByPHRId(request.getPhrId());
        phr.setStatus(request.getStatus());
        boolean response = phrdao.savePHR(phr);
        return UpdatePHRResult.builder()
                .withSuccess(response)
                .build();
    }
}
