package mysql.function;

import mysql.info.ExportInfo;
import mysql.info.Column;
import mysql.info.Table;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Iterator;

/**
 * <p>描述 :
 *
 * @author yao.song
 * @date 2019-10-08 18:08
 **/
public class BatchInsert extends AbstractFunctionExport {

    private Column priColumn;
    private Table table;

    @Override
    public void init(ExportInfo exportInfo) {
        name = "batchInsert";
        comment = "批量插入";
        returnType = "java.lang.Integer";

        priColumn = exportInfo.getPriColumn();
        paramTypeList = Arrays.asList("List<" + priColumn.getJdbcType() + ">");
        paramNameList = Arrays.asList("list");

        table = exportInfo.getTable();
    }

    @Override
    public void exportDao(String prefix, Writer writer, ExportInfo exportInfo) throws IOException {
        String param = generateMybatisParam(paramTypeList.get(0), paramNameList.get(0));
        exportJavaComment(writer, prefix, comment, null);
        exportInterfaceFunction(writer, prefix, returnType, name, Arrays.asList(param));
    }

    @Override
    public void exportDaoMapper(String prefix, Writer writer, ExportInfo exportInfo) throws IOException {
        writer.write(prefix + "<!--" + comment + "-->\n");
        writer.write(prefix + "<insert id=\"batchInsert\" parameterType=\"java.util.List\">\n");
        writer.write(prefix + "\tinsert into " + table.getName() + "\n");
        writer.write(prefix + "\t(<include refid=\"Base_Column_List\"/>)\n");
        writer.write(prefix + "\tvalues\n");
        writer.write(prefix + "\t<foreach collection=\"list\" index=\"index\" item=\"item\" separator=\",\">\n");
        writer.write(prefix + "\t\t");
        Iterator<Column> iterator = exportInfo.getColumnList().iterator();
        while (iterator.hasNext()) {
            writer.write("#{item." + iterator.next().getJdbcName() + "}");
            if (iterator.hasNext()) {
                writer.write(", ");
            }
        }
        writer.write("\n");
        writer.write(prefix + "\t</foreach>\n");
        writer.write(prefix + "</insert>\n");
    }

    @Override
    public void exportServiceImpl(String prefix, Writer writer, ExportInfo exportInfo) throws IOException {
        exportJavaComment(writer, prefix, comment, null);
        exportImplFunction(writer, prefix, returnType, name, paramTypeList, paramNameList, exportInfo.getDaoVariableName());
    }

}
