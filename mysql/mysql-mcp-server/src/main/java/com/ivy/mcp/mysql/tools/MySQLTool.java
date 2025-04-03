package com.ivy.mcp.mysql.tools;

import com.ivy.mcp.mysql.util.MySQLUtil;
import com.ivy.mcp.mysql.util.StrUtil;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static com.ivy.mcp.mysql.util.StrUtil.MYSQL_EXECUTE_TOOL;

/**
 * @author ivy
 */
public class MySQLTool {

    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLTool.class);

    public static McpServerFeatures.SyncToolSpecification SyncMySQLTool() {
        return new McpServerFeatures.SyncToolSpecification(executeSqlTool(),
                ((exchange, args) -> {
                    LOGGER.debug("Executing SQL sql: {}", args.get("query"));
                    try {
                        final String query = String.valueOf(args.get("query")).toUpperCase();
                        if (query.startsWith("SELECT")) {
                            String result = MySQLUtil.query(query);
                            return new McpSchema.CallToolResult(List.of(new McpSchema.TextContent(result)), false);
                        } else if (query.startsWith("SHOW TABLES")) {
                            String result = String.join("/n", MySQLUtil.showTables());
                            return new McpSchema.CallToolResult(List.of(new McpSchema.TextContent(result)), false);
                        } else {
                            boolean result = MySQLUtil.update(query);

                            return new McpSchema.CallToolResult(List.of(
                                    new McpSchema.TextContent(result ? "Executed success" : "Executing failed")),
                                    false);
                        }
                    } catch (final SQLException e) {
                        return new McpSchema.CallToolResult(
                                List.of(
                                        new McpSchema.TextContent(String.format("Error executing query: %s", e))),
                                true);
                    }
                }));
    }

    public static McpSchema.Tool executeSqlTool() {
        return new McpSchema.Tool(MYSQL_EXECUTE_TOOL, "Execute an SQL query on the MySQL server",
                """
                                {
                                    "type": "object",
                                    "properties": {
                                        "query": {
                                            "type": "string",
                                            "description": "The SQL query to execute"
                                        }
                                    },
                                    "required": [
                                        "query"
                                    ]
                                }
                        """);
    }

    public static String SyncListResources() {
        try {
            return MySQLUtil.showTables().stream()
                    .map(tableName -> new ResourceDes(
                            tableName,
                            StrUtil.RESOURCE_URI.replace("{table}", tableName),
                            "")
                            .toString()
                    )
                    .collect(Collectors.joining("\n"));
        } catch (SQLException e) {
            LOGGER.error("list all resources failed, error:{}", e.getMessage(), e);
            return "";
        }
    }

    public record ResourceDes(String name, String uri, String description) {
    }
}
