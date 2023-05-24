package com.nashss.se.yodaservice.activity.results;

import com.nashss.se.yodaservice.models.PHRModel;

import java.util.List;

public class GetAllPHRResult {
    private final List<PHRModel> phrId;

    public GetAllPHRResult(List<PHRModel> phrId) {
        this.phrId = phrId;
    }

    public List<PHRModel> getPhrId(){
        return this.phrId;
    }

    @Override
    public String toString() {
        return "GetAllPHRResult{" +
                "phrId=" + phrId +
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

        public GetAllPHRResult build() {
            return new GetAllPHRResult(phrId);
        }
    }

}
