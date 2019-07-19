package mysql.export;

import mysql.info.Column;
import mysql.info.Table;
import mysql.util.ExportUtils;

import java.io.*;
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
                dirPath + File.separator + exportInfo.getPoFileName());
        OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, ExportInfo.CHARSET_NAME);

        //包名
        writer.write("package " + exportInfo.getPoPackageName() + ";\n");
        writer.write("\n");

        //类注释
        ExportUtils.exportDefaultComment(writer, "",
                table.getName() + "(" + table.getComment() + ")");

        //类名
        writer.write("@Data\n");
        writer.write("public class " + exportInfo.getPoClassName() + " implements Serializable {\n");
        writer.write("\n");

        //字段
        String columnPrefix = "\t";
        String columnScope = "private";
        for (Column column : columnList) {
            String columnComment = column.getName() + "(" + column.getComment() + ")";
            ExportUtils.exportVariable(writer, columnPrefix, columnComment, columnScope,
                    column.getJdbcType(), column.getJdbcName());
        }
        writer.write("\n");

        //结尾
        writer.write("}");

        writer.close();
    }

}
