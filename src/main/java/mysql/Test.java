package mysql;

import mysql.export.*;
import mysql.info.Column;
import mysql.info.Table;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>项目名称: MybatisCodeGenerator
 * <p>文件名称: Test
 * <p>描述 :
 *
 * @author yao.song
 * @date 2019-07-18 17:26
 **/
public class Test {
    public static void main(String[] args) throws IOException {
        Table table = new Table();
        table.setName("student");
        table.setJdbcName("student");
        table.setComment("学生表");

        Column idColumn = new Column();
        idColumn.setName("id");
        idColumn.setComment("主键");
        idColumn.setJdbcName("id");
        idColumn.setJdbcType("java.lang.Integer");
        idColumn.setKey("PRI");

        Column nameColumn = new Column();
        nameColumn.setName("nick_name");
        nameColumn.setComment("名称");
        nameColumn.setJdbcName("nickName");
        nameColumn.setJdbcType("java.lang.String");

        Column ageColumn = new Column();
        ageColumn.setName("ch_age");
        ageColumn.setComment("公历年龄");
        ageColumn.setJdbcName("chAge");
        ageColumn.setJdbcType("java.lang.String");

        Column sexColumn = new Column();
        sexColumn.setName("sex");
        sexColumn.setComment("性别");
        sexColumn.setJdbcName("sex");
        sexColumn.setJdbcType("java.lang.Boolean");

        Column createTimeColumn = new Column();
        createTimeColumn.setName("create_time");
        createTimeColumn.setComment("入学时间");
        createTimeColumn.setJdbcName("createTime");
        createTimeColumn.setJdbcType("java.sql.Timestamp");


        List<Column> columnList = new LinkedList<>();
        columnList.add(idColumn);
        columnList.add(nameColumn);
        columnList.add(ageColumn);
        columnList.add(sexColumn);
        columnList.add(createTimeColumn);

        String poExportDir = "entity/po";
        String daoExportDir = "dao";
        String serviceExportDir = "service";
        String serviceImplExportDir = "service/impl";

        Map<String, String> packageNameMap = new HashMap<>();
        packageNameMap.put(ExportInfo.PackageName.PO, "com.entity.po");
        packageNameMap.put(ExportInfo.PackageName.DAO, "com.dao");
        packageNameMap.put(ExportInfo.PackageName.SERVICE, "com.service");
        packageNameMap.put(ExportInfo.PackageName.SERVICE_IMPL, "com.service.impl");

        ExportInfo exportInfo = new ExportInfo(table, columnList, packageNameMap);

        ExportPo exportPo = new ExportPo();
        ExportDao exportDao = new ExportDao();
        ExportService exportService = new ExportService();
        ExportServiceImpl exportServiceImpl = new ExportServiceImpl();

        exportPo.export(poExportDir, exportInfo);
        exportDao.export(daoExportDir, exportInfo);
        exportService.export(serviceExportDir, exportInfo);
        exportServiceImpl.export(serviceImplExportDir, exportInfo);
    }

}
