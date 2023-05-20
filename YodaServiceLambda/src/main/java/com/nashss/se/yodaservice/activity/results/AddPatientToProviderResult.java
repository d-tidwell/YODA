package com.nashss.se.yodaservice.activity.results;

public class AddPatientToProviderResult {
    private final boolean success;

    public AddPatientToProviderResult(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "AddPatientToProvideResult{" +
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

        public AddPatientToProviderResult build() {
            return new AddPatientToProviderResult(success);
        }
    }

}
