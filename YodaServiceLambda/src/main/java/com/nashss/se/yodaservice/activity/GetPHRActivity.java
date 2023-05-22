package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.GetPHRRequest;
import com.nashss.se.yodaservice.activity.results.GetPHRResult;
import com.nashss.se.yodaservice.converters.ModelConverter;
import com.nashss.se.yodaservice.dynamodb.PHRDAO;
import com.nashss.se.yodaservice.models.PHRModel;

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

    public GetPHRResult handleRequest(final GetPHRRequest request) {
        PHRModel result = ModelConverter.phrConvertSingle(phrdao.getPHRsByPHRId(request.getPhrId()));
        return GetPHRResult.builder()
                .withPatientId(result.getPatientId())
                .withProviderName(result.getProviderName())
                .withDate(result.getDate())
                .withStatus(result.getStatus())
                .build();
    }
}
