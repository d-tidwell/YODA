package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.UpdatePHRRequest;
import com.nashss.se.yodaservice.activity.results.UpdatePHRResult;
import com.nashss.se.yodaservice.dynamodb.DictationDAO;
import com.nashss.se.yodaservice.dynamodb.PHRDAO;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

public class UpdatePHRActivity{

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

    public UpdatePHRResult handleRequest(final UpdatePHRRequest request){
        //Pending Dictation link
        return UpdatePHRResult.builder()
                .build();
    }
}
