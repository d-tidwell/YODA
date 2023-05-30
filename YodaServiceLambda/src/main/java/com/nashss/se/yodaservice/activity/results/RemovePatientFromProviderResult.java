package com.nashss.se.yodaservice.activity.results;

public class RemovePatientFromProviderResult {
    private final boolean success;

    public RemovePatientFromProviderResult(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "RemovePatientFromProvideResult{" +
                "success=" + success +
                "}";
    }

    //CHECKSTYLE:OFF:Builder
    public static RemovePatientFromProviderResult.Builder builder() {
        return new RemovePatientFromProviderResult.Builder();
    }

    public boolean getSuccess() {
        return success;
    }

    public static class Builder {
        private boolean success;

        public RemovePatientFromProviderResult.Builder withSuccess(boolean success){
            this.success = success;
            return this;
        }

        public RemovePatientFromProviderResult build() {
            return new RemovePatientFromProviderResult(success);
        }
    }
}
