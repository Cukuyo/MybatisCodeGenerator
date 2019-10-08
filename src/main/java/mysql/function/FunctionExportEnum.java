package mysql.function;

/**
 * <p>描述 :
 *
 * @author yao.song
 * @date 2019-10-08 18:34
 **/
public enum FunctionExportEnum {

    SELECT_BY_PRIMARYKEY(new SelectByPrimaryKey()),
    DELETE_BY_PRIMARYKEY(new DeleteByPrimaryKey()),
    INSERT_SELECTIVE(new InsertSelective()),
    INSERT_SELECTIVE_IGNORE(new InsertSelectiveIgnore()),
    UPDATE_SELECTIVE(new UpdateSelective()),
    UPDATE_SELECTIVE_BY_PRIMARYKEY(new UpdateSelectiveByPrimaryKey()),
    LIST_SELECTIVE(new ListSelective()),
    DELETE_SELECTIVE(new DeleteSelective()),
    COUNT_SELECTIVE(new CountSelective()),
    BATCH_INSERT(new BatchInsert()),
    BATCH_DELETE_BY_PRIMARYKEY_LIST(new BatchDeleteByPrimaryKeyList());

    private AbstractFunctionExport functionExport;

    public AbstractFunctionExport getFunctionExport() {
        return functionExport;
    }

    FunctionExportEnum(AbstractFunctionExport functionExport) {
        this.functionExport = functionExport;
    }
}
