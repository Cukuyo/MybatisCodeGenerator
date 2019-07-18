package mysql.export;

import mysql.info.Column;
import mysql.info.Table;

import java.io.IOException;
import java.util.List;

/**
 * <p>项目名称: MybatisGenerator
 * <p>文件名称: Export
 * <p>描述 :
 *
 * @author yao.song
 * @date 2019-07-16 10:23
 **/
public interface Export {

    void export(String dirPath, Table table, List<Column> columnList) throws IOException;

}
