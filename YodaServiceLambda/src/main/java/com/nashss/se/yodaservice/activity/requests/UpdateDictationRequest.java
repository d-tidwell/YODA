package com.nashss.se.yodaservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = GetAllPHRRequest.Builder.class)
public class UpdateDictationRequest {

    private final String PhrId;

    private UpdateDictationRequest(String PhrId) {
        this.PhrId = PhrId;
    }

    public String getPhrId() {
        return this.PhrId;
    }

    @Override
    public String toString() {
        return "UpdateDictationRequest{" +
                "PhrId='" + PhrId + '\'' +
                '}';
    }
    public static Builder builder(){return new Builder();}
    @JsonPOJOBuilder
    public static class Builder {
        private String PhrId;

        public Builder withPhrId(String PhrId) {
            this.PhrId = PhrId;
            return this;
        }

        public UpdateDictationRequest build() {
            return new UpdateDictationRequest(PhrId);
        }
    }
}