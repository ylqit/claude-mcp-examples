package com.ivy.mcp.mysql.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MySQLUtil {
    private static final String SHOW_TABLES_SQL = "SHOW TABLES";
    private static final String SELECT_SQL = "SELECT * FROM %s LIMIT 500";

    /**
     * Get a connection to the MySQL database.
     *
     * @return a connection to the MySQL database
     */
    private static Connection getConnection() throws SQLException {
        final String host = getEnv("MYSQL_HOST", "localhost");
        final int port = Integer.parseInt(getEnv("MYSQL_PORT", "3306"));
        final String user = getEnv("MYSQL_USER", "root");
        final String password = getEnv("MYSQL_PASSWORD", "Admin123");
        final String database = getEnv("MYSQL_DATABASE", "jmt");

        final String url = String.format("jdbc:mysql://%s:%d/%s", host, port, database);
        return DriverManager.getConnection(url, user, password);
    }

    private static String getEnv(String key, String defaultValue) {
        final String val = System.getenv().getOrDefault(key, defaultValue);
        if (val == null) {
            throw new IllegalStateException(key + " is required");
        }
        return val;
    }

    /**
     * get the database metadata of the MySQL database.
     *
     * @return the database metadata of the MySQL database
     */
    public static DatabaseMetaData getMetaData() throws SQLException {
        return getConnection().getMetaData();
    }

    /**
     * show all tables in the MySQL database.
     *
     * @return a string of all tables in the MySQL database
     */
    public static String showTablesTool() throws SQLException {
        List<String> tables = new ArrayList<>();
        try (
                final Connection connection = getConnection();
                final Statement statement = connection.createStatement();
                final ResultSet rs = statement.executeQuery(SHOW_TABLES_SQL);
        ) {
            while (rs.next()) {
                tables.add(rs.getString(1));
            }
        }
        return String.join("/n", tables);
    }

    /**
     * execute a SQL statement and return the result set as a string.
     *
     * @param sql the SQL statement to execute
     * @return a string of the result set of the SQL statement
     */
    public static String queryTool(final String sql) throws SQLException {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            return parseColumn(rs);
        }
    }

    /**
     * execute a SQL statement.
     *
     * @param sql the SQL statement to execute
     * @return true if the statement is an update statement, false otherwise
     */
    public static boolean updateTool(final String sql) throws SQLException {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            return statement.execute(sql);
        }
    }

    /**
     * read the resources of a table.
     *
     * @param tableName the name of the table to read
     * @return a string of the resources of the table
     */
    public static String readTableResources(final String tableName) throws SQLException {
        if (tableName == null) {
            throw new NullPointerException("tableName is null");
        }
        final String sql = SELECT_SQL.formatted(tableName);
        try (final Connection connection = getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql);
             final ResultSet rs = statement.executeQuery()) {
            return parseColumn(rs);
        }
    }

    private static String parseColumn(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        StringBuilder queryContent = new StringBuilder();
        int columnCount = metaData.getColumnCount();
        List<String> columnNames = new ArrayList<>(columnCount);
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
        }
        // add column names
        queryContent.append(String.join(",", columnNames));
        // add next line
        queryContent.append("\n");

        // parse result set data
        List<String> line;
        while (rs.next()) {
            line = new ArrayList<>(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                final Object value = rs.getObject(i);
                line.add(value == null ? "" : value.toString());
            }
            queryContent.append(String.join(",", line));
            queryContent.append("\n");
        }
        return queryContent.toString();
    }
}
