package com.nashss.se.yodaservice.models;

import java.util.Objects;

public class PatientModel {
    private final String name;
    private final String age;

    public PatientModel(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PatientModel)) return false;
        PatientModel that = (PatientModel) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getAge(), that.getAge());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAge());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
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

        public PatientModel build() {return new PatientModel(name, age); }
    }
}
