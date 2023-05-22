package com.nashss.se.yodaservice.activity.results;

public class GetPresigneds3Result {

    private final String url;

    private GetPresigneds3Result(String fileName) {
        this.url = fileName;
    }

    public String geturl() {
        return this.url;
    }

    @Override
    public String toString() {
        return "GetPresigneds3Result{" +
                "url='" + url + '\'' +
                '}';
    }
    //CHECKSTYLE:OFF:Builder
    public static Builder builder(){return new Builder();}
    public static class Builder {
        private String url;

        public Builder withUrl(String fileName) {
            this.url = url;
            return this;
        }

        public GetPresigneds3Result build() {
            return new GetPresigneds3Result(url);
        }
    }
}
