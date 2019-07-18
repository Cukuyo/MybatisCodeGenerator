package main;

import mysql.MysqlDb;
import mysql.export.ExportPo;
import mysql.info.Column;
import mysql.info.Table;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * <p>项目名称: MybatisGenerator
 * <p>文件名称: Main
 * <p>描述 :
 *
 * @author yao.song
 * @date 2019-07-12 17:48
 **/
public class Main {

    private static String ip;
    private static String port;
    private static String user;
    private static String password;

    public static void main(String[] args) {
        //输入信息
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入Ip:");
        ip = scanner.nextLine();
        System.out.println("请输入端口:");
        port = scanner.nextLine();
        System.out.println("请输入用户名:");
        user = scanner.nextLine();
        System.out.println("请输入密码:");
        password = scanner.nextLine();

        //开始连接
        MysqlDb mysqlDb = new MysqlDb(ip, port, user, password);
        try {
            System.out.println("开始连接");
            mysqlDb.connect();
        } catch (SQLException e) {
            System.err.println("连接失败:" + e.getMessage());
            return;
        }

        //选择数据库
        try {
            List<String> dbNameArr = mysqlDb.getDatabases();
            System.out.println("请选择数据库:");
            for (int i = 0; i < dbNameArr.size(); i++) {
                System.out.println(i + "、" + dbNameArr.get(i));
            }
            int dbNameIndex = scanner.nextInt();
            String dbName = dbNameArr.get(dbNameIndex);

            List<Table> tableList = mysqlDb.getTables(dbName);
            System.out.println("请选择表:");
            for (int i = 0; i < tableList.size(); i++) {
                Table table = tableList.get(i);
                System.out.println(i + "、" + table.getName() + "(" + table.getComment() + ")");
            }
            System.out.println(tableList.size() + "、*");

            int tableListIndex = scanner.nextInt();
            if (tableListIndex == tableList.size()) {
                for (Table table : tableList) {
                    List<Column> columnList = mysqlDb.getColumns(dbName, table.getName());
                    for (Column column:columnList){
                        System.out.println(column);
                    }
                    ExportPo exportPo=new ExportPo();
                    exportPo.export("entity/po",table,columnList);
                }
            } else {
                Table table=tableList.get(tableListIndex);
                List<Column> columnList = mysqlDb.getColumns(dbName, table.getName());
                for (Column column:columnList){
                    System.out.println(column);
                }
                ExportPo exportPo=new ExportPo();
                exportPo.export("entity/po",tableList.get(tableListIndex),columnList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            mysqlDb.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
