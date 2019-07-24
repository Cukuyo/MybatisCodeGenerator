package mysql.export;

import mysql.info.Column;
import mysql.info.Table;
import mysql.util.ExportUtils;

import java.io.*;
import java.util.List;

/**
 * <p>项目名称: MybatisGenerator
 * <p>文件名称: ExportDao
 * <p>描述 :
 *
 * @author yao.song
 * @date 2019-07-16 09:26
 **/
public class ExportDao implements Export, DaoFunction {

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
                dirPath + File.separator + exportInfo.getDaoFileName());
        OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, ExportInfo.CHARSET_NAME);

        //包名
        writer.write("package " + exportInfo.getDaoPackageName() + ";\n");
        writer.write("\n");

        //类注释
        ExportUtils.exportDefaultComment(writer, "",
                table.getName() + "(" + table.getComment() + ")");

        //类名
        writer.write("@Mapper\n");
        writer.write("public interface " + table.getJdbcName() + "Dao {\n");
        writer.write("\n");

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
        writer.write("}");

        writer.close();
    }

    @Override
    public void selectByPrimaryKey(Table table, List<Column> columnList, Writer writer) throws IOException {
        if (priColumn != null) {
            String param = "@Param(\"" + priColumn.getJdbcName() + "\") " + priColumn.getJdbcType() + " " + priColumn.getJdbcName();
            String[] params = new String[]{param};
            generateFunc(writer, "根据主键查询",
                    exportInfo.getPoClassName(), "selectByPrimaryKey", params);
        }
    }

    @Override
    public void deleteByPrimaryKey(Table table, List<Column> columnList, Writer writer) throws IOException {
        if (priColumn != null) {
            String param = "@Param(\"" + priColumn.getJdbcName() + "\") " + priColumn.getJdbcType() + " " + priColumn.getJdbcName();
            String[] params = new String[]{param};
            generateFunc(writer, "根据主键删除",
                    "java.lang.Integer", "deleteByPrimaryKey", params);
        }
    }

    @Override
    public void insertSelective(Table table, List<Column> columnList, Writer writer) throws IOException {
        String param = exportInfo.getPoClassName() + " " + exportInfo.getPoVariableName();
        String[] params = new String[]{param};
        generateFunc(writer, "选择性插入",
                "java.lang.Integer", "insertSelective", params);
    }

    @Override
    public void insertSelectiveIgnore(Table table, List<Column> columnList, Writer writer) throws IOException {
        String param = exportInfo.getPoClassName() + " " + exportInfo.getPoVariableName();
        String[] params = new String[]{param};
        generateFunc(writer, "选择性可忽略的插入",
                "java.lang.Integer", "insertSelectiveIgnore", params);
    }

    @Override
    public void updateSelective(Table table, List<Column> columnList, Writer writer) throws IOException {
        String param = exportInfo.getPoClassName() + " " + exportInfo.getPoVariableName();
        String[] params = new String[]{param};
        generateFunc(writer, "选择性更新",
                "java.lang.Integer", "updateSelective", params);
    }

    @Override
    public void updateSelectiveByPrimaryKey(Table table, List<Column> columnList, Writer writer) throws IOException {
        String param = exportInfo.getPoClassName() + " " + exportInfo.getPoVariableName();
        String[] params = new String[]{param};
        generateFunc(writer, "根据主键选择性更新",
                "java.lang.Integer", "updateSelectiveByPrimaryKey", params);
    }

    @Override
    public void deleteSelective(Table table, List<Column> columnList, Writer writer) throws IOException {
        String param = exportInfo.getPoClassName() + " " + exportInfo.getPoVariableName();
        String[] params = new String[]{param};
        generateFunc(writer, "选择性删除",
                "java.lang.Integer", "deleteSelective", params);
    }

    @Override
    public void listSelective(Table table, List<Column> columnList, Writer writer) throws IOException {
        String param = exportInfo.getPoClassName() + " " + exportInfo.getPoVariableName();
        String[] params = new String[]{param};
        generateFunc(writer, "选择性查询列表",
                "List<" + exportInfo.getPoClassName() + ">", "listSelective", params);
    }

    @Override
    public void countSelective(Table table, List<Column> columnList, Writer writer) throws IOException {
        String param = exportInfo.getPoClassName() + " " + exportInfo.getPoVariableName();
        String[] params = new String[]{param};
        generateFunc(writer, "选择性count", "java.lang.Long", "countSelective", params);
    }

    @Override
    public void batchDeleteByPrimaryKeyList(Table table, List<Column> columnList, Writer writer) throws IOException {
        if (priColumn != null) {
            String param = "List<" + priColumn.getJdbcType() + "> list";
            String[] params = new String[]{param};
            generateFunc(writer, "根据主键批量删除", "java.lang.Integer", "batchDeleteByPrimaryKeyList", params);
        }
    }

    private void generateFunc(Writer writer, String comment,
                              String returnType, String funcName,
                              String[] params) throws IOException {
        ExportUtils.exportComment(writer, "\t", comment, null);
        ExportUtils.exportInterfaceFunction(writer, "\t", returnType,
                funcName, params);
        writer.write("\n");
    }
}
