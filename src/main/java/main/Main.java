package main;

import mysql.MysqlDb;
import mysql.export.*;
import mysql.info.Column;
import mysql.info.Table;
import mysql.util.StringUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    //数据库连接属性
    private static String ip;
    private static String port;
    private static String user;
    private static String password;

    //导出文件存放地址("/"开头是绝对地址，没有则相对命令执行目录的路径)
    private static String poExportDir = "entity/po";
    private static String daoExportDir = "dao";
    private static String daoMapperExportDir = "mapper";
    private static String serviceExportDir = "service";
    private static String serviceImplExportDir = "service/impl";

    //导出实例
    private static ExportPo exportPo = new ExportPo();
    private static ExportDao exportDao = new ExportDao();
    private static ExportDaoMapper exportDaoMapper = new ExportDaoMapper();
    private static ExportService exportService = new ExportService();
    private static ExportServiceImpl exportServiceImpl = new ExportServiceImpl();


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //获取数据库连接参数
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

        try {
            //1、请选择数据库
            List<String> dbNameArr = mysqlDb.getDatabases();
            System.out.println("请选择数据库:");
            for (int i = 0; i < dbNameArr.size(); i++) {
                System.out.println(i + "、" + dbNameArr.get(i));
            }
            int dbNameIndex = scanner.nextInt();
            //由于之前是nextInt，所以缓冲区的换行符还没有用，这里手动读取下
            //https://blog.csdn.net/smile_Shujie/article/details/8860030810
            scanner.nextLine();
            String dbName = dbNameArr.get(dbNameIndex);

            //2、请选择表
            List<Table> tableList = mysqlDb.getTables(dbName);
            System.out.println("请选择表（多张表以逗号隔开）:");
            for (int i = 0; i < tableList.size(); i++) {
                Table table = tableList.get(i);
                System.out.println(i + "、" + table.getName() + "(" + table.getComment() + ")");
            }
            System.out.println(tableList.size() + "、以上所有表");
            String tableListIndexStr = scanner.nextLine();

            //获取生成java文件的包名
            Map<String, String> packageNameMap = new HashMap<>();
            String packageNametemp;
            System.out.println("请输入po层包名，直接回车则使用默认包名:");
            packageNametemp = scanner.nextLine();
            if (!StringUtils.isEmpty(packageNametemp)) {
                packageNameMap.put(ExportInfo.PackageName.PO, packageNametemp);
            }
            System.out.println("请输入dao层包名，直接回车则使用默认包名:");
            packageNametemp = scanner.nextLine();
            if (!StringUtils.isEmpty(packageNametemp)) {
                packageNameMap.put(ExportInfo.PackageName.DAO, packageNametemp);
            }
            System.out.println("请输入service层包名，直接回车则使用默认包名:");
            packageNametemp = scanner.nextLine();
            if (!StringUtils.isEmpty(packageNametemp)) {
                packageNameMap.put(ExportInfo.PackageName.SERVICE, packageNametemp);
            }
            System.out.println("请输入serviceImpl层包名，直接回车则使用默认包名:");
            packageNametemp = scanner.nextLine();
            if (!StringUtils.isEmpty(packageNametemp)) {
                packageNameMap.put(ExportInfo.PackageName.SERVICE_IMPL, packageNametemp);
            }

            //导出选择的表
            for (String indexStr : tableListIndexStr.split(",")) {
                Integer index = Integer.valueOf(indexStr);
                if (index == tableList.size()) {
                    for (Table table : tableList) {
                        List<Column> columnList = mysqlDb.getColumns(dbName, table.getName());
                        ExportInfo exportInfo = new ExportInfo(table, columnList, packageNameMap);
                        export(exportInfo);
                    }
                } else {
                    Table table = tableList.get(index);
                    List<Column> columnList = mysqlDb.getColumns(dbName, table.getName());
                    ExportInfo exportInfo = new ExportInfo(table, columnList, packageNameMap);
                    export(exportInfo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }


        //关闭连接
        try {
            mysqlDb.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void export(ExportInfo exportInfo) throws IOException {
        exportPo.export(poExportDir, exportInfo);
        exportDao.export(daoExportDir, exportInfo);
        exportDaoMapper.export(daoMapperExportDir, exportInfo);
        exportService.export(serviceExportDir, exportInfo);
        exportServiceImpl.export(serviceImplExportDir, exportInfo);
    }

}
