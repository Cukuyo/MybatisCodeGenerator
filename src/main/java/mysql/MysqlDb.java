package mysql;

import mysql.info.Column;
import mysql.info.Table;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>项目名称: MybatisGenerator
 * <p>文件名称: MysqlDb
 * <p>描述 :
 *
 * @author yao.song
 * @date 2019-07-12 18:02
 **/
public class MysqlDb {

    /**
     * 数据库连接对象
     */
    private Connection connection;
    /**
     * SQL执行对象
     */
    private Statement statement;

    private String ip;
    private String port;
    private String user;
    private String passwd;

    public MysqlDb(String ip, String port, String user, String passwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.passwd = passwd;
    }

    public void connect() throws SQLException {
        //2018年10月14日:应用程序不再需要显式地加载JDBC驱动程序使用forname ()。
        //现有的项目目前加载JDBC驱动程序使用forname()将继续没有修改
//        try {
//            //指定JDBC连接时调用的DriverManager，注意：
//            //1、不要导入com.mysql.cj.jdbc.*，否则你会有麻烦的!
//            //2、newInstance()调用是针对一些损坏的Java实现的解决方案
//            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new SQLException(e.getMessage());
//        }

        String url = "jdbc:mysql://" + ip + ":" + port;
        connection = DriverManager.getConnection(url, user, passwd);
        statement = connection.createStatement();
    }

    public void disconnect() throws SQLException {
        statement.close();
        connection.close();
    }

    public List<String> getDatabases() throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT SCHEMA_NAME FROM information_schema.SCHEMATA");
        List<String> dbNameList = new LinkedList<String>();
        while (resultSet.next()) {
            dbNameList.add(resultSet.getString(1));
        }
        resultSet.close();
        return dbNameList;
    }

    public List<Table> getTables(String dbName) throws SQLException {
        ResultSet resultSet = statement.executeQuery(
                "SELECT TABLE_NAME,TABLE_COMMENT " +
                        "FROM information_schema.TABLES " +
                        "WHERE TABLE_SCHEMA=" + '\'' + dbName + '\'');
        List<Table> tableList = new LinkedList<Table>();
        while (resultSet.next()) {
            Table table = new Table();
            String name = resultSet.getString("TABLE_NAME");
            table.setName(name);
            table.setJdbcName(name);
            table.setComment(resultSet.getString("TABLE_COMMENT"));
            tableList.add(table);
        }
        resultSet.close();
        return tableList;
    }

    public List<Column> getColumns(String dbName, String tableName) throws SQLException {
        ResultSet resultSet = statement.executeQuery(
                "SELECT COLUMN_NAME,DATA_TYPE,COLUMN_TYPE,COLUMN_KEY,COLUMN_COMMENT " +
                        "FROM information_schema.COLUMNS " +
                        "WHERE TABLE_SCHEMA=" + '\'' + dbName + '\'' + " AND TABLE_NAME=" + '\'' + tableName + '\'');
        List<Column> columnList = new LinkedList<Column>();
        while (resultSet.next()) {
            Column column = new Column();

            String columnName = resultSet.getString("COLUMN_NAME");
            column.setName(columnName);
            column.setJdbcName(columnName);

            String dateType = resultSet.getString("DATA_TYPE");
            String columnType = resultSet.getString("COLUMN_TYPE");
            column.setDataType(dateType);
            column.setColumnType(columnType);
            column.setJdbcType(dateType, columnType);

            column.setKey(resultSet.getString("COLUMN_KEY"));
            column.setComment(resultSet.getString("COLUMN_COMMENT"));

            columnList.add(column);
        }
        resultSet.close();
        return columnList;
    }
}
