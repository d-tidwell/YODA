package com.nashss.se.yodaservice.converters;

import software.amazon.awssdk.services.comprehendmedical.model.Attribute;
import software.amazon.awssdk.services.comprehendmedical.model.Entity;
import software.amazon.awssdk.services.comprehendmedical.model.Trait;

import java.util.*;




//
//public class HealthDataConverter {
//
//    public Map<String, Map<String, Map<String, List<Map<String, Object>>>>> parse(List<Entity> entities) {
//        Map<String, Map<String, Map<String, List<Map<String, Object>>>>> healthData = new HashMap<>();
//        Map<String, Object> currentTextMap = null;
//        String currentCategory = null;
//        String currentType = null;
//        String currentText = null;
//
//        for (Entity entity : entities) {
//            if (entity.category() != null) {
//                currentCategory = entity.categoryAsString();
//                currentType = entity.type() != null ? entity.type().toString() : "NA";
//                currentText = entity.text();
//
//                // Create a new map for this text
//                currentTextMap = new HashMap<>();
//                if (!healthData.containsKey(currentCategory)) {
//                    healthData.put(currentCategory, new HashMap<>());
//                }
//                if (!healthData.get(currentCategory).containsKey(currentType)) {
//                    healthData.get(currentCategory).put(currentType, new HashMap<>());
//                }
//                if (!healthData.get(currentCategory).get(currentType).containsKey(currentText)) {
//                    healthData.get(currentCategory).get(currentType).put(currentText, new ArrayList<>());
//                }
//                healthData.get(currentCategory).get(currentType).get(currentText).add(currentTextMap);
//
//                if(entity.traits().size() > 0) {
//                    List<String> traitsList = new ArrayList<>();
//                    for (Trait trait : entity.traits()) {
//                        // Add trait to the current text map
//                        traitsList.add(trait.nameAsString());
//                    }
//                    currentTextMap.put("Traits", traitsList);
//                }
//
//                for (Attribute attribute : entity.attributes()) {
//                    // Add attribute to the current text map
//                    Map<String, Object> attributeMap = new HashMap<>();
//                    if (attribute.type() != null) {
//                        attributeMap.put(attribute.type().toString(), attribute.text());
//                        currentTextMap.putAll(attributeMap);
//                    } else {
//                        attributeMap.put("ATTRIBUTE TYPE", attribute.text());
//                        currentTextMap.putAll(attributeMap);
//                    }
//
//                }
//            }
//        }
//
//        return healthData;
//    }
//}

