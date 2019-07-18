package mysql.info;

import lombok.Data;
import mysql.JdbcTypeSearch;

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
     * 把数据库小写字母+_的命名方式改为驼峰命名
     *
     * @param name name
     * @return jdbcName
     */
    public static String dbNameToJdbcName(String name) {
        char[] nameCharArr = name.toCharArray();
        StringBuilder builder = new StringBuilder(nameCharArr.length);
        boolean isChange = false;
        for (char c : nameCharArr) {
            if (isChange) {
                builder.append(String.valueOf(c).toUpperCase());
                isChange = false;
            } else {
                if (c == '_') {
                    isChange = true;
                } else {
                    builder.append(c);
                }
            }
        }
        return builder.toString();
    }


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


    /**
     * @param name 数据库小写字母+_命名方式的名字
     */
    public void setJdbcName(String name) {
        this.jdbcName = dbNameToJdbcName(name);
    }

    public void setJdbcType(String dateType, String columnType) {
        this.jdbcType = JdbcTypeSearch.search(dateType, columnType);
    }
}
