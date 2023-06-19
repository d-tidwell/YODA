package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.AiRequest;
import com.nashss.se.yodaservice.activity.results.AiResult;
import com.nashss.se.yodaservice.dynamodb.PHRDAO;
import com.nashss.se.yodaservice.dynamodb.models.PHR;
import com.nashss.se.yodaservice.prompt.PromptStandard;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import javax.inject.Inject;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AiActivity {

    private final OpenAiService openAiService;
    private final PHRDAO phrdao;

    @Inject
    public AiActivity(OpenAiService openAiService1, PHRDAO phrdao) {
        this.openAiService = openAiService1;
        this.phrdao = phrdao;
    }

    public AiResult handleRequest(final AiRequest request) {
        try {
            PHR existingPhr = phrdao.getPHR(request.getPhrId(), request.getDate());
            PromptStandard prompt = new PromptStandard(existingPhr.getComprehendData(), existingPhr.getTranscription());
            List<ChatMessage> messages = new ArrayList<>();
            ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(),prompt.toString());
            messages.add(userMessage);
            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                    .model("gpt-4")
                    .messages(messages)
                    .logitBias(new HashMap<>())
                    .build();
            try {
                System.out.println("Completion initiated");
                String completion = openAiService.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage().getContent();
                return AiResult.builder()
                        .withDifferential(completion)
                        .build();
            } catch (RuntimeException RE) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                RE.printStackTrace(pw);
                throw new RuntimeException("Error while creating chat completion with OpenAI service: \n" + sw);
            }
        } catch (Exception RE) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            RE.printStackTrace(pw);
            throw new RuntimeException("Error while receiving completion data: \n" + sw);
        }
    }

}

