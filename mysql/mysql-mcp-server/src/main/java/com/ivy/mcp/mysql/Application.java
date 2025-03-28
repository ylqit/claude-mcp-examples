package com.ivy.mcp.mysql;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpServerTransportProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ivy.mcp.mysql.McpMySQLServer.syncServer;

public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args) {
        McpServerTransportProvider transport = new StdioServerTransportProvider(new ObjectMapper());
        McpSyncServer mcpSyncServer = syncServer(transport);
        LOGGER.info("McpSyncServer started, server info: {}", mcpSyncServer.getServerInfo());
    }
}
