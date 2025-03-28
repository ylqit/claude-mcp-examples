package com.ivy.mcp.mysql;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpSchema;

import java.time.Duration;
import java.util.Map;

public class McpMySQLClient {
    public static void main(String[] args) {

        StdioClientTransport transport = new StdioClientTransport(
                ServerParameters.builder("java")
                        .env(Map.of(
                                        "MYSQL_USER", "root",
                                        "MYSQL_PASSWORD", "Admin123",
                                        "MYSQL_DATABASE", "jmt"
                                )
                        )
                        .args("-jar", "mysql/mcp-mysql-server/target/mcp-mysql-server-1.0.0-SNAPSHOT.jar")
                        .build(),
                new ObjectMapper()
        );
        try (McpSyncClient client = McpClient.sync(transport)
                .clientInfo(
                        new McpSchema.Implementation("mysql-mcp-client", "1.0.0")
                )
                .capabilities(
                        McpSchema.ClientCapabilities.builder().roots(true).sampling().build()
                )
                .requestTimeout(Duration.ofSeconds(60))
                .build()) {
            McpSchema.InitializeResult initialize = client.initialize();
            System.out.println("client initialized: " + initialize);


            McpSchema.ListToolsResult listToolsResult = client.listTools();

            listToolsResult.tools().forEach(tool -> {
                System.out.println(tool.name() + ": " + tool.description());
            });


            McpSchema.ListResourcesResult listResourcesResult = client.listResources();
            listResourcesResult.resources().forEach(resource -> {
                System.out.println(resource.name() + ": " + resource.description());
            });

            client.listPrompts().prompts().forEach(prompt -> {
                System.out.println(prompt.name() + ": " + prompt.description());
            });
        }
    }
}
