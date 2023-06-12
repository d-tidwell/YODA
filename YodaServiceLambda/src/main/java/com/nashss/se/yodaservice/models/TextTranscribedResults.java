package com.nashss.se.yodaservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class TextTranscribedResults {
    private List<TranscriptJSON> transcripts;
    // getters and setters
    public List<TranscriptJSON> getTranscripts() {
        return transcripts;
    }

    public void setTranscripts(List<TranscriptJSON> transcripts) {
        this.transcripts = transcripts;
    }


}

