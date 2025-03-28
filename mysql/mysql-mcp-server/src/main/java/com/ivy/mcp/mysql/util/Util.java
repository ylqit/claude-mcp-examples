package com.ivy.mcp.mysql.util;

public class Util {
    public static String getTableName(String uri) {
        if (uri == null || uri.trim().isEmpty()) {
            throw new IllegalArgumentException("uri invalid: " + uri);
        }
        return uri.substring("mysql://".length()).split("/")[0];
    }
}
