package com.ivy.mcp.stdio.server.prompts;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PromptProvider {

    @Bean
    public List<McpServerFeatures.SyncPromptSpecification> promptRegistrations() {

        var prompt = new McpSchema.Prompt(
                "greeting",
                "A friendly greeting prompt",
                List.of(
                        new McpSchema.PromptArgument("name", "The name to greet", true)
                )
        );

        var promptRegistration = new McpServerFeatures.SyncPromptSpecification(
                prompt, (exchange, request) -> {

            String nameArgument = (String) request.arguments().get("name");
            if (nameArgument == null) {
                nameArgument = "friend";
            }

            var userMessage = new McpSchema.PromptMessage(McpSchema.Role.USER,
                    new McpSchema.TextContent("Hello " + nameArgument + "! How can I assist you today?"));

            return new McpSchema.GetPromptResult("A personalized greeting message", List.of(userMessage));
        });
        return List.of(promptRegistration);
    }
}
