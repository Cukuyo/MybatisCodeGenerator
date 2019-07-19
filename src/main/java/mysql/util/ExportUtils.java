package mysql.util;

import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>项目名称: MybatisCodeGenerator
 * <p>文件名称: ExportUtils
 * <p>描述 :
 *
 * @author yao.song
 * @date 2019-07-18 13:22
 **/
public class ExportUtils {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 导出注释
     *
     * @param writer        writer
     * @param prefix        前缀，一般是""或者\t,\t\t
     * @param commet        注释
     * @param annotationArr 注解注释，如@date 2019-07-16 09:19
     * @throws IOException IOException
     */
    public static void exportComment(Writer writer,
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

    /**
     * 导出注释
     *
     * @param writer writer
     * @param prefix 前缀，一般是""或者\t,\t\t
     * @param commet 注释
     * @param author 作者
     * @param date   时间
     * @throws IOException IOException
     */
    public static void exportComment(Writer writer,
                                     String prefix,
                                     String commet,
                                     String author,
                                     String date) throws IOException {
        writer.write(prefix + "/**\n");
        writer.write(prefix + " * " + commet + "\n");
        writer.write(prefix + " *\n");
        writer.write(prefix + " * @author " + author + "\n");
        writer.write(prefix + " * @date " + date + "\n");
        writer.write(prefix + " **/\n");
    }

    /**
     * 导出默认模板注释
     *
     * @param writer writer
     * @param prefix 前缀，一般是""或者\t,\t\t
     * @param commet 注释
     * @throws IOException IOException
     */
    public static void exportDefaultComment(Writer writer,
                                            String prefix,
                                            String commet) throws IOException {
        writer.write(prefix + "/**\n");
        writer.write(prefix + " * " + commet + "\n");
        writer.write(prefix + " *\n");
        writer.write(prefix + " * @author " + System.getProperty("user.name") + "\n");
        writer.write(prefix + " * @date " + LocalDateTime.now().format(FORMATTER) + "\n");
        writer.write(prefix + " **/\n");
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
    public static void exportVariable(Writer writer,
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
     * 导出函数
     *
     * @param writer     writer
     * @param prefix     前缀，一般是""或者\t,\t\t
     * @param returnType 返回类型
     * @param funcName   函数名
     * @param params     参数数组
     * @throws IOException IOException
     */
    public static void exportInterfaceFunction(Writer writer,
                                               String prefix,
                                               String returnType,
                                               String funcName,
                                               String[] params) throws IOException {
        writer.write(prefix + returnType + " " + funcName + "(");
        for (int i = 0; i < params.length; i++) {
            writer.write(params[i]);
            if (i < params.length - 1) {
                writer.write(", ");
            }
        }
        writer.write(");\n");
    }

    /**
     * 导出Impl函数
     *
     * @param writer       writer
     * @param prefix       前缀，一般是""或者\t,\t\t
     * @param returnType   返回类型
     * @param funcName     函数名
     * @param paramTypeArr 参数类型数组
     * @param paramNameArr 参数名数组
     * @param daoName      dao名
     * @throws IOException IOException
     */
    public static void exportImplFunction(Writer writer,
                                          String prefix,
                                          String returnType,
                                          String funcName,
                                          String[] paramTypeArr,
                                          String[] paramNameArr,
                                          String daoName) throws IOException {
        writer.write(prefix + "@Override\n");

        writer.write(prefix + "public " + returnType + " " + funcName + "(");
        for (int i = 0; i < paramTypeArr.length; i++) {
            writer.write(paramTypeArr[i] + " " + paramNameArr[i]);
            if (i < paramTypeArr.length - 1) {
                writer.write(", ");
            }
        }
        writer.write("){\n");

        writer.write(prefix + "\treturn " + daoName + "." + funcName + "(");
        for (int i = 0; i < paramTypeArr.length; i++) {
            writer.write(paramNameArr[i]);
            if (i < paramTypeArr.length - 1) {
                writer.write(", ");
            }
        }
        writer.write(");\n");
        writer.write(prefix + "}\n");
    }

}
