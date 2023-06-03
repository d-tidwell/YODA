package com.nashss.se.yodaservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = GetPatientRequest.Builder.class)
public class GetAllPatientsRequest {
    private final String providerName;

    public GetAllPatientsRequest(String providerName) {
        this.providerName = providerName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getProviderName() {
        return providerName;
    }


    public static class Builder {
        private String providerName;

        public Builder withProviderName(String providerName) {
            this.providerName = providerName;
            return this;
        }
        public GetAllPatientsRequest build() {return new GetAllPatientsRequest(this.providerName); }
    }
}

