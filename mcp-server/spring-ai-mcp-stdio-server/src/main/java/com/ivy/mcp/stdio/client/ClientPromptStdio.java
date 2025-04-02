package com.ivy.mcp.stdio.client;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.Map;


public class ClientPromptStdio {

    public static void main(String[] args) {

        var stdioParams = ServerParameters.builder("java")
                .args("-jar",
                        "mcp-server/spring-ai-mcp-stdio-server/target/spring-ai-mcp-stdio-server-1.0.0-SNAPSHOT.jar")
                .build();

        var transport = new StdioClientTransport(stdioParams);
        try (var client = McpClient.sync(transport).build()) {

            client.initialize();

            McpSchema.ListPromptsResult listPromptsResult = client.listPrompts();
            System.out.println("Available Prompts = " + listPromptsResult);


            System.out.println("\n\n\n\n");
            McpSchema.GetPromptResult prompt = client.getPrompt(
                    new McpSchema.GetPromptRequest("greeting", Map.of("name", "ivy"))
            );

           prompt.messages().forEach(
                   message -> System.out.println(
                           message.role().name() + " -> " + ((McpSchema.TextContent)message.content()).text()
                   )
           );
        }
    }

}
