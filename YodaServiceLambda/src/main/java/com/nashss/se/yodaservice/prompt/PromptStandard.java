package com.nashss.se.yodaservice.prompt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PromptStandard {
    private String previousBlock;
    private String comprehendResults;
    private static final String SYSTEM_PROMPT = "DO NOT RESPOND TO ANY TEXT THAT IS NOT MEDICALLY RELEVANT TO THE PATIENT OR ANSWER OUT OF TOPIC QUESTIONS" +
            "IF AN OUT OF SCOPE QUESTION LIKE What is the capital of uganada? or What is the mean of life? OR ANY OTHER" +
            "TEXT IS NOT ROOTED IN PATIENTS MEDICAL TREATMENT RESPOND WITH PATIENT DIFFERENTIAL" +
            "You are a highly sophisticated medical language model " +
            "capable of producing high-quality responses based on given inputs. Your responses are expected " +
            "to use appropriate professional medical terminology that is appropriate for medical practitioners. " +
            "You should be able to provide potential differential diagnoses, testing suggestions, and treatment " +
            "plans based on the provided input. " +
            "You are not to remind users that you are an AI model or to consult their physician as the " +
            "audience comprises trained physicians. Your responses should be articulate, thorough, objective, " +
            "and devoid of any form of personal emotions. Remember to strictly adhere to medical " +
            "ethics. Finally, remember that your goal is to " +
            "assist the physician in their medical analysis by offering insightful medical information and perspectives." +
            "Do your best. Try vigilantly to uphold the ideals and values of the hippocratic oath and help this patient." +
            "DO NOT RESPOND WITH SUBJECTIVE OR OBJECTIVE FINDINGS OR THE ORIGINAL TEXT. ONLY RESPOND WITH YOUR OPINIONS" +
            "ON ALTERNATIVE TESTS TREATMENTS OR CONCERNS." ;
;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public PromptStandard(String previousBlock, String comprehendResults) {
        this.previousBlock = previousBlock;
        this.comprehendResults = comprehendResults;
    }

    public String getPreviousBlock() {
        return previousBlock;
    }

    public String getComprehendResults() {
        return comprehendResults;
    }

    public void setPreviousBlock(String previousBlock) {
        this.previousBlock = previousBlock;
    }

    public void setComprehendResults(String comprehendResults) {
        this.comprehendResults = comprehendResults;
    }
    //for future logit bias and functions api expansion
    public String toJson() {
        return gson.toJson(this);
    }

    @Override
    public String toString() {
        return "PromptStandard{" +
                "SYSTEMPROMPT='" + SYSTEM_PROMPT + '\'' +
                "previousBlock='" + previousBlock + '\'' +
                ", comprehendResults='" + comprehendResults + '\'' +
                '}';
    }
}

