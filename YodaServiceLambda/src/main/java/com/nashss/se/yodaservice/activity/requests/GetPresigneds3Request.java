package com.nashss.se.yodaservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = GetAllPHRRequest.Builder.class)
public class GetPresigneds3Request {

    private final String fileName;

    private GetPresigneds3Request(String fileName) {
        this.fileName = fileName;
    }

    public String getfileName() {
        return this.fileName;
    }

    @Override
    public String toString() {
        return "GetPresigneds3Request{" +
                "fileName='" + fileName + '\'' +
                '}';
    }
    public static Builder builder(){return new Builder();}
    @JsonPOJOBuilder
    public static class Builder {
        private String fileName;

        public Builder withfileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public GetPresigneds3Request build() {
            return new GetPresigneds3Request(fileName);
        }
    }
}