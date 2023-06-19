package com.nashss.se.yodaservice.activity.results;

public class AiResult {
    private final String differential;

    public AiResult(String differential) {
        this.differential = differential;
    }

    public String getDifferential() {
        return differential;
    }

    @Override
    public String toString() {
        return "AiResult{" +
                "differential='" + differential + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String differential;

        public Builder withDifferential(String differential){
            this.differential = differential;
            return this;
        }

        public AiResult build() {
            return new AiResult(differential);
        }
    }
}

