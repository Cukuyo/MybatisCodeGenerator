package mysql.export;

import mysql.info.Column;
import mysql.info.Table;

import java.io.Writer;
import java.io.IOException;
import java.util.List;

/**
 * <p>项目名称: MybatisCodeGenerator
 * <p>文件名称: DaoFunction
 * <p>描述 :
 *
 * @author yao.song
 * @date 2019-07-18 11:16
 **/
public interface DaoFunction {

    /**
     * 根据主键查询
     */
    void selectByPrimaryKey(Table table, List<Column> columnList, Writer writer) throws IOException;

    /**
     * 根据主键删除
     */
    void deleteByPrimaryKey(Table table, List<Column> columnList, Writer writer) throws IOException;

    /**
     * 选择性插入
     */
    void insertSelective(Table table, List<Column> columnList, Writer writer) throws IOException;

    /**
     * 选择性可忽略的插入
     */
    void insertSelectiveIgnore(Table table, List<Column> columnList, Writer writer) throws IOException;

    /**
     * 选择性更新
     */
    void updateSelective(Table table, List<Column> columnList, Writer writer) throws IOException;

    /**
     * 根据主键选择性更新
     */
    void updateSelectiveByPrimaryKey(Table table, List<Column> columnList, Writer writer) throws IOException;

    /**
     * 选择性删除
     */
    void deleteSelective(Table table, List<Column> columnList, Writer writer) throws IOException;

    /**
     * 选择性查询列表
     */
    void listSelective(Table table, List<Column> columnList, Writer writer) throws IOException;

    /**
     * 选择性count
     */
    void countSelective(Table table, List<Column> columnList, Writer writer) throws IOException;

    /**
     * 根据主键批量删除
     */
    void batchDeleteByPrimaryKeyList(Table table, List<Column> columnList, Writer writer) throws IOException;

}
