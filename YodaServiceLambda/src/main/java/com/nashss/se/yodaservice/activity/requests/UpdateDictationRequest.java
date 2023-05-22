package com.nashss.se.yodaservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = GetAllPHRRequest.Builder.class)
public class UpdateDictationRequest {

    private final String phrId;
    private final String phrDate;
    private final String fileName;
    private final String type;

    private UpdateDictationRequest(String phrId, String phrDate, String fileName, String type) {
        this.phrId = phrId;
        this.phrDate = phrDate;
        this.fileName = fileName;
        this.type = type;
    }

    public String getPhrId() {
        return this.phrId;
    }

    public String getPhrDate() {
        return this.phrDate;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "UpdateDictationRequest{" +
                "phrId='" + phrId + '\'' +
                "phrDate='" + phrDate + '\'' +
                "fileName='" + fileName + '\'' +
                "type='" + type + '\'' +
                '}';
    }
    //CHECKSTYLE:OFF:Builder
    public static Builder builder(){return new Builder(); }

    @JsonPOJOBuilder
    public static class Builder {
        private String phrId;
        private String phrDate;
        private String fileName;
        private String type;

        public Builder withPhrId(String phrId) {
            this.phrId = phrId;
            return this;
        }
        public Builder withPhrDate(String phrDate) {
            this.phrDate = phrDate;
            return this;
        }
        public Builder withFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }
        public Builder withType(String type) {
            this.type = type;
            return this;
        }


        public UpdateDictationRequest build() {
            return new UpdateDictationRequest(phrId, phrDate, fileName, type);
        }
    }
}

