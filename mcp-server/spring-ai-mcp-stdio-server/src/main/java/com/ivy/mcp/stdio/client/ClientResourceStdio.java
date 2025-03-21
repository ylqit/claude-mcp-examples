package com.ivy.mcp.stdio.client;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.List;


public class ClientResourceStdio {

    public static void main(String[] args) {

        var stdioParams = ServerParameters.builder("java")
                .args("-jar",
                        "mcp-server/spring-ai-mcp-stdio-server/target/spring-ai-mcp-stdio-server-1.0.0-SNAPSHOT.jar")
                .build();

        var transport = new StdioClientTransport(stdioParams);
        try (var client = McpClient.sync(transport).build()) {

            client.initialize();

            McpSchema.ListResourcesResult resources = client.listResources();
            System.out.println("Available Resources = " + resources);

            McpSchema.ReadResourceResult readResourceResult = client.readResource(
                    new McpSchema.ReadResourceRequest("system://info")
            );

            System.out.println("--------------------------------------------");
            List<McpSchema.ResourceContents> contents = readResourceResult.contents();
            contents.forEach(content -> {
                if (content.mimeType().equals("application/json")) {
                    McpSchema.TextResourceContents text = (McpSchema.TextResourceContents) content;
                    System.out.println(text.text());
                }
            });
        }
    }

}
