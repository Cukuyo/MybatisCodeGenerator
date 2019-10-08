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
 * @date 2019-10-08 17:57
 **/
public class InsertSelective extends AbstractFunctionExport {

    private Column priColumn;
    private Table table;

    @Override
    public void init(ExportInfo exportInfo) {
        name = "insertSelective";
        comment = "选择性插入";
        returnType = "java.lang.Integer";

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
        writer.write(prefix + "<insert id=\"insertSelective\" useGeneratedKeys=\"true\" ");
        if (priColumn != null) {
            writer.write("keyProperty=\"" + priColumn.getName() + "\" ");
        }
        writer.write("parameterType=\"" + exportInfo.getPoPackageClassName() + "\">\n");
        writer.write(prefix + "\tinsert into " + table.getName() + "\n");
        writer.write(prefix + "\t<include refid=\"Base_Insert_Selective\"/>\n");
        writer.write(prefix + "</insert>\n");
    }

    @Override
    public void exportServiceImpl(String prefix, Writer writer, ExportInfo exportInfo) throws IOException {
        exportJavaComment(writer, prefix, comment, null);
        exportImplFunction(writer, prefix, returnType, name, paramTypeList, paramNameList, exportInfo.getDaoVariableName());
    }

}
