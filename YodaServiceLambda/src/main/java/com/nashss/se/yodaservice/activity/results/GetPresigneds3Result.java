package com.nashss.se.yodaservice.activity.results;

public class GetPresigneds3Result {

    private final String fileName;

    private GetPresigneds3Result(String fileName) {
        this.fileName = fileName;
    }

    public String getfileName() {
        return this.fileName;
    }

    @Override
    public String toString() {
        return "GetPresigneds3Result{" +
                "fileName='" + fileName + '\'' +
                '}';
    }
    public static Builder builder(){return new Builder();}
    public static class Builder {
        private String fileName;

        public Builder withfileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public GetPresigneds3Result build() {
            return new GetPresigneds3Result(fileName);
        }
    }
}
