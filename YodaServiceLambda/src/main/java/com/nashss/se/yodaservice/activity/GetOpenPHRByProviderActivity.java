package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.GetAllPHRRequest;
import com.nashss.se.yodaservice.activity.requests.GetOpenPHRByProviderRequest;
import com.nashss.se.yodaservice.activity.results.GetAllPHRResult;
import com.nashss.se.yodaservice.activity.results.GetOpenPHRByProviderResult;
import com.nashss.se.yodaservice.converters.ModelConverter;
import com.nashss.se.yodaservice.dynamodb.PHRDAO;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import com.nashss.se.yodaservice.dynamodb.models.PHR;
import com.nashss.se.yodaservice.models.PHRModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import javax.inject.Inject;



public class GetOpenPHRByProviderActivity {

    private final Logger log = LogManager.getLogger();
    private final PHRDAO phrdao;

    @Inject
    public  GetOpenPHRByProviderActivity (PHRDAO phrdao) {
        this.phrdao = phrdao;
    }

    public GetOpenPHRByProviderResult handleRequest(final GetOpenPHRByProviderRequest request) {

        List<PHR> all = phrdao.getUncompletedPHRsByProvider(request.getProviderName());
        List<PHRModel> results = ModelConverter.convertListPHRtoModels(all);

        return GetOpenPHRByProviderResult.builder()
                .withPhrId(results)
                .build();
    }
}
