package mysql.export;

import mysql.info.Column;
import mysql.info.Table;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>项目名称: MybatisGenerator
 * <p>文件名称: ExportPo
 * <p>描述 :
 *
 * @author yao.song
 * @date 2019-07-16 09:19
 **/
public class ExportPo implements Export {

    public void export(String dirPath, Table table, List<Column> columnList) throws IOException {
        //确保父目录以创建
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        FileWriter writer = new FileWriter(dirPath + File.separator + table.getJdbcName() + "Po.java");

        //包名
        writer.write("package 请手动替换包名;\n");
        writer.write("\n");

        //类注释
        writer.write("/**\n");
        writer.write(" * " + table.getName() + "(" + table.getComment() + ")" + "\n");
        writer.write(" *\n");
        writer.write(" * @author " + System.getProperty("user.name") + "\n");
        writer.write(" * @date " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");
        writer.write(" */\n");

        //类名
        writer.write("@Data\n");
        writer.write("public class " + table.getJdbcName() + "Po implements Serializable {\n");
        writer.write("\n");

        //字段
        for (Column column : columnList) {
            writer.write("\t/**\n");
            writer.write("\t * " + column.getName() + "(" + column.getComment() + ")" + "\n");
            writer.write("\t */\n");
            writer.write("\tprivate " + column.getJdbcType() + " " + column.getJdbcName() + ";\n");
        }
        writer.write("\n");

        //结尾
        writer.write("}");

        writer.close();
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

        new ExportPo().export("entity/po", table, columnList);
    }

}
