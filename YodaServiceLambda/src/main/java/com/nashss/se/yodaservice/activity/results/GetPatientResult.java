package com.nashss.se.yodaservice.activity.results;

public class GetPatientResult {
    private final String name;

    public GetPatientResult(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "GetPatientResult{" +
                "name='" + name + '\'' +
                '}';
    }
    //CHECKSTYLE:OFF:Builder
    public static Builder builder(){return new Builder();}

    public static class Builder {
        private String name;

        public Builder withName(String name){
            this.name = name;
            return this;
        }
        public GetPatientResult build(){return new GetPatientResult(name);}
    }


}
