package com.nashss.se.yodaservice.activity.results;

public class GetPresigneds3Result {

    private final String URL;

    private GetPresigneds3Result(String fileName) {
        this.URL = fileName;
    }

    public String getURL() {
        return this.URL;
    }

    @Override
    public String toString() {
        return "GetPresigneds3Result{" +
                "URL='" + URL + '\'' +
                '}';
    }
    //CHECKSTYLE:OFF:Builder
    public static Builder builder(){return new Builder();}
    public static class Builder {
        private String URL;

        public Builder withURL(String fileName) {
            this.URL = URL;
            return this;
        }

        public GetPresigneds3Result build() {
            return new GetPresigneds3Result(URL);
        }
    }
}
