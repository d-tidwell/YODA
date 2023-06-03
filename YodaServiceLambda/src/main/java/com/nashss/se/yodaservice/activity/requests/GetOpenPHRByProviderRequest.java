package com.nashss.se.yodaservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = GetOpenPHRByProviderRequest.Builder.class)
public class GetOpenPHRByProviderRequest {
    private final String providerName;

    public GetOpenPHRByProviderRequest(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderName(){
        return this.providerName;
    }

    @Override
    public String toString() {
        return "GetOpenPHRByProviderRequest{" +
                "providerName='" + providerName + '\'' +
                '}';
    }
    //CHECKSTYLE:OFF:Builder
    public static Builder builder(){return new Builder();}

    @JsonPOJOBuilder
    public static class Builder {
        private String providerName;

        public Builder withProviderName(String providerName){
            this.providerName = providerName;
            return this;
        }

        public GetOpenPHRByProviderRequest build(){return new GetOpenPHRByProviderRequest(providerName);}
    }
}
