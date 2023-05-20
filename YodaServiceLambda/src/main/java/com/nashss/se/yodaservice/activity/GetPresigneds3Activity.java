package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.GetPresigneds3Request;
import com.nashss.se.yodaservice.activity.results.GetPresigneds3Result;
import com.nashss.se.yodaservice.dynamodb.DictationDAO;
import com.nashss.se.yodaservice.dynamodb.PHRDAO;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

public class GetPresigneds3Activity{

    private final Logger log = LogManager.getLogger();

    private final PatientDAO patientDAO;
    private final PHRDAO phrdao;

    private final DictationDAO dicDao;

    @Inject
    public GetPresigneds3Activity(PatientDAO patientDAO, PHRDAO phrdao, DictationDAO dicDao) {
        this.patientDAO = patientDAO;
        this.dicDao = dicDao;
        this.phrdao = phrdao;
    }

    public GetPresigneds3Result handleRequest(final GetPresigneds3Request request){

        return GetPresigneds3Result.builder()
                .build();
    }
}