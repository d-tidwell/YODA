package com.nashss.se.yodaservice.activity.results;

public class CreatePHRResult {
    private final String status;

    public CreatePHRResult(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CreatePHRResult{" +
                "status=" + status +
                "}";
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String status;

        public Builder withstatus(String status){
            this.status = status;
            return this;
        }

        public CreatePHRResult build() {
            return new CreatePHRResult(status);
        }
    }

}
