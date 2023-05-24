package com.nashss.se.yodaservice.activity.results;

public class GetPatientResult {
    private final String name;
    private final String age;
    public GetPatientResult(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return this.name;
    }
    
    public String getAge() {
        return this.age;
    }

    @Override
    public String toString() {
        return "GetPatientResult{" +
                "name='" + name + '\'' +
                "age='" + age + '\'' +
                '}';
    }
    //CHECKSTYLE:OFF:Builder
    public static Builder builder(){return new Builder();}

    public static class Builder {
        private String name;
        private String age;

        public Builder withName(String name){
            this.name = name;
            return this;
        }
        public Builder withAge(String age){
            this.age = age;
            return this;
        }
        public GetPatientResult build(){return new GetPatientResult(name, age);}
    }
}

