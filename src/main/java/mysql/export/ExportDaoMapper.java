package mysql.export;

import mysql.function.FunctionExportEnum;
import mysql.info.Column;
import mysql.info.ExportInfo;
import mysql.info.Table;

import java.io.*;
import java.util.List;
import java.util.ListIterator;

/**
 * <p>项目名称: MybatisGenerator
 * <p>文件名称: ExportDaoMapper
 * <p>描述 :
 *
 * @author yao.song
 * @date 2019-07-16 09:26
 **/
public class ExportDaoMapper implements Export {

    private ExportInfo exportInfo;
    private Table table;
    private List<Column> columnList;
    private Column priColumn;

    public void export(String dirPath, ExportInfo exportInfo)
            throws IOException {
        this.exportInfo = exportInfo;
        this.table = exportInfo.getTable();
        this.columnList = exportInfo.getColumnList();
        this.priColumn = exportInfo.getPriColumn();

        //确保父目录已创建
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(
                dirPath + File.separator + exportInfo.getDaoMapperFileName());
        OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, ExportInfo.CHARSET_NAME);

        //固定标识
        fileHeader(table, columnList, writer);
        //namespace
        namespace(table, columnList, writer);
        //BaseResultMap
        baseResultMap(table, columnList, writer);
        //Base_Column_List
        baseColumnList(table, columnList, writer);
        //Base_Insert_Selective
        baseInsertSelective(table, columnList, writer);
        //Base_Update_Selective
        baseUpdateSelective(table, columnList, writer);
        //Base_List_Selective
        baseListSelective(table, columnList, writer);

        //导出各函数
        FunctionExportEnum[] enums = FunctionExportEnum.values();
        for (FunctionExportEnum functionExportEnum : enums) {
            functionExportEnum.getFunctionExport().exportDaoMapper("\t", writer, exportInfo);
            writer.write("\n");
        }

        //结尾
        writer.write("</mapper>");

        writer.close();
    }

    public void baseListSelective(Table table, List<Column> columnList, Writer writer) throws IOException {
        writer.write("\t<!--选择性查询-->\n");
        writer.write("\t<sql id=\"Base_List_Selective\">\n");
        for (Column column : columnList) {
            writer.write("\t\t<if test=\"" + column.getJdbcName() + " != null\">\n");
            writer.write("\t\t\t and " + column.getName() + " = #{" + column.getJdbcName() + "}\n");
            writer.write("\t\t</if>\n");
        }
        writer.write("\t</sql>\n");
        writer.write("\n");
    }

    public void baseUpdateSelective(Table table, List<Column> columnList, Writer writer) throws IOException {
        writer.write("\t<!--选择性更新-->\n");
        writer.write("\t<sql id=\"Base_Update_Selective\">\n");
        writer.write("\t\t<set>\n");
        for (Column column : columnList) {
            writer.write("\t\t\t<if test=\"" + column.getJdbcName() + " != null\">\n");
            writer.write("\t\t\t\t" + column.getName() + " = #{" + column.getJdbcName() + "},\n");
            writer.write("\t\t\t</if>\n");
        }
        writer.write("\t\t</set>\n");
        writer.write("\t</sql>\n");
        writer.write("\n");
    }

    public void baseInsertSelective(Table table, List<Column> columnList, Writer writer) throws IOException {
        writer.write("\t<!--选择性插入-->\n");
        writer.write("\t<sql id=\"Base_Insert_Selective\">\n");
        writer.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n");
        for (Column column : columnList) {
            writer.write("\t\t\t<if test=\"" + column.getJdbcName() + " != null\">\n");
            writer.write("\t\t\t\t" + column.getName() + ",\n");
            writer.write("\t\t\t</if>\n");
        }
        writer.write("\t\t</trim>\n");
        writer.write("\t\t<trim prefix=\"value (\" suffix=\")\" suffixOverrides=\",\">\n");
        for (Column column : columnList) {
            writer.write("\t\t\t<if test=\"" + column.getJdbcName() + " != null\">\n");
            writer.write("\t\t\t\t" + "#{" + column.getJdbcName() + "},\n");
            writer.write("\t\t\t</if>\n");
        }
        writer.write("\t\t</trim>\n");
        writer.write("\t</sql>\n");
        writer.write("\n");
    }

    public void baseColumnList(Table table, List<Column> columnList, Writer writer) throws IOException {
        writer.write("\t<!--基础列名-->\n");
        writer.write("\t<sql id=\"Base_Column_List\">\n");
        writer.write("\t\t");
        ListIterator<Column> iterator = columnList.listIterator();
        do {
            writer.write(iterator.next().getName());
            if (iterator.hasNext()) {
                writer.write(",");
            }
        } while (iterator.hasNext());
        writer.write("\n");
        writer.write("\t</sql>\n");
        writer.write("\n");
    }

    public void baseResultMap(Table table, List<Column> columnList, Writer writer) throws IOException {
        writer.write("\t<!--可根据自己的需求，是否要使用-->\n");
        writer.write("\t<resultMap type=\"" + exportInfo.getPoPackageClassName() + "\" id=\"BaseResultMap\">\n");
        for (Column column : columnList) {
            writer.write("\t\t<result property=\"" + column.getJdbcName() + "\" column=\"" + column.getName() + "\"/>\n");
        }
        writer.write("\t</resultMap>\n");
        writer.write("\n");
    }

    public void namespace(Table table, List<Column> columnList, Writer writer) throws IOException {
        writer.write("<mapper namespace=\"" + exportInfo.getDaoPackageClassName() + "\">\n");
        writer.write("\n");
    }

    public void fileHeader(Table table, List<Column> columnList, Writer writer) throws IOException {
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        writer.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
        writer.write("\n");
    }

}
