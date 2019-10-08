package mysql.export;

import mysql.function.FunctionExportEnum;
import mysql.info.Column;
import mysql.info.ExportInfo;
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
public class ExportServiceImpl implements Export {

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

        //导出各函数
        FunctionExportEnum[] enums = FunctionExportEnum.values();
        for (FunctionExportEnum functionExportEnum : enums) {
            functionExportEnum.getFunctionExport().exportServiceImpl("\t", writer, exportInfo);
            writer.write("\n");
        }

        //结尾
        writer.write("}");

        writer.close();
    }

}
