package com.nashss.se.yodaservice.activity.results;

public class UpdatePHRResult {
    private final boolean success;

    public UpdatePHRResult(boolean success) {
        this.success = success;
    }

    public boolean withSuccess() {
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
        private boolean success;

        public Builder withSuccess(boolean success){
            this.success = success;
            return this;
        }

        public UpdatePHRResult build() {
            return new UpdatePHRResult(success);
        }
    }

}
