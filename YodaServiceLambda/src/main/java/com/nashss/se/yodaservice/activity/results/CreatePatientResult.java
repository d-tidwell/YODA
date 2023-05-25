package com.nashss.se.yodaservice.activity.results;

public class CreatePatientResult {

    private final boolean success;

    public CreatePatientResult(boolean success) {
        this.success = success;
    }

    public String getSuccess() {
        return String.valueOf(success);
    }

    @Override
    public String toString() {
        return "CreatePatientResult{" +
                "success=" + success +
                "}";
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean success;

        public Builder withSuccess(boolean success){
            this.success = success;
            return this;
        }

        public CreatePatientResult build() {
            return new CreatePatientResult(success);
        }
    }

}

