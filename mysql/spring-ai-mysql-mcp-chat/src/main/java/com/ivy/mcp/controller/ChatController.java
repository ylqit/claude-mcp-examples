package com.ivy.mcp.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ChatController {
    @Resource
    private OllamaChatModel ollamaChatModel;

    @Resource
    private SyncMcpToolCallbackProvider toolCallbackProvider;

    @GetMapping("/chat")
    public String call(@RequestParam String input) {
        ChatClient chatClient = ChatClient.builder(ollamaChatModel)
                .defaultTools(toolCallbackProvider.getToolCallbacks())
                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
                .build();
        return chatClient.prompt(input).call().content();
    }


}
