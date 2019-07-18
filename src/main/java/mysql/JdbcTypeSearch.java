package mysql;

/**
 * <p>项目名称: MybatisGenerator
 * <p>文件名称: JdbcTypeSearch
 * <p>描述 :
 *
 * @author yao.song
 * @date 2019-07-15 15:30
 **/
public class JdbcTypeSearch {

    public static String search(String dataType, String columnType) {
        dataType = dataType.toLowerCase();

        if (dataType.equals("bit")) {
            return bitSearch(columnType);
        } else if (dataType.equals("tinyint")) {
            return tinyintSearch(columnType);
        } else if (dataType.equals("bool")) {
            return boolSearch(columnType);
        } else if (dataType.equals("boolean")) {
            return booleanSearch(columnType);
        } else if (dataType.equals("smallint")) {
            return smallintSearch(columnType);
        } else if (dataType.equals("mediumint")) {
            return mediumintSearch(columnType);
        } else if (dataType.equals("int")) {
            return intSearch(columnType);
        } else if (dataType.equals("integer")) {
            return integerSearch(columnType);
        } else if (dataType.equals("bigint")) {
            return bigintSearch(columnType);
        } else if (dataType.equals("float")) {
            return floatSearch(columnType);
        } else if (dataType.equals("double")) {
            return doubleSearch(columnType);
        } else if (dataType.equals("decimal")) {
            return decimalSearch(columnType);
        } else if (dataType.equals("date")) {
            return dateSearch(columnType);
        } else if (dataType.equals("datetime")) {
            return datetimeSearch(columnType);
        } else if (dataType.equals("timestamp")) {
            return timestampSearch(columnType);
        } else if (dataType.equals("time")) {
            return timeSearch(columnType);
        } else if (dataType.equals("year")) {
            return yearSearch(columnType);
        } else if (dataType.equals("char")) {
            return charSearch(columnType);
        } else if (dataType.equals("varchar")) {
            return varcharSearch(columnType);
        } else if (dataType.equals("binary")) {
            return binarySearch(columnType);
        } else if (dataType.equals("varbinary")) {
            return varbinarySearch(columnType);
        } else if (dataType.equals("tinyblob")) {
            return tinyblobSearch(columnType);
        } else if (dataType.equals("tinytext")) {
            return tinytextSearch(columnType);
        } else if (dataType.equals("blob")) {
            return blobSearch(columnType);
        } else if (dataType.equals("text")) {
            return textSearch(columnType);
        } else if (dataType.equals("mediumblob")) {
            return mediumblobSearch(columnType);
        } else if (dataType.equals("mediumtext")) {
            return mediumtextSearch(columnType);
        } else if (dataType.equals("longblob")) {
            return longblobSearch(columnType);
        } else if (dataType.equals("longtext")) {
            return longtextSearch(columnType);
        } else if (dataType.equals("enum")) {
            return enumSearch(columnType);
        } else if (dataType.equals("set")) {
            return setSearch(columnType);
        } else {
            throw new RuntimeException("未找到对应的JDBC类型：" + columnType + "，https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-type-conversions.html");
        }
    }

    private static String setSearch(String columnType) {
        return "java.lang.String";
    }

    private static String enumSearch(String columnType) {
        return "java.lang.String";
    }

    private static String longtextSearch(String columnType) {
        return "java.lang.String";
    }

    private static String longblobSearch(String columnType) {
        return "byte[]";
    }

    private static String mediumtextSearch(String columnType) {
        return "java.lang.String";
    }

    private static String mediumblobSearch(String columnType) {
        return "byte[]";
    }

    private static String textSearch(String columnType) {
        return "java.lang.String";
    }

    private static String blobSearch(String columnType) {
        return "byte[]";
    }

    private static String tinytextSearch(String columnType) {
        return "java.lang.String";
    }

    private static String tinyblobSearch(String columnType) {
        return "byte[]";
    }

    private static String varbinarySearch(String columnType) {
        return "byte[]";
    }

    private static String binarySearch(String columnType) {
        return "byte[]";
    }

    private static String varcharSearch(String columnType) {
        return "java.lang.String";
    }

    private static String charSearch(String columnType) {
        return "java.lang.String";
    }

    private static String yearSearch(String columnType) {
        return "java.sql.Date";
    }

    private static String timeSearch(String columnType) {
        return "java.sql.Time";
    }

    private static String timestampSearch(String columnType) {
        return "java.sql.Timestamp";
    }

    private static String datetimeSearch(String columnType) {
        return "java.sql.Timestamp";
    }

    private static String dateSearch(String columnType) {
        return "java.sql.Date";
    }

    private static String decimalSearch(String columnType) {
        return "java.math.BigDecimal";
    }

    private static String doubleSearch(String columnType) {
        return "java.lang.Double";
    }

    private static String floatSearch(String columnType) {
        return "java.lang.Float";
    }

    private static String bigintSearch(String columnType) {
        if (columnType.toLowerCase().contains("unsigned")) {
            return "java.math.BigInteger";
        } else {
            return "java.lang.Long";
        }
    }

    private static String integerSearch(String columnType) {
        if (columnType.toLowerCase().contains("unsigned")) {
            return "java.lang.Long";
        } else {
            return "java.lang.Integer";
        }
    }

    private static String intSearch(String columnType) {
        if (columnType.toLowerCase().contains("unsigned")) {
            return "java.lang.Long";
        } else {
            return "java.lang.Integer";
        }
    }

    private static String mediumintSearch(String columnType) {
        return "java.lang.Integer";
    }

    private static String smallintSearch(String columnType) {
        return "java.lang.Integer";
    }

    private static String booleanSearch(String columnType) {
        return "java.lang.Boolean";
    }

    private static String boolSearch(String columnType) {
        return "java.lang.Boolean";
    }

    private static String tinyintSearch(String columnType) {
        int size = getSize(columnType);
        if (size == 1) {
            return "java.lang.Boolean";
        }
        if (size > 1) {
            return "java.lang.Integer";
        }
        throw new RuntimeException("未找到对应的JDBC类型：" + columnType + "，https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-type-conversions.html");
    }

    private static String bitSearch(String columnType) {
        int size = getSize(columnType);
        if (size == 1) {
            return "java.lang.Boolean";
        }

        if (size > 1) {
            return "byte[]";
        }

        throw new RuntimeException("未找到对应的JDBC类型：" + columnType + "，https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-type-conversions.html");
    }

    private static int getSize(String columnType) {
        int left = columnType.indexOf("(");
        int right = columnType.indexOf(")");
        int size = Integer.valueOf(columnType.substring(left, right)).intValue();
        return size;
    }

}
