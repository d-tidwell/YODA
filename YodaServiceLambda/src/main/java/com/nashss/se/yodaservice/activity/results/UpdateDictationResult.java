package com.nashss.se.yodaservice.activity.results;

public class UpdateDictationResult {
    private final String status;

    public UpdateDictationResult(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UpdateDictationResult{" +
                "status='" + status + '\'' +
                '}';
    }
    //CHECKSTYLE:OFF:Builder
    public static Builder builder(){return new Builder();}

    public static class Builder {
        private String status;

        public Builder withStatus(String status){
            this.status = status;
            return this;
        }

        public UpdateDictationResult build(){return new UpdateDictationResult(status);}
    }
}
