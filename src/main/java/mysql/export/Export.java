package mysql.export;

import java.io.IOException;

/**
 * <p>项目名称: MybatisGenerator
 * <p>文件名称: Export
 * <p>描述 :
 *
 * @author yao.song
 * @date 2019-07-16 10:23
 **/
public interface Export {

    /**
     * 导出文件
     *
     * @param dirPath    文件导出目录
     * @param exportInfo 导出信息
     * @throws IOException IOException
     */
    void export(String dirPath, ExportInfo exportInfo) throws IOException;

}
