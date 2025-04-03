package com.ivy.mcp.controller;

import com.ivy.mcp.providers.SyncTemplateProvider;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ChatController {
    @Resource
    private OpenAiChatModel openAiChatModel;

    @Resource
    private SyncMcpToolCallbackProvider toolCallbackProvider;

    @Resource
    private SyncTemplateProvider syncTemplateProvider;

    @GetMapping("/chat")
    public String call(@RequestParam String input) {
        ToolCallback[] toolCallbacks = toolCallbackProvider.getToolCallbacks();
        ChatClient chatClient = ChatClient.builder(openAiChatModel)

                .defaultSystem(syncTemplateProvider.getSystemPrompt(toolCallbacks))
                .defaultTools(toolCallbacks)
                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
                .build();
        return chatClient.prompt(input).call().content();
    }

}
