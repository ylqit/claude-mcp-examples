package com.ivy.mcp;

import jakarta.annotation.Resource;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
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
        @Resource
        private OllamaChatModel ollamaChatModel;

        @Resource
        private OpenAiChatModel openAiChatModel;

        @Resource
        private SyncMcpToolCallbackProvider toolCallbackProvider;

        @GetMapping("/chat")
        public String call(@RequestParam String input) {
            ChatClient chatClient = ChatClient.builder(ollamaChatModel)
                    .defaultTools(toolCallbackProvider.getToolCallbacks())
                    .defaultAdvisors(new SimpleLoggerAdvisor(req->req.userText(), req->req.toString(), 0))
                    .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
                    .build();
            return chatClient.prompt(input).call().content();
        }
    }
}
