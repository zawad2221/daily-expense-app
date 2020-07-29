package info.devram.dainikhatabook.Config;

public class Util {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "expense_manager.db";

    public static final String EXPENSE_TABLE_NAME = "expense";
    public static final String INCOME_TABLE_NAME = "income";

    public static final String EXPENSE_KEY_ID = "_id";
    public static final String EXPENSE_KEY_TYPE = "type";
    public static final String EXPENSE_KEY_DATE = "date";
    public static final String EXPENSE_KEY_DESC = "description";
    public static final String EXPENSE_KEY_AMOUNT = "amount";
    public static final String EXPENSE_KEY_SYNC = "status";
    public static final int SYNC_STATUS_FALSE = 0;
    public static final int SYNC_STATUS_TRUE = 1;

    public static final String INCOME_KEY_ID = "id";
    public static final String INCOME_KEY_TYPE = "type";
    public static final String INCOME_KEY_DATE = "date_added";
    public static final String INCOME_KEY_DESC = "description";
    public static final String INCOME_KEY_AMOUNT = "amount";
}
