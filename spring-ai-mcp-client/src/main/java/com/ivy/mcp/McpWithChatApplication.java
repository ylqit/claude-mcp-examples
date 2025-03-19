package com.ivy.mcp;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
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
        private SyncMcpToolCallbackProvider toolCallbackProvider;

        @GetMapping("/chat")
        public String call(@RequestParam String input) {
            ChatClient chatClient = ChatClient.builder(ollamaChatModel)
                    .defaultTools(toolCallbackProvider.getToolCallbacks())
                    .build();
            return chatClient.prompt(input).call().content();
        }
    }
}
