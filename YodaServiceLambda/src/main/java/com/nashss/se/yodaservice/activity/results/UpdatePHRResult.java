package com.nashss.se.yodaservice.activity.results;

public class UpdatePHRResult {
    private final String success;

    public UpdatePHRResult(String success) {
        this.success = success;
    }

    public String withSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return "UpdatePHRResult{" +
                "success=" + success +
                "}";
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String success;

        public Builder withSuccess(String success){
            this.success = success;
            return this;
        }

        public UpdatePHRResult build() {
            return new UpdatePHRResult(success);
        }
    }

}
