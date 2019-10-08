package mysql.export;

import mysql.info.Column;
import mysql.info.Table;
import mysql.util.ExportUtils;

import java.io.*;
import java.util.List;

/**
 * <p>项目名称: MybatisGenerator
 * <p>文件名称: ExportServiceImpl
 * <p>描述 :
 *
 * @author yao.song
 * @date 2019-07-16 09:28
 **/
public class ExportServiceImpl implements Export, DaoFunction {

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
                dirPath + File.separator + exportInfo.getServiceImplFileName());
        OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, ExportInfo.CHARSET_NAME);

        //包名
        writer.write("package " + exportInfo.getServiceImplPackageName() + ";\n");
        writer.write("\n");

        //类注释
        ExportUtils.exportDefaultComment(writer, "",
                table.getName() + "(" + table.getComment() + ")");

        //类名
        writer.write("@Service\n");
        writer.write("public class " + exportInfo.getServiceImplClassName() + " implements " + exportInfo.getServiceClassName() + " {\n");
        writer.write("\n");

        //AutoWired
        writer.write("\t@Autowired\n");
        writer.write("\tprivate " + exportInfo.getDaoClassName() + " " + exportInfo.getDaoVariableName() + ";\n");
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
        //批量插入
        batchInsert(table, columnList, writer);

        //结尾
        writer.write("}");

        writer.close();
    }

    @Override
    public void selectByPrimaryKey(Table table, List<Column> columnList, Writer writer) throws IOException {
        if (priColumn != null) {
            String returnType = exportInfo.getPoClassName();
            String funcName = "selectByPrimaryKey";
            String[] paramTypeArr = new String[]{priColumn.getJdbcType()};
            String[] paramNameArr = new String[]{priColumn.getJdbcName()};
            generateFunc(writer, "根据主键查询", returnType, funcName, paramTypeArr, paramNameArr);
        }
    }

    @Override
    public void deleteByPrimaryKey(Table table, List<Column> columnList, Writer writer) throws IOException {
        if (priColumn != null) {
            String returnType = "java.lang.Integer";
            String funcName = "deleteByPrimaryKey";
            String[] paramTypeArr = new String[]{priColumn.getJdbcType()};
            String[] paramNameArr = new String[]{priColumn.getJdbcName()};
            generateFunc(writer, "根据主键删除", returnType, funcName, paramTypeArr, paramNameArr);
        }
    }

    @Override
    public void insertSelective(Table table, List<Column> columnList, Writer writer) throws IOException {
        String returnType = "java.lang.Integer";
        String funcName = "insertSelective";
        String[] paramTypeArr = new String[]{exportInfo.getPoClassName()};
        String[] paramNameArr = new String[]{exportInfo.getPoVariableName()};
        generateFunc(writer, "选择性插入", returnType, funcName, paramTypeArr, paramNameArr);
    }

    @Override
    public void insertSelectiveIgnore(Table table, List<Column> columnList, Writer writer) throws IOException {
        String returnType = "java.lang.Integer";
        String funcName = "insertSelectiveIgnore";
        String[] paramTypeArr = new String[]{exportInfo.getPoClassName()};
        String[] paramNameArr = new String[]{exportInfo.getPoVariableName()};
        generateFunc(writer, "选择性可忽略的插入", returnType, funcName, paramTypeArr, paramNameArr);
    }

    @Override
    public void updateSelective(Table table, List<Column> columnList, Writer writer) throws IOException {
        String returnType = "java.lang.Integer";
        String funcName = "updateSelective";
        String[] paramTypeArr = new String[]{exportInfo.getPoClassName()};
        String[] paramNameArr = new String[]{exportInfo.getPoVariableName()};
        generateFunc(writer, "选择性更新", returnType, funcName, paramTypeArr, paramNameArr);
    }

    @Override
    public void updateSelectiveByPrimaryKey(Table table, List<Column> columnList, Writer writer) throws IOException {
        String returnType = "java.lang.Integer";
        String funcName = "updateSelectiveByPrimaryKey";
        String[] paramTypeArr = new String[]{exportInfo.getPoClassName()};
        String[] paramNameArr = new String[]{exportInfo.getPoVariableName()};
        generateFunc(writer, "根据主键选择性更新", returnType, funcName, paramTypeArr, paramNameArr);
    }

    @Override
    public void deleteSelective(Table table, List<Column> columnList, Writer writer) throws IOException {
        String returnType = "java.lang.Integer";
        String funcName = "deleteSelective";
        String[] paramTypeArr = new String[]{exportInfo.getPoClassName()};
        String[] paramNameArr = new String[]{exportInfo.getPoVariableName()};
        generateFunc(writer, "选择性删除", returnType, funcName, paramTypeArr, paramNameArr);
    }

    @Override
    public void listSelective(Table table, List<Column> columnList, Writer writer) throws IOException {
        String returnType = "List<" + exportInfo.getPoClassName() + ">";
        String funcName = "listSelective";
        String[] paramTypeArr = new String[]{exportInfo.getPoClassName()};
        String[] paramNameArr = new String[]{exportInfo.getPoVariableName()};
        generateFunc(writer, "选择性查询列表", returnType, funcName, paramTypeArr, paramNameArr);
    }

    @Override
    public void countSelective(Table table, List<Column> columnList, Writer writer) throws IOException {
        String returnType = "java.lang.Long";
        String funcName = "countSelective";
        String[] paramTypeArr = new String[]{exportInfo.getPoClassName()};
        String[] paramNameArr = new String[]{exportInfo.getPoVariableName()};
        generateFunc(writer, "选择性count", returnType, funcName, paramTypeArr, paramNameArr);
    }

    @Override
    public void batchDeleteByPrimaryKeyList(Table table, List<Column> columnList, Writer writer) throws IOException {
        if (priColumn != null) {
            String returnType = "java.lang.Integer";
            String funcName = "batchDeleteByPrimaryKeyList";
            String[] paramTypeArr = new String[]{"List<" + priColumn.getJdbcType() + ">"};
            String[] paramNameArr = new String[]{"list"};
            generateFunc(writer, "根据主键批量删除", returnType, funcName, paramTypeArr, paramNameArr);
        }
    }

    @Override
    public void batchInsert(Table table, List<Column> columnList, Writer writer) throws IOException {
        String returnType = "java.lang.Integer";
        String funcName = "batchInsert";
        String[] paramTypeArr = new String[]{"List<" + exportInfo.getPoClassName() + ">"};
        String[] paramNameArr = new String[]{"list"};
        generateFunc(writer, "批量插入", returnType, funcName, paramTypeArr, paramNameArr);
    }

    private void generateFunc(Writer writer, String comment,
                              String returnType, String funcName,
                              String[] paramTypeArr, String[] paramNameArr) throws IOException {
        ExportUtils.exportComment(writer, "\t", comment, null);
        ExportUtils.exportImplFunction(writer, "\t", returnType, funcName, paramTypeArr, paramNameArr, exportInfo.getDaoVariableName());
        writer.write("\n");
    }
}
