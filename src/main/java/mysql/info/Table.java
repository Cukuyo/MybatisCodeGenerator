package mysql.info;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>项目名称: MybatisGenerator
 * <p>文件名称: Table
 * <p>描述 : 表
 *
 * @author yao.song
 * @date 2019-07-13 14:53
 **/
@Data
public class Table implements Serializable {

    /**
     * 把数据库小写字母+_的命名方式改为首字母大写的驼峰命名
     *
     * @param name name
     * @return jdbcName
     */
    public static String dbNameToJdbcName(String name) {
        //先驼峰
        name = Column.dbNameToJdbcName(name);
        //再把第一个大写
        String firstChar = String.valueOf(name.charAt(0)).toUpperCase();

        return firstChar + name.substring(1);
    }

    /**
     * 表名
     */
    private String name;
    /**
     * JDBC的表名，如user_class->UserClass
     */
    private String jdbcName;
    /**
     * 注释
     */
    private String comment;

    /**
     * JDBC的表名
     *
     * @param name 表名
     */
    public void setJdbcName(String name) {
        this.jdbcName = dbNameToJdbcName(name);
    }
}
