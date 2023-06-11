package com.nashss.se.yodaservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nashss.se.yodaservice.models.Entity;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse {
    private String jobName;
    private String accountId;

    private TextTranscribedResults results;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public TextTranscribedResults getTextTranscribedResults() {
        return results;
    }

    public void setResults(TextTranscribedResults results) {
        this.results = results;
    }




    // getter and setter
}
