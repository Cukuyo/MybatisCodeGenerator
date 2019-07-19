package mysql.export;

import lombok.Data;
import mysql.info.Column;
import mysql.info.Table;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>项目名称: MybatisCodeGenerator
 * <p>文件名称: ExportInfo
 * <p>描述 :
 *
 * @author yao.song
 * @date 2019-07-18 17:11
 **/
@Data
public class ExportInfo implements Serializable {

    public static final String CHARSET_NAME="UTF-8";

    public static class PackageName {
        public static final String PO = "po";
        public static final String DAO = "dao";
        public static final String SERVICE = "service";
        public static final String SERVICE_IMPL = "service_impl";
    }

    private String poPackageName;
    private String poClassName;
    private String poPackageClassName;
    private String poVariableName;
    private String poFileName;

    private String daoPackageName;
    private String daoClassName;
    private String daoPackageClassName;
    private String daoVariableName;
    private String daoFileName;

    private String servicePackageName;
    private String serviceClassName;
    private String servicePackageClassName;
    private String serviceVariableName;
    private String serviceFileName;

    private String serviceImplPackageName;
    private String serviceImplClassName;
    private String serviceImplPackageClassName;
    private String serviceImplVariableName;
    private String serviceImplFileName;

    private String daoMapperFileName;

    //我们关注的列
    /**
     * 主键列
     */
    private Column priColumn;

    private Table table;
    private List<Column> columnList;
    private Map<String, String> packageNameMap;

    /**
     * 因为在写入文件的时候需要其他文件的命名方式，所以统一在这里写。比如xml文件需要po和dao的包名，而dao文件需要po的包名
     *
     * @param table          table
     * @param columnList     columnList
     * @param packageNameMap ExportInfo.PackageName
     */
    public ExportInfo(Table table, List<Column> columnList, Map<String, String> packageNameMap) {
        this.table = table;
        this.columnList = columnList;
        this.packageNameMap = packageNameMap;

        //寻找我们需要的特定的列
        for (Column column : columnList) {
            //寻找主键
            String key = column.getKey();
            if (key != null && key.equals(Column.KEY_PRIMARY)) {
                priColumn = column;
                break;
            }
        }

        //包名
        poPackageName = packageNameMap.get(PackageName.PO);
        if (poPackageName == null) {
            poPackageName = "请手动替换包名";
        }
        daoPackageName = packageNameMap.get(PackageName.DAO);
        if (daoPackageName == null) {
            daoPackageName = "请手动替换包名";
        }
        servicePackageName = packageNameMap.get(PackageName.SERVICE);
        if (servicePackageName == null) {
            servicePackageName = "请手动替换包名";
        }
        serviceImplPackageName = packageNameMap.get(PackageName.SERVICE_IMPL);
        if (serviceImplPackageName == null) {
            serviceImplPackageName = "请手动替换包名";
        }

        //类名
        poClassName = table.getJdbcName() + "Po";
        daoClassName = table.getJdbcName() + "Dao";
        serviceClassName = table.getJdbcName() + "Service";
        serviceImplClassName = table.getJdbcName() + "ServiceImpl";

        //包.类名
        poPackageClassName = poPackageName + "." + poClassName;
        daoPackageClassName = daoPackageName + "." + daoClassName;
        servicePackageClassName = servicePackageName + "." + serviceClassName;
        serviceImplPackageClassName = serviceImplPackageName + "." + serviceClassName;

        //变量名
        poVariableName = poClassName.substring(0, 1).toLowerCase() + poClassName.substring(1);
        daoVariableName = daoClassName.substring(0, 1).toLowerCase() + daoClassName.substring(1);
        serviceVariableName = serviceClassName.substring(0, 1).toLowerCase() + serviceClassName.substring(1);
        serviceImplVariableName = serviceImplClassName.substring(0, 1).toLowerCase() + serviceImplClassName.substring(1);

        //文件名
        poFileName = poClassName + ".java";
        daoFileName = daoClassName + ".java";
        serviceFileName = serviceClassName + ".java";
        serviceImplFileName = serviceImplClassName + ".java";
        daoMapperFileName = daoClassName + "Mapper.xml";
    }
}
