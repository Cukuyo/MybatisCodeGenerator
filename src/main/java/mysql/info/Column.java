package mysql.info;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>项目名称: MybatisGenerator
 * <p>文件名称: Column
 * <p>描述 : 列
 *
 * @author yao.song
 **/
@Data
public class Column implements Serializable {

    /**
     * 主键KEY值
     */
    public static final String KEY_PRIMARY = "PRI";


    /**
     * 列名
     */
    private String name;
    /**
     * JDBC驼峰命名，如user_id->userId
     */
    private String jdbcName;
    /**
     * 数据类型，如varchar
     */
    private String dataType;
    /**
     * 列类型，如varchar(12)
     */
    private String columnType;
    /**
     * JDBC类型，如java.lang.Long
     */
    private String jdbcType;
    /**
     * 列索引类型，如PRI
     */
    private String key;
    /**
     * 注释
     */
    private String comment;

}
