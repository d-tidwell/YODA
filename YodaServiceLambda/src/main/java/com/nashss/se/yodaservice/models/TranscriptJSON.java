package com.nashss.se.yodaservice.models;

public class TranscriptJSON {
    private String transcript;
    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }

    @Override
    public String toString() {
        return "TranscriptJSON{" +
                "transcript='" + transcript + '\'' +
                '}';
    }

}
