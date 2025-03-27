package com.ivy.mcp.mysql.resources;

import com.ivy.mcp.mysql.util.MySQLUtil;
import com.ivy.mcp.mysql.util.Util;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class MySQLResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLResource.class);
    public static McpServerFeatures.SyncResourceSpecification SyncResource() {
        return new McpServerFeatures.SyncResourceSpecification(
                new McpSchema.Resource(
                        "mysql://{table}/data",
                        "MySQL table data",
                        "Contents of a MySQL table",
                        "text/plain",
                        null
                ),
                (exchange, request) -> {
                    String tableName = Util.getTableName(request.uri());
                    LOGGER.debug("Reading MySQL table data, tableName: {}", tableName);
                    try {
                        return new McpSchema.ReadResourceResult(
                                List.of(
                                        new McpSchema.TextResourceContents(
                                                "mysql://{table}/data".replace("{table}", tableName),
                                                "text/plain",
                                                MySQLUtil.readTableResources(tableName)
                                        )
                                )
                        );
                    } catch (final SQLException e) {
                        throw new IllegalStateException(String.format("Database error: %s", e.getMessage()), e);
                    }
                }
        );
    }
}
