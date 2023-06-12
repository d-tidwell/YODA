package com.nashss.se.yodaservice.activity.results;

import com.nashss.se.yodaservice.dynamodb.models.PHR;
import com.nashss.se.yodaservice.models.PHRModel;

public class UpdateDictationResult {
    private final String status;
    private final PHRModel model;

    public UpdateDictationResult(String status, PHRModel model) {
        this.status = status;
        this.model = model;
    }

    @Override
    public String toString() {
        return "UpdateDictationResult{" +
                "status='" + status + '\'' +
                "model='" + model+ '\'' +
                '}';
    }
    //CHECKSTYLE:OFF:Builder
    public static Builder builder(){return new Builder();}

    public String getStatus() {
        return status;
    }

    public PHRModel getModel() {
        return model;
    }

    public static class Builder {
        private String status;
        private PHRModel model;

        public Builder withStatus(String status){
            this.status = status;
            return this;
        }
        public Builder withModel(PHRModel model){
            this.model = model;
            return this;
        }

        public UpdateDictationResult build(){return new UpdateDictationResult(status, model);}
    }
}
