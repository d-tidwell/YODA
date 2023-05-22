package com.nashss.se.yodaservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = GetProviderRequest.class)
public class GetProviderRequest {

    private final String providerId;

    public GetProviderRequest(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderId() {
        return providerId;
    }

    @Override
    public String toString() {
        return "GetProviderRequest{" +
                "providerId='" + providerId + '\'' +
                '}';
    }
    //CHECKSTYLE:OFF:Builder
    public static Builder builder(){return new Builder();}

    @JsonPOJOBuilder
    public static class Builder {
        private String providerId;

        public Builder withProviderId(String providerId){
            this.providerId = providerId;
            return this;
        }

        public GetProviderRequest build(){return new GetProviderRequest(providerId);}
    }
}
