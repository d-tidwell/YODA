package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.GetProviderRequest;
import com.nashss.se.yodaservice.activity.results.GetProviderResult;
import com.nashss.se.yodaservice.dynamodb.ProviderDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

public class GetProviderActivity{

    private final Logger log = LogManager.getLogger();
    private final ProviderDAO providerDAO;

    @Inject
    public GetProviderActivity(ProviderDAO providerDAO) {
        this.providerDAO = providerDAO;
    }

    public GetProviderResult handleRequest(final GetProviderRequest request){

        return GetProviderResult.builder()
                .build();
    }
}