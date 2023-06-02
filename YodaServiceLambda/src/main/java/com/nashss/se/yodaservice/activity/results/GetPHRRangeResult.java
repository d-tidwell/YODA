package com.nashss.se.yodaservice.activity.results;

import com.nashss.se.yodaservice.models.PHRModel;

import java.util.List;

public class GetPHRRangeResult {
    private final List<PHRModel> phrId;

    public GetPHRRangeResult(List<PHRModel> phrId) {
        this.phrId = phrId;
    }
    
    public List<PHRModel> getPhrId(){
        return phrId;
    }

    @Override
    public String toString() {
        return "GetPHRRangeResult{" +
                "PHRModels=" + phrId +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<PHRModel> phrId;

        public Builder withPhrId(List<PHRModel> phrId){
            this.phrId = phrId;
            return this;
        }

        public GetPHRRangeResult build() {
            return new GetPHRRangeResult(phrId);
        }
    }

}

