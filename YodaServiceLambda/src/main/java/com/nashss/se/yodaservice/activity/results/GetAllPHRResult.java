package com.nashss.se.yodaservice.activity.results;

import com.nashss.se.yodaservice.dynamodb.models.PHR;
import com.nashss.se.yodaservice.models.PHRModel;

import java.util.List;

public class GetAllPHRResult {
    private final List<PHRModel> PHRId;

    public GetAllPHRResult(List<PHRModel> PHRId) {
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
        private List<PHRModel> PHRId;

        public Builder withPHRId(List<PHRModel> PHRId){
            this.PHRId = PHRId;
            return this;
        }

        public GetAllPHRResult build() {
            return new GetAllPHRResult(PHRId);
        }
    }

}
