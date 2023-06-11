package com.nashss.se.yodaservice.models;

import java.util.List;

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

