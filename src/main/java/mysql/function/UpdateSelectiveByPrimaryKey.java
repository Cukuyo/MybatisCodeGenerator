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
 * @date 2019-10-08 18:07
 **/
public class UpdateSelectiveByPrimaryKey extends AbstractFunctionExport {

    private Column priColumn;
    private Table table;

    @Override
    public void init(ExportInfo exportInfo) {
        name = "updateSelectiveByPrimaryKey";
        comment = "根据主键选择性更新";
        returnType = "java.lang.Integer";

        priColumn = exportInfo.getPriColumn();
        if (priColumn != null) {
            paramTypeList = Arrays.asList(exportInfo.getPoClassName());
            paramNameList = Arrays.asList(exportInfo.getPoVariableName());
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
            writer.write("\t<update id=\"updateSelectiveByPrimaryKey\" parameterType=\"" + exportInfo.getPoPackageClassName() + "\">\n");
            writer.write("\t\tupdate " + table.getName() + "\n");
            writer.write("\t\t<include refid=\"Base_Update_Selective\"/>\n");
            writer.write("\t\twhere " + priColumn.getName() + " = #{" + priColumn.getJdbcName() + "}\n");
            writer.write("\t</update>\n");
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
