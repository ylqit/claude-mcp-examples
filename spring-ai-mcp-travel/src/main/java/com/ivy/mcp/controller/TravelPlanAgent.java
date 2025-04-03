package com.ivy.mcp.controller;

import com.ivy.mcp.prompt.PromptUtil;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class TravelPlanAgent {

    @Resource
    private OpenAiChatModel openAiChatModel;

    @Resource
    private PromptUtil promptUtil;

    @Resource
    private SyncMcpToolCallbackProvider toolCallbackProvider;

    @GetMapping("/travel")
    public Flux<String> call(@RequestParam String input) {
        ChatClient chatClient = ChatClient.builder(openAiChatModel)
                .defaultTools(toolCallbackProvider.getToolCallbacks())
                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
                .build();
        return chatClient.prompt(promptUtil.create(input)).stream().content();
    }
}
