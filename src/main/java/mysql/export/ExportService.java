package mysql.export;

import mysql.info.Column;
import mysql.info.Table;
import mysql.util.ExportUtils;

import java.io.*;
import java.util.List;

/**
 * <p>项目名称: MybatisGenerator
 * <p>文件名称: ExportService
 * <p>描述 :
 *
 * @author yao.song
 * @date 2019-07-16 09:26
 **/
public class ExportService implements Export {

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
                dirPath + File.separator + exportInfo.getServiceFileName());
        OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, ExportInfo.CHARSET_NAME);

        //包名
        writer.write("package " + exportInfo.getServicePackageName() + ";\n ");
        writer.write("\n");

        //类注释
        ExportUtils.exportDefaultComment(writer, "",
                table.getName() + "(" + table.getComment() + ")");

        //类名
        writer.write("public interface " + exportInfo.getServiceClassName() + " extends " + exportInfo.getDaoClassName() + " {\n");
        writer.write("\n");

        //结尾
        writer.write("}");

        writer.close();
    }

}
