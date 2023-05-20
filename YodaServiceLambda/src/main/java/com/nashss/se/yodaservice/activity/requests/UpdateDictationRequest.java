package com.nashss.se.yodaservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = GetAllPHRRequest.Builder.class)
public class UpdateDictationRequest {

    private final String fileName;

    private UpdateDictationRequest(String fileName) {
        this.fileName = fileName;
    }

    public String getfileName() {
        return this.fileName;
    }

    @Override
    public String toString() {
        return "UpdateDictationRequest{" +
                "fileName='" + fileName + '\'' +
                '}';
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String fileName;

        public Builder withfileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public UpdateDictationRequest build() {
            return new UpdateDictationRequest(fileName);
        }
    }
}