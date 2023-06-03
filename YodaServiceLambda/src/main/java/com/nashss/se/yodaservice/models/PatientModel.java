package com.nashss.se.yodaservice.models;

import java.util.Objects;

public class PatientModel {
    private final String name;
    private final String age;
    private final String id;

    public PatientModel(String name, String age, String id) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PatientModel)) return false;
        PatientModel that = (PatientModel) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getAge(), that.getAge()) && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAge(), getId());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String name;
        private String age;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withAge(String age) {
            this.age = age;
            return this;
        }

        public Builder withId(String id){
            this.id = id;
            return this;

        }
        public PatientModel build() {return new PatientModel(name, age, id); }
    }
}
