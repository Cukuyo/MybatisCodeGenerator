package mysql.util;

/**
 * <p>项目名称: MybatisCodeGenerator
 * <p>文件名称: StringUtils
 * <p>描述 :
 *
 * @author yao.song
 * @date 2019-07-18 13:55
 **/
public class StringUtils {

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 把数据库小写字母+_的命名方式改为小写驼峰命名，如user_id->userId
     *
     * @param name name
     * @return String
     */
    public static String dbNameToLowerCaseHump(String name) {
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
     * 把数据库小写字母+_的命名方式改为大写驼峰命名，如user_id->UserId
     *
     * @param name name
     * @return String
     */
    public static String dbNameToUpperCaseHump(String name) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return dbNameToLowerCaseHump(name);
    }
}
