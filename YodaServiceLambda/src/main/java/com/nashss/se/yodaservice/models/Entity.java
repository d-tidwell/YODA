package com.nashss.se.yodaservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Entity {
    private String Text;
    private String Category;

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    private String Type;

    // getters and setters
}

