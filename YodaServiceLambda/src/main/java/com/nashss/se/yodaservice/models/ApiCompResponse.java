package com.nashss.se.yodaservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiCompResponse {
    public List<Entity> getEntities() {
        return Entities;
    }

    public void setEntities(List<Entity> entities) {
        Entities = entities;
    }

    private List<Entity> Entities;

    // getter and setter
}
