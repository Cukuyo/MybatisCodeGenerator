package mysql.export;

import mysql.info.Column;
import mysql.info.ExportInfo;
import mysql.info.Table;
import mysql.util.ExportUtils;
import mysql.util.StringUtils;

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
            exportVariable(writer, columnPrefix, columnComment, columnScope,
                    column.getJdbcType(), column.getJdbcName());
        }
        writer.write("\n");

        //结尾
        writer.write("}");

        writer.close();
    }

    /**
     * 导出变量
     *
     * @param writer  writer
     * @param prefix  前缀，一般是""或者\t,\t\t
     * @param comment 变量注释
     * @param scope   public|private|protected
     * @param type    java类型
     * @param name    变量名
     * @throws IOException IOException
     */
    private void exportVariable(Writer writer,
                                String prefix,
                                String comment,
                                String scope,
                                String type,
                                String name) throws IOException {
        if (!StringUtils.isEmpty(comment)) {
            exportComment(writer, prefix, comment, null);
        }
        writer.write(prefix + scope + " " + type + " " + name + ";\n");
    }

    /**
     * 导出注释
     *
     * @param writer        writer
     * @param prefix        前缀，一般是""或者\t,\t\t
     * @param commet        注释
     * @param annotationArr 注解注释，如@date 2019-07-16 09:19
     * @throws IOException IOException
     */
    private void exportComment(Writer writer,
                               String prefix,
                               String commet,
                               String[] annotationArr) throws IOException {
        writer.write(prefix + "/**\n");
        if (!StringUtils.isEmpty(commet)) {
            writer.write(prefix + " * " + commet + "\n");
        }
        if (annotationArr != null && annotationArr.length > 0) {
            writer.write(prefix + " *\n");
            for (String str : annotationArr) {
                writer.write(prefix + " * " + str + "\n");
            }
        }
        writer.write(prefix + " **/\n");
    }

}
