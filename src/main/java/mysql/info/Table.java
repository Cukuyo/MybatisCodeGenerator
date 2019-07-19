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

}
