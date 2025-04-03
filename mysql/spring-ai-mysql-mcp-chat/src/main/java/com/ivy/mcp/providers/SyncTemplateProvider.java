package com.ivy.mcp.providers;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import jakarta.annotation.Resource;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author fangjie33
 * @description 获取指定的提示词模板
 */
@Component
public class SyncTemplateProvider {
    private final List<McpSyncClient> mcpSyncClients;

    public SyncTemplateProvider(List<McpSyncClient> mcpSyncClients) {
        this.mcpSyncClients = mcpSyncClients;
    }

    public String getSystemPrompt(ToolCallback[] toolCallbacks) {
        if (CollectionUtils.isEmpty(List.of(toolCallbacks))) {
            return "";
        }
        McpSyncClient mysqlMcpSyncClient = mcpSyncClients.get(0);
        String toolDescriptions = List.of(toolCallbacks)
                .stream()
                .map(toolCallback -> {
                    return toolCallback.getToolDefinition().toString();
                }).collect(Collectors.joining("/n"));

        McpSchema.GetPromptResult prompt = mysqlMcpSyncClient.getPrompt(
                new McpSchema.GetPromptRequest(
                        "mysql-default-system-prompt",
                        Map.of("tools_description", toolDescriptions)
                )
        );
        return prompt.messages()
                .stream()
                .map(promptMessage -> ((McpSchema.TextContent) promptMessage.content()).text())
                .collect(Collectors.joining("\n"));
    }
}