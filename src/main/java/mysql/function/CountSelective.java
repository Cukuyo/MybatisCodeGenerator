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
 * @date 2019-10-08 18:08
 **/
public class CountSelective extends AbstractFunctionExport {

    private Column priColumn;
    private Table table;

    @Override
    public void init(ExportInfo exportInfo) {
        name = "countSelective";
        comment = "选择性count";
        returnType = "java.lang.Long";

        priColumn = exportInfo.getPriColumn();
        paramTypeList = Arrays.asList(exportInfo.getPoClassName());
        paramNameList = Arrays.asList(exportInfo.getPoVariableName());

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
        writer.write(prefix + "<select id=\"countSelective\" parameterType=\"" + exportInfo.getPoPackageClassName() + "\" resultType=\"" + returnType + "\">\n");
        writer.write(prefix + "\tselect count(*) from " + table.getName() + "\n");
        writer.write(prefix + "\t<where>\n");
        writer.write(prefix + "\t\t<include refid=\"Base_List_Selective\"/>\n");
        writer.write(prefix + "\t</where>\n");
        writer.write(prefix + "</select>\n");
    }

    @Override
    public void exportServiceImpl(String prefix, Writer writer, ExportInfo exportInfo) throws IOException {
        exportJavaComment(writer, prefix, comment, null);
        exportImplFunction(writer, prefix, returnType, name, paramTypeList, paramNameList, exportInfo.getDaoVariableName());
    }

}
