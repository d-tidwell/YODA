package com.nashss.se.yodaservice.activity.results;

import java.util.List;

public class GetAllPHRResult {
    private final List<String> PHRId;

    public GetAllPHRResult(List<String> PHRId) {
        this.PHRId = PHRId;
    }

    @Override
    public String toString() {
        return "GetAllPHRResult{" +
                "PHRId=" + PHRId +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<String> PHRId;

        public Builder withPHRId(List<String> PHRId){
            this.PHRId = PHRId;
            return this;
        }

        public GetAllPHRResult build() {
            return new GetAllPHRResult(PHRId);
        }
    }

}
