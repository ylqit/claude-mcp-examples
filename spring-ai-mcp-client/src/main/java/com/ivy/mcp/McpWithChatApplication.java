package com.ivy.mcp;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class McpWithChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpWithChatApplication.class, args);
    }

    @RestController
    public static class ChatController {

        @Value("${ai.model}")
        private String aiMode;

        @Resource
        private OllamaChatModel ollamaChatModel;

        @Resource
        private OpenAiChatModel openAiChatModel;

        @Resource
        private SyncMcpToolCallbackProvider toolCallbackProvider;

        @GetMapping("/chat")
        public String call(@RequestParam String input) {
            ChatClient chatClient = ChatClient.builder(StringUtils.equals(aiMode, "ollama") ? ollamaChatModel : openAiChatModel)
                    .defaultTools(toolCallbackProvider.getToolCallbacks())
                    .defaultAdvisors(new SimpleLoggerAdvisor(AdvisedRequest::userText, ChatResponse::toString, 0))
                    .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
                    .build();
            return chatClient.prompt(input).call().content();
        }
    }
}
