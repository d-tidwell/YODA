package com.nashss.se.yodaservice.activity.results;

public class CreateProviderResult {

    private final boolean success;

    public CreateProviderResult(boolean success) {
        this.success = success;
    }

    public String getSuccess() {
        return String.valueOf(success);
    }

    @Override
    public String toString() {
        return "CreateProviderResult{" +
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

        public CreateProviderResult build() {
            return new CreateProviderResult(success);
        }
    }

}