package com.ivy.mcp.providers;

import io.modelcontextprotocol.client.McpSyncClient;
import jakarta.annotation.Resource;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fangjie33
 * @description 获取指定的提示词模板
 */
@Component
public class SyncTemplateProvider {

    private final List<McpSyncClient> mcpClients;

    @Resource
    private SyncMcpToolCallbackProvider toolCallbackProvider;

    public SyncTemplateProvider(List<McpSyncClient> mcpClients) {
        this.mcpClients = mcpClients;
    }

    public SyncTemplateProvider(McpSyncClient... mcpClients) {
        this.mcpClients = List.of(mcpClients);
    }

    public String getTemplate(String templateName) {
        return "";
    }
}