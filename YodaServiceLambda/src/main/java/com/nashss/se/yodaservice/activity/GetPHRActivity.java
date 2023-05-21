package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.GetPHRRequest;
import com.nashss.se.yodaservice.activity.results.GetPHRResult;
import com.nashss.se.yodaservice.dynamodb.PHRDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

public class GetPHRActivity {
    private final Logger log = LogManager.getLogger();
    private final PHRDAO phrdao;
    @Inject
    public GetPHRActivity(PHRDAO phrdao) {
        this.phrdao = phrdao;
    }

    public GetPHRResult handleRequest(final GetPHRRequest request){
        return GetPHRResult.builder().build();
    }
}
