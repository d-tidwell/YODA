package com.nashss.se.yodaservice.activity.results;

import java.util.List;

public class GetPHRRangeResult{
    private final List<String> PHRId;

    public GetPHRRangeResult(List<String> PHRId) {
        this.PHRId = PHRId;
    }

    @Override
    public String toString() {
        return "GetPHRRangeResult{" +
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

        public GetPHRRangeResult build() {
            return new GetPHRRangeResult(PHRId);
        }
    }

}