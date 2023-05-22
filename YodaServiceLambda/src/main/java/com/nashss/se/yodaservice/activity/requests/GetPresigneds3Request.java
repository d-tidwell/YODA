package com.nashss.se.yodaservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = GetAllPHRRequest.Builder.class)
public class GetPresigneds3Request {

    private final String fileName;
    private final String phrId;
    private final String date;

    private GetPresigneds3Request(String fileName, String phrId, String date) {
        this.fileName = fileName;
        this.phrId = phrId;
        this.date = date;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getPhrId() {
        return this.phrId;
    }

    public String getDate() {
        return this.date;
    }

    @Override
    public String toString() {
        return "GetPresigneds3Request{" +
                "fileName='" + fileName + '\'' +
                ", phrId='" + phrId + '\'' +
                ", date=" + date +
                '}';
    }

    public static Builder builder(){return new Builder();}

    @JsonPOJOBuilder
    public static class Builder {
        private String fileName;
        private String phrId;
        private String date;

        public Builder withFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder withPhrId(String phrId) {
            this.phrId = phrId;
            return this;
        }

        public Builder withDate(String date) {
            this.date = date;
            return this;
        }

        public GetPresigneds3Request build() {
            return new GetPresigneds3Request(fileName, phrId, date);
        }
    }
}
