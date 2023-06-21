package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.GetPHRRangeRequest;
import com.nashss.se.yodaservice.activity.results.GetPHRRangeResult;
import com.nashss.se.yodaservice.converters.ModelConverter;
import com.nashss.se.yodaservice.dynamodb.PHRDAO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

public class GetPHRRangeActivity {

    private final PHRDAO phrdao;

    @Inject
    public GetPHRRangeActivity(PHRDAO phrdao) {
        this.phrdao = phrdao;
    }
    public GetPHRRangeResult handleRequest(final GetPHRRangeRequest request) {
        return GetPHRRangeResult.builder()
                .withPhrId(ModelConverter.convertListPHRtoModels(phrdao.getPHRsByPatientIdAndDateRange(
                        request.getPatientId(), request.getFrom(), request.getTo()
                )))
                .build();
    }
}

