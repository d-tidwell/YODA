package com.nashss.se.yodaservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = GetProviderRequest.class)
public class GetProviderRequest {

    private final String providerName;

    public GetProviderRequest(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderName() {
        return providerName;
    }

    @Override
    public String toString() {
        return "GetProviderRequest{" +
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

        public GetProviderRequest build(){return new GetProviderRequest(providerName);}
    }
}
