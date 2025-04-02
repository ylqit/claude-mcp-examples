package com.ivy.mcp.mysql.util;

public class StrUtil {
    public static String getTableName(String uri) {
        if (uri == null || uri.trim().isEmpty()) {
            throw new IllegalArgumentException("uri invalid: " + uri);
        }
        return uri.substring("mysql://".length()).split("/")[0];
    }

    public static final String RESOURCE_URI ="mysql://{table}/data";

    public static final String MYSQL_SYSTEM_PROMPT = "mysql-default-system-prompt";
    public static final String MYSQL_RESOURCE_TOOL_NAME = "mysql_resources_tool";
    public static final String MYSQL_EXECUTE_TOOL= "mysql_execute_sql";
}
