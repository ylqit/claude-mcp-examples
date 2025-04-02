package com.ivy.mcp.mysql.resources;

import com.ivy.mcp.mysql.util.MySQLUtil;
import com.ivy.mcp.mysql.util.StrUtil;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

import static com.ivy.mcp.mysql.util.StrUtil.RESOURCE_URI;

public class MySQLResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLResource.class);
    public static McpServerFeatures.SyncResourceSpecification SyncResource() {
        return new McpServerFeatures.SyncResourceSpecification(
                new McpSchema.Resource(
                        RESOURCE_URI,
                        "MySQL table data",
                        "Contents of a MySQL table",
                        "text/plain",
                        null
                ),
                (exchange, request) -> {
                    String tableName = StrUtil.getTableName(request.uri());
                    LOGGER.debug("Reading MySQL table data, tableName: {}", tableName);
                    try {
                        return new McpSchema.ReadResourceResult(
                                List.of(
                                        new McpSchema.TextResourceContents(
                                                RESOURCE_URI.replace("{table}", tableName),
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
