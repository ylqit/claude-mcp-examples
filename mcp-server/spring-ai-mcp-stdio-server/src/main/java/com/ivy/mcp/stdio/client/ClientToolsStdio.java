package com.ivy.mcp.stdio.client;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.ListToolsResult;

import java.util.Map;

/**
 * With stdio transport, the MCP server is automatically started by the client. But you
 * have to build the server jar first:
 */
public class ClientToolsStdio {

    public static void main(String[] args) {

        var stdioParams = ServerParameters.builder("java")
                .args("-jar",
                        "mcp-server/spring-ai-mcp-stdio-server/target/spring-ai-mcp-stdio-server-1.0.0-SNAPSHOT.jar")
                .build();

        var transport = new StdioClientTransport(stdioParams);
        try (var client = McpClient.sync(transport).build()) {

            client.initialize();

            ListToolsResult toolsList = client.listTools();
            System.out.println("Available Tools = " + toolsList);

             CallToolResult sumResult = client.callTool(new CallToolRequest("add",
                     Map.of("a", 1, "b", 2)));
             System.out.println("add a+ b =  " + sumResult.content().get(0));


             CallToolResult currentTimResult = client.callTool(new CallToolRequest("getCurrentTime", Map.of()));
             System.out.println("current time Response = " + currentTimResult);


            CallToolResult toUpperCaseResult = client.callTool(new CallToolRequest("toUpperCase", Map.of("input", "hello")));;
            System.out.println("toUpperCaseResult = " + toUpperCaseResult);
        }
    }

}
