package mysql.function;

import mysql.info.ExportInfo;
import mysql.util.StringUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

/**
 * <p>描述 : 方法导出
 *
 * @author yao.song
 * @date 2019-10-08 16:20
 **/
public abstract class AbstractFunctionExport {

    /**
     * 方法名
     */
    protected String name;
    /**
     * 方法注释
     */
    protected String comment;
    /**
     * 方法返回类型
     */
    protected String returnType;
    /**
     * 参数类型数组
     */
    protected List<String> paramTypeList;
    /**
     * 参数名数组
     */
    protected List<String> paramNameList;

    /**
     * 有的数据需要运行时才能确认
     */
    public abstract void init(ExportInfo exportInfo);

    public abstract void exportDao(String prefix, Writer writer, ExportInfo exportInfo) throws IOException;

    public abstract void exportDaoMapper(String prefix, Writer writer, ExportInfo exportInfo) throws IOException;

    public abstract void exportServiceImpl(String prefix, Writer writer, ExportInfo exportInfo) throws IOException;

    /**
     * 生成mybatis param注解的名称
     *
     * @param type 类型
     * @param name 名称
     * @return (String, str)->@Param("str") String str
     */
    protected String generateMybatisParam(String type, String name) {
        return "@Param(\"" + name + "\") " + type + " " + name;
    }

    /**
     * 写入注释
     *
     * @param writer                writer
     * @param prefix                前缀，一般是""或者\t,\t\t
     * @param comment               注释
     * @param annotationCommentList 注解注释，如@date 2019-07-16 09:19
     * @throws IOException IOException
     */
    protected void exportJavaComment(Writer writer,
                                     String prefix,
                                     String comment,
                                     List<String> annotationCommentList) throws IOException {
        writer.write(prefix + "/**\n");

        //写入注释
        if (!StringUtils.isEmpty(comment)) {
            writer.write(prefix + " * " + comment + "\n");
        }
        //写入注解注释
        if (annotationCommentList != null && !annotationCommentList.isEmpty()) {
            writer.write(prefix + " *\n");
            for (String annotationComment : annotationCommentList) {
                writer.write(prefix + " * " + annotationComment + "\n");
            }
        }

        writer.write(prefix + " **/\n");
    }

    /**
     * 写入接口函数
     *
     * @param writer     writer
     * @param prefix     前缀，一般是""或者\t,\t\t
     * @param returnType 返回类型
     * @param funcName   函数名
     * @param paramList  参数数组
     * @throws IOException IOException
     */
    public static void exportInterfaceFunction(Writer writer,
                                               String prefix,
                                               String returnType,
                                               String funcName,
                                               List<String> paramList) throws IOException {
        writer.write(prefix + returnType + " " + funcName + "(");
        Iterator<String> iterator = paramList.iterator();
        while (iterator.hasNext()) {
            writer.write(iterator.next());
            if (iterator.hasNext()) {
                writer.write(", ");
            }
        }
        writer.write(");\n");
    }

    /**
     * 写入Impl函数
     *
     * @param writer        writer
     * @param prefix        前缀，一般是""或者\t,\t\t
     * @param returnType    返回类型
     * @param funcName      函数名
     * @param paramTypeList 参数类型数组
     * @param paramNameList 参数名数组
     * @param daoName       dao名
     * @throws IOException IOException
     */
    public static void exportImplFunction(Writer writer,
                                          String prefix,
                                          String returnType,
                                          String funcName,
                                          List<String> paramTypeList,
                                          List<String> paramNameList,
                                          String daoName) throws IOException {
        writer.write(prefix + "@Override\n");

        writer.write(prefix + "public " + returnType + " " + funcName + "(");
        int size = paramTypeList.size();
        for (int i = 0; i < size; i++) {
            writer.write(paramTypeList.get(i) + " " + paramNameList.get(i));
            if (i < size - 1) {
                writer.write(", ");
            }
        }
        writer.write("){\n");

        writer.write(prefix + "\treturn " + daoName + "." + funcName + "(");
        for (int i = 0; i < size; i++) {
            writer.write(paramNameList.get(i));
            if (i < size - 1) {
                writer.write(", ");
            }
        }
        writer.write(");\n");
        writer.write(prefix + "}\n");
    }

}
