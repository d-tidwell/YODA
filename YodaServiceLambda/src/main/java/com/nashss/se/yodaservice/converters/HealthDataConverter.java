package com.nashss.se.yodaservice.converters;

import software.amazon.awssdk.services.comprehendmedical.model.Attribute;
import software.amazon.awssdk.services.comprehendmedical.model.Entity;
import software.amazon.awssdk.services.comprehendmedical.model.Trait;

import java.util.*;

public class HealthDataConverter {
    public Map<String, Map<String, Map<String, List<Map<String, Object>>>>> parse(List<Entity> entities) {
        Map<String, Map<String, Map<String, List<Map<String, Object>>>>> healthData = new HashMap<>();
        healthData.put("ANATOMY", new HashMap<>());
        healthData.put("BEHAVIORAL_ENVIRONMENTAL_SOCIAL", new HashMap<>());
        healthData.put("MEDICAL_CONDITION", new HashMap<>());
        healthData.put("MEDICATION", new HashMap<>());
        healthData.put("PROTECTED_HEALTH_INFORMATION", new HashMap<>());
        healthData.put("TEST_TREATMENT_PROCEDURE", new HashMap<>());
        healthData.put("TIME_EXPRESSION", new HashMap<>());

        for (Entity entity : entities) {
            Map<String, Object> currentTextMap = null;
            String currentCategory = null;
            String currentType = null;
            String currentText = null;
            if (entity.category() != null) {
                currentCategory = entity.categoryAsString();
                currentType = !entity.type().toString().equals("null") ? entity.type().toString() : "TYPE";
                currentText = entity.text();

                // Create a new map for this text
                currentTextMap = new HashMap<>();
                if (healthData.containsKey(currentCategory)) {

                    if (!healthData.get(currentCategory).containsKey(currentType)) {
                        healthData.get(currentCategory).put(currentType, new HashMap<>());
                    }
                    if (!healthData.get(currentCategory).get(currentType).containsKey(currentText)) {
                        healthData.get(currentCategory).get(currentType).put(currentText, new ArrayList<>());
                    }
                    healthData.get(currentCategory).get(currentType).get(currentText).add(currentTextMap);

                    if(entity.traits().size() > 0) {
                        List<String> traitsList = new ArrayList<>();
                        for (Trait trait : entity.traits()) {
                            // Add trait to the current text map
                            traitsList.add(trait.nameAsString());
                        }
                        currentTextMap.put("Traits", traitsList);
                    }
                    Map<String, List<String>> attributeMap = new HashMap<>();
                    for (Attribute attribute : entity.attributes()) {
                        // if its a duplicate key
                        if (attributeMap.containsKey(attribute.type().toString())) {
                            attributeMap.get(attribute.type().toString()).add(attribute.text());
                        }
                        //if the value of the type is null
                        else if (!attribute.type().toString().equals("null")) {
                            attributeMap.put(attribute.type().toString(), new ArrayList<>(Arrays.asList(attribute.text())));
                        } else {
                            attributeMap.put("ATTRIBUTE TYPE", new ArrayList<>(Arrays.asList(attribute.text())));
                        }
                    }
                    currentTextMap.putAll(attributeMap);

                }
            }
        }

        return healthData;
    }
}

