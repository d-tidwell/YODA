package com.nashss.se.yodaservice.activity.results;

import com.nashss.se.yodaservice.models.PHRModel;

import java.util.List;

public class GetPHRRangeResult{
    private final List<PHRModel> PHRId;

    public GetPHRRangeResult(List<PHRModel> PHRId) {
        this.PHRId = PHRId;
    }

    @Override
    public String toString() {
        return "GetPHRRangeResult{" +
                "PHRModels=" + PHRId +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<PHRModel> PHRId;

        public Builder withPHRId(List<PHRModel> PHRId){
            this.PHRId = PHRId;
            return this;
        }

        public GetPHRRangeResult build() {
            return new GetPHRRangeResult(PHRId);
        }
    }

}