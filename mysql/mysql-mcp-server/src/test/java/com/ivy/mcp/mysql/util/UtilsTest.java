package com.ivy.mcp.mysql.util;


import java.sql.SQLException;
import java.util.List;

class UtilsTest {

    public static void main(String[] args) throws SQLException {
        List<String> tables = MySQLUtil.showTables();
        System.out.println(tables);

        String sql = "select * from todo_info";
        String query = MySQLUtil.query(sql);
        System.out.println(query);

        String resources = MySQLUtil.readTableResources("todo_info");
        System.out.println(resources);
    }
}