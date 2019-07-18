package mysql.export;

import mysql.info.Column;
import mysql.info.Table;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
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
    @Override
    public void export(String dirPath, Table table, List<Column> columnList) throws IOException {
        //确保父目录以创建
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        FileWriter writer = new FileWriter(dirPath + File.separator + table.getJdbcName() + "DaoMapper.xml");

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

        //结尾
        writer.write("</mapper>");

        writer.close();
    }

    private void countSelective(Table table, List<Column> columnList, FileWriter writer) throws IOException {
        writer.write("\t<!--选择性count-->\n");
        writer.write("\t<select id=\"countSelective\" resultType=\"long\">\n");
        writer.write("\t\tselect count(*) from "+table.getName()+"\n");
        writer.write("\t\t<where>\n");
        writer.write("\t\t\t<include refid=\"Base_List_Selective\"/>\n");
        writer.write("\t\t</where>\n");
        writer.write("\t</select>\n");
        writer.write("\n");
    }

    private void listSelective(Table table, List<Column> columnList, FileWriter writer) throws IOException {
        writer.write("\t<!--选择性查询列表-->\n");
        writer.write("\t<select id=\"listSelective\" resultMap=\"BaseResultMap\" parameterType=\"请手动替换包名." + table.getJdbcName() + "Po\">\n");
        writer.write("\t\tselect\n");
        writer.write("\t\t<include refid=\"Base_Column_List\"/>\n");
        writer.write("\t\tfrom  " + table.getName() + "\n");
        writer.write("\t\t<where>\n");
        writer.write("\t\t\t<include refid=\"Base_List_Selective\"/>\n");
        writer.write("\t\t</where>\n");
        writer.write("\t</select>\n");
        writer.write("\n");
    }

    private void deleteSelective(Table table, List<Column> columnList, FileWriter writer) throws IOException {
        writer.write("\t<!--选择性删除-->\n");
        writer.write("\t<delete id=\"deleteSelective\">\n");
        writer.write("\t\tdelete from " + table.getName() + "\n");
        writer.write("\t\t<where>\n");
        writer.write("\t\t\t<include refid=\"Base_List_Selective\"/>\n");
        writer.write("\t\t</where>\n");
        writer.write("\t</delete>\n");
        writer.write("\n");
    }

    private void updateSelectiveByPrimaryKey(Table table, List<Column> columnList, FileWriter writer) throws IOException {
        Column priColumn = null;
        for (Column column : columnList) {
            String key = column.getKey();
            if (key != null && key.equals(Column.KEY_PRIMARY)) {
                priColumn = column;
            }
        }
        if (priColumn == null) {
            return;
        }
        writer.write("\t<!--根据主键选择性更新-->\n");
        writer.write("\t<update id=\"updateSelectiveByPrimaryKey\" parameterType=\"请手动替换包名." + table.getJdbcName() + "Po\">\n");
        writer.write("\t\tupdate " + table.getName() + "\n");
        writer.write("\t\t<include refid=\"Base_Update_Selective\"/>\n");
        writer.write("\t\twhere " + priColumn.getName() + " = #{" + priColumn.getJdbcName() + "}\n");
        writer.write("\t</update>\n");
        writer.write("\n");
    }

    private void updateSelective(Table table, List<Column> columnList, FileWriter writer) throws IOException {
        writer.write("\t<!--选择性更新-->\n");
        writer.write("\t<update id=\"updateSelective\" parameterType=\"请手动替换包名." + table.getJdbcName() + "Po\">\n");
        writer.write("\t\tupdate " + table.getName() + "\n");
        writer.write("\t\t<include refid=\"Base_Update_Selective\"/>\n");
        writer.write("\t</update>\n");
        writer.write("\n");
    }

    private void insertSelectiveIgnore(Table table, List<Column> columnList, FileWriter writer) throws IOException {
        Column priColumn = null;
        for (Column column : columnList) {
            String key = column.getKey();
            if (key != null && key.equals(Column.KEY_PRIMARY)) {
                priColumn = column;
            }
        }
        writer.write("\t<!--选择性可忽略的插入-->\n");
        writer.write("\t<insert id=\"insertSelectiveIgnore\" useGeneratedKeys=\"true\" ");
        if (priColumn != null) {
            writer.write("keyProperty=\"" + priColumn.getName() + "\" ");
        }
        writer.write("parameterType=\"请手动替换包名." + table.getJdbcName() + "Po\">\n");
        writer.write("\t\tinsert ignore into " + table.getName() + "\n");
        writer.write("\t\t<include refid=\"Base_Insert_Selective\"/>\n");
        writer.write("\t</insert>\n");
        writer.write("\n");
    }

    private void insertSelective(Table table, List<Column> columnList, FileWriter writer) throws IOException {
        Column priColumn = null;
        for (Column column : columnList) {
            String key = column.getKey();
            if (key != null && key.equals(Column.KEY_PRIMARY)) {
                priColumn = column;
            }
        }
        writer.write("\t<!--选择性插入-->\n");
        writer.write("\t<insert id=\"insertSelective\" useGeneratedKeys=\"true\" ");
        if (priColumn != null) {
            writer.write("keyProperty=\"" + priColumn.getName() + "\" ");
        }
        writer.write("parameterType=\"请手动替换包名." + table.getJdbcName() + "Po\">\n");
        writer.write("\t\tinsert into " + table.getName() + "\n");
        writer.write("\t\t<include refid=\"Base_Insert_Selective\"/>\n");
        writer.write("\t</insert>\n");
        writer.write("\n");
    }

    private void deleteByPrimaryKey(Table table, List<Column> columnList, FileWriter writer) throws IOException {
        for (Column column : columnList) {
            String key = column.getKey();
            if (key != null && key.equals(Column.KEY_PRIMARY)) {
                writer.write("\t<!--根据主键删除-->\n");
                writer.write("\t<delete id=\"deleteByPrimaryKey\">\n");
                writer.write("\t\tdelete from " + table.getName() + "\n");
                writer.write("\t\twhere " + column.getName() + " = #{" + column.getJdbcName() + "}\n");
                writer.write("\t</delete>\n");
                writer.write("\n");
                break;
            }
        }
    }

    private void selectByPrimaryKey(Table table, List<Column> columnList, FileWriter writer) throws IOException {
        for (Column column : columnList) {
            String key = column.getKey();
            if (key != null && key.equals(Column.KEY_PRIMARY)) {
                writer.write("\t<!--根据主键查询-->\n");
                writer.write("\t<select id=\"selectByPrimaryKey\" resultMap=\"BaseResultMap\">\n");
                writer.write("\t\tselect\n");
                writer.write("\t\t<include refid=\"Base_Column_List\"/>\n");
                writer.write("\t\tfrom " + table.getName() + "\n");
                writer.write("\t\twhere " + column.getName() + " = #{" + column.getJdbcName() + "}\n");
                writer.write("\t</select>\n");
                writer.write("\n");
                break;
            }
        }
    }

    private void baseListSelective(Table table, List<Column> columnList, FileWriter writer) throws IOException {
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

    private void baseUpdateSelective(Table table, List<Column> columnList, FileWriter writer) throws IOException {
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

    private void baseInsertSelective(Table table, List<Column> columnList, FileWriter writer) throws IOException {
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

    private void baseColumnList(Table table, List<Column> columnList, FileWriter writer) throws IOException {
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

    private void baseResultMap(Table table, List<Column> columnList, FileWriter writer) throws IOException {
        writer.write("\t<!--可根据自己的需求，是否要使用-->\n");
        writer.write("\t<resultMap type=\"请手动替换包名." + table.getJdbcName() + "Po\" id=\"BaseResultMap\">\n");
        for (Column column : columnList) {
            writer.write("\t\t<result property=\"" + column.getJdbcName() + "\" column=\"" + column.getName() + "\"/>\n");
        }
        writer.write("\t</resultMap>\n");
        writer.write("\n");
    }

    private void namespace(Table table, List<Column> columnList, FileWriter writer) throws IOException {
        writer.write("<mapper namespace=\"请手动替换包名." + table.getJdbcName() + "Dao\">\n");
        writer.write("\n");
    }

    private void fileHeader(Table table, List<Column> columnList, FileWriter writer) throws IOException {
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        writer.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
        writer.write("\n");
    }

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

        new ExportDaoMapper().export("mapper", table, columnList);
    }
}
