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

}
