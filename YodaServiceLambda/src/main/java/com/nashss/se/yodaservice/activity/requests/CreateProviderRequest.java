package com.nashss.se.yodaservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = CreateProviderRequest.Builder.class)
public class CreateProviderRequest {

    private final String providerName;

    private final String providerEmail;

    private CreateProviderRequest(String providerName, String providerEmail) {
        this.providerName = providerName;
        this.providerEmail = providerEmail;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getProviderEmail() {
        return providerEmail;
    }

    @Override
    public String toString() {
        return "CreateProviderRequest{" +
                "providerName='" + providerName + '\'' +
                ", providerEmail='" + providerEmail + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String providerName;
        private String providerEmail;

        public Builder withProviderName(String providerName) {
            this.providerName = providerName;
            return this;
        }

        public Builder withProviderEmail(String providerEmail) {
            this.providerEmail = providerEmail;
            return this;
        }

        public CreateProviderRequest build() {
            return new CreateProviderRequest(providerName, providerEmail);
        }
    }
}