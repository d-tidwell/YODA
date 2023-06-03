package com.nashss.se.yodaservice.activity.results;

import java.util.List;

import com.nashss.se.yodaservice.models.PHRModel;

public class GetOpenPHRByProviderResult {
    private final List<PHRModel> phrId;

    public GetOpenPHRByProviderResult(List<PHRModel> phrId) {
        this.phrId = phrId;
    }

    public List<PHRModel> getPhrId(){
        return this.phrId;
    }

    @Override
    public String toString() {
        return "GetOpenPHRByProviderResult{" +
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

        public GetOpenPHRByProviderResult build() {
            return new GetOpenPHRByProviderResult(phrId);
        }
    }

}


