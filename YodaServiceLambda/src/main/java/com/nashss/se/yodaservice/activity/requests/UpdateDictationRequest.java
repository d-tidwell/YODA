package com.nashss.se.yodaservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = GetAllPHRRequest.Builder.class)
public class UpdateDictationRequest {

    private final String PhrId;
    private final String PhrDate;
    private final String fileName;

    private UpdateDictationRequest(String PhrId, String phrDate, String fileName) {
        this.PhrId = PhrId;
        this.PhrDate = phrDate;
        this.fileName = fileName;
    }

    public String getPhrId() {
        return this.PhrId;
    }

    public String getPhrDate() {return PhrDate;}
    public String getFileName(){return this.fileName;}

    @Override
    public String toString() {
        return "UpdateDictationRequest{" +
                "PhrId='" + PhrId + '\'' +
                "PhrDate='" + PhrDate + '\'' +
                "fileName='" + fileName + '\'' +
                '}';
    }
    public static Builder builder(){return new Builder();}

    @JsonPOJOBuilder
    public static class Builder {
        private String PhrId;
        private String PhrDate;
        private String fileName;

        public Builder withPhrId(String PhrId) {
            this.PhrId = PhrId;
            return this;
        }
        public Builder withPhrDate(String PhrDate) {
            this.PhrDate = PhrDate;
            return this;
        }
        public Builder withFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }


        public UpdateDictationRequest build() {
            return new UpdateDictationRequest(PhrId, PhrDate, fileName);
        }
    }
}