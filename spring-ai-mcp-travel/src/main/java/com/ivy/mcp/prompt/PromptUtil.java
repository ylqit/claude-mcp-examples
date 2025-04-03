package com.ivy.mcp.prompt;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PromptUtil {

    @Value("classpath:travel.st")
    private Resource travelPrompt;

    public Prompt create(String input) {
        PromptTemplate promptTemplate = new PromptTemplate(travelPrompt);
        return promptTemplate.create(Map.of("question", input));
    }
}
