package mysql.function;

import mysql.info.ExportInfo;
import mysql.info.Column;
import mysql.info.Table;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

/**
 * <p>描述 :
 *
 * @author yao.song
 * @date 2019-10-08 17:50
 **/
public class DeleteByPrimaryKey extends AbstractFunctionExport {

    private Column priColumn;
    private Table table;

    @Override
    public void init(ExportInfo exportInfo) {
        name = "deleteByPrimaryKey";
        comment = "根据主键删除";
        returnType = "java.lang.Integer";

        priColumn = exportInfo.getPriColumn();
        if (priColumn != null) {
            paramTypeList = Arrays.asList(priColumn.getJdbcType());
            paramNameList = Arrays.asList(priColumn.getJdbcName());
        }

        table = exportInfo.getTable();
    }

    @Override
    public void exportDao(String prefix, Writer writer, ExportInfo exportInfo) throws IOException {
        if (priColumn != null) {
            String param = generateMybatisParam(paramTypeList.get(0), paramNameList.get(0));
            exportJavaComment(writer, prefix, comment, null);
            exportInterfaceFunction(writer, prefix, returnType, name, Arrays.asList(param));
        }
    }

    @Override
    public void exportDaoMapper(String prefix, Writer writer, ExportInfo exportInfo) throws IOException {
        if (priColumn != null) {
            writer.write(prefix + "<!--" + comment + "-->\n");
            writer.write(prefix + "<delete id=\"deleteByPrimaryKey\">\n");
            writer.write(prefix + "\tdelete from " + table.getName() + "\n");
            writer.write(prefix + "\twhere " + priColumn.getName() + " = #{" + priColumn.getJdbcName() + "}\n");
            writer.write(prefix + "</delete>\n");
        }
    }

    @Override
    public void exportServiceImpl(String prefix, Writer writer, ExportInfo exportInfo) throws IOException {
        if (priColumn != null) {
            exportJavaComment(writer, prefix, comment, null);
            exportImplFunction(writer, prefix, returnType, name, paramTypeList, paramNameList, exportInfo.getDaoVariableName());
        }
    }

}
