package com.ivy.mcp.mysql.util;


import java.sql.SQLException;

class UtilsTest {

    public static void main(String[] args) throws SQLException {
        String s = MySQLUtil.showTablesTool();
        System.out.println(s);

        String sql = "select * from todo_info";
        String query = MySQLUtil.queryTool(sql);
        System.out.println(query);

        String resources = MySQLUtil.readTableResources("todo_info");
        System.out.println(resources);
    }
}