package com.ivy.mcp.mysql;

import com.ivy.mcp.mysql.prompt.MySQLPrompt;
import com.ivy.mcp.mysql.resources.MySQLResource;
import com.ivy.mcp.mysql.tools.MySQLTool;
import com.ivy.mcp.mysql.util.MySQLUtil;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpServerTransportProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * @author ivy
 */
public class McpMySQLServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(McpMySQLServer.class);

    public static McpSyncServer syncServer(final McpServerTransportProvider transport) {
        LOGGER.info("Starting MySQL MCP Server...");
        try {
            final DatabaseMetaData metadata = MySQLUtil.getMetaData();
            LOGGER.info("Database Config : {} as {}", metadata.getURL(), metadata.getUserName());
        } catch (final SQLException e) {
            LOGGER.error("Database Connect Error : {} ", e.getMessage(), e);
            throw new IllegalStateException(String.format("Database error: %s", e.getMessage()), e);
        }
        final McpSyncServer syncServer = McpServer.sync(transport)
                .serverInfo("mysql-mcp-server", "1.0.0")
                .capabilities(McpSchema.ServerCapabilities.builder()
                        .resources(false, true)
                        .tools(true)
                        .prompts(true)
                        .logging()
                        .build())
                .prompts(MySQLPrompt.SyncPrompt())
                .tools(MySQLTool.SyncMySQLTool())
                .resources(MySQLResource.SyncResource())
                .build();

        syncServer.loggingNotification(McpSchema.LoggingMessageNotification.builder()
                .level(McpSchema.LoggingLevel.INFO)
                .logger("mysql-mcp-logger")
                .data("Server initialized")
                .build());

        LOGGER.info("MySQL MCP Server Started");
        return syncServer;
    }
}
