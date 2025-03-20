package com.ivy.mcp.sse.client;


import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.Map;

public class ClientWebmvc {

    public static void main(String[] args) {

        var transport = new HttpClientSseClientTransport("http://localhost:8080");
        try (var client = McpClient.sync(transport).build()) {

            client.initialize();

            McpSchema.ListToolsResult toolsList = client.listTools();
            System.out.println("Available Tools = " + toolsList);

            McpSchema.CallToolResult sumResult = client.callTool(new McpSchema.CallToolRequest("add",
                    Map.of("a", 1, "b", 2)));
            System.out.println("add a+ b =  " + sumResult.content().get(0));


            McpSchema.CallToolResult currentTimResult = client.callTool(new McpSchema.CallToolRequest("getCurrentTime", Map.of()));
            System.out.println("current time Response = " + currentTimResult);
        }
    }

}
