package mysql.export;

import mysql.info.Column;
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
public class ExportDaoMapper implements Export, DaoFunction {

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
        //根据主键查询
        selectByPrimaryKey(table, columnList, writer);
        //根据主键删除
        deleteByPrimaryKey(table, columnList, writer);
        //选择性插入
        insertSelective(table, columnList, writer);
        //选择性可忽略的插入
        insertSelectiveIgnore(table, columnList, writer);
        //选择性更新
        updateSelective(table, columnList, writer);
        //根据主键选择性更新
        updateSelectiveByPrimaryKey(table, columnList, writer);
        //选择性删除
        deleteSelective(table, columnList, writer);
        //选择性查询列表
        listSelective(table, columnList, writer);
        //选择性count
        countSelective(table, columnList, writer);
        //根据主键批量删除
        batchDeleteByPrimaryKeyList(table, columnList, writer);

        //结尾
        writer.write("</mapper>");

        writer.close();
    }

    @Override
    public void countSelective(Table table, List<Column> columnList, Writer writer) throws IOException {
        writer.write("\t<!--选择性count-->\n");
        writer.write("\t<select id=\"countSelective\" parameterType=\"" + exportInfo.getPoPackageClassName() + "\" resultType=\"long\">\n");
        writer.write("\t\tselect count(*) from " + table.getName() + "\n");
        writer.write("\t\t<where>\n");
        writer.write("\t\t\t<include refid=\"Base_List_Selective\"/>\n");
        writer.write("\t\t</where>\n");
        writer.write("\t</select>\n");
        writer.write("\n");
    }

    @Override
    public void batchDeleteByPrimaryKeyList(Table table, List<Column> columnList, Writer writer) throws IOException {
        if (priColumn != null) {
            writer.write("\t<!--根据主键批量删除-->\n");
            writer.write("\t<delete id=\"batchDeleteByPrimaryKeyList\">\n");
            writer.write("\t\tdelete from "+table.getName()+" where "+priColumn.getName()+" in\n");
            writer.write("\t\t<foreach item=\"id\" collection=\"list\" open=\"(\" separator=\",\" close=\")\">\n");
            writer.write("\t\t\t#{id}\n");
            writer.write("\t\t</foreach>\n");
            writer.write("\t</delete>\n");
            writer.write("\n");
        }
    }

    @Override
    public void listSelective(Table table, List<Column> columnList, Writer writer) throws IOException {
        writer.write("\t<!--选择性查询列表-->\n");
        writer.write("\t<select id=\"listSelective\" resultMap=\"BaseResultMap\" parameterType=\"" + exportInfo.getPoPackageClassName() + "\">\n");
        writer.write("\t\tselect\n");
        writer.write("\t\t<include refid=\"Base_Column_List\"/>\n");
        writer.write("\t\tfrom  " + table.getName() + "\n");
        writer.write("\t\t<where>\n");
        writer.write("\t\t\t<include refid=\"Base_List_Selective\"/>\n");
        writer.write("\t\t</where>\n");
        writer.write("\t</select>\n");
        writer.write("\n");
    }

    @Override
    public void deleteSelective(Table table, List<Column> columnList, Writer writer) throws IOException {
        writer.write("\t<!--选择性删除-->\n");
        writer.write("\t<delete id=\"deleteSelective\" parameterType=\"" + exportInfo.getPoPackageClassName() + "\">\n");
        writer.write("\t\tdelete from " + table.getName() + "\n");
        writer.write("\t\t<where>\n");
        writer.write("\t\t\t<include refid=\"Base_List_Selective\"/>\n");
        writer.write("\t\t</where>\n");
        writer.write("\t</delete>\n");
        writer.write("\n");
    }

    @Override
    public void updateSelectiveByPrimaryKey(Table table, List<Column> columnList, Writer writer)
            throws IOException {
        if (priColumn != null) {
            writer.write("\t<!--根据主键选择性更新-->\n");
            writer.write("\t<update id=\"updateSelectiveByPrimaryKey\" parameterType=\"" + exportInfo.getPoPackageClassName() + "\">\n");
            writer.write("\t\tupdate " + table.getName() + "\n");
            writer.write("\t\t<include refid=\"Base_Update_Selective\"/>\n");
            writer.write("\t\twhere " + priColumn.getName() + " = #{" + priColumn.getJdbcName() + "}\n");
            writer.write("\t</update>\n");
            writer.write("\n");
        }
    }

    @Override
    public void updateSelective(Table table, List<Column> columnList, Writer writer) throws IOException {
        writer.write("\t<!--选择性更新-->\n");
        writer.write("\t<update id=\"updateSelective\" parameterType=\"" + exportInfo.getPoPackageClassName() + "\">\n");
        writer.write("\t\tupdate " + table.getName() + "\n");
        writer.write("\t\t<include refid=\"Base_Update_Selective\"/>\n");
        writer.write("\t</update>\n");
        writer.write("\n");
    }

    @Override
    public void insertSelectiveIgnore(Table table, List<Column> columnList, Writer writer) throws IOException {
        writer.write("\t<!--选择性可忽略的插入-->\n");
        writer.write("\t<insert id=\"insertSelectiveIgnore\" useGeneratedKeys=\"true\" ");
        if (priColumn != null) {
            writer.write("keyProperty=\"" + priColumn.getName() + "\" ");
        }
        writer.write("parameterType=\"" + exportInfo.getPoPackageClassName() + "\">\n");
        writer.write("\t\tinsert ignore into " + table.getName() + "\n");
        writer.write("\t\t<include refid=\"Base_Insert_Selective\"/>\n");
        writer.write("\t</insert>\n");
        writer.write("\n");
    }

    @Override
    public void insertSelective(Table table, List<Column> columnList, Writer writer) throws IOException {
        writer.write("\t<!--选择性插入-->\n");
        writer.write("\t<insert id=\"insertSelective\" useGeneratedKeys=\"true\" ");
        if (priColumn != null) {
            writer.write("keyProperty=\"" + priColumn.getName() + "\" ");
        }
        writer.write("parameterType=\"" + exportInfo.getPoPackageClassName() + "\">\n");
        writer.write("\t\tinsert into " + table.getName() + "\n");
        writer.write("\t\t<include refid=\"Base_Insert_Selective\"/>\n");
        writer.write("\t</insert>\n");
        writer.write("\n");
    }

    @Override
    public void deleteByPrimaryKey(Table table, List<Column> columnList, Writer writer) throws IOException {
        if (priColumn != null) {
            writer.write("\t<!--根据主键删除-->\n");
            writer.write("\t<delete id=\"deleteByPrimaryKey\">\n");
            writer.write("\t\tdelete from " + table.getName() + "\n");
            writer.write("\t\twhere " + priColumn.getName() + " = #{" + priColumn.getJdbcName() + "}\n");
            writer.write("\t</delete>\n");
            writer.write("\n");
        }
    }

    @Override
    public void selectByPrimaryKey(Table table, List<Column> columnList, Writer writer) throws IOException {
        Column priColumn = null;
        for (Column column : columnList) {
            String key = column.getKey();
            if (key != null && key.equals(Column.KEY_PRIMARY)) {
                priColumn = column;
                break;
            }
        }
        if (priColumn != null) {
            writer.write("\t<!--根据主键查询-->\n");
            writer.write("\t<select id=\"selectByPrimaryKey\" resultMap=\"BaseResultMap\">\n");
            writer.write("\t\tselect\n");
            writer.write("\t\t<include refid=\"Base_Column_List\"/>\n");
            writer.write("\t\tfrom " + table.getName() + "\n");
            writer.write("\t\twhere " + priColumn.getName() + " = #{" + priColumn.getJdbcName() + "}\n");
            writer.write("\t</select>\n");
            writer.write("\n");
        }
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
