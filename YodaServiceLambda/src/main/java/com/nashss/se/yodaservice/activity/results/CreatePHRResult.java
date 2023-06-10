package com.nashss.se.yodaservice.activity.results;

import com.nashss.se.yodaservice.models.PHRModel;

public class CreatePHRResult {
    private final PHRModel newPHR;

    public CreatePHRResult(PHRModel newPHR) {
        this.newPHR = newPHR;
    }

    public PHRModel getPHR() {
        return newPHR;
    }

    @Override
    public String toString() {
        return "CreatePHRResult{" +
                "PHR=" + newPHR +
                "}";
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private PHRModel newPHR;

        public Builder withPHR(PHRModel newPHR){
            this.newPHR = newPHR;
            return this;
        }

        public CreatePHRResult build() {
            return new CreatePHRResult(newPHR);
        }
    }

}
