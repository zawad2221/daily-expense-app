package info.devram.dainikhatabook.Helpers;

import java.util.ArrayList;
import java.util.List;

public class Config {
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

    public static final String INCOME_KEY_ID = "_id";
    public static final String INCOME_KEY_TYPE = "type";
    public static final String INCOME_KEY_DATE = "date";
    public static final String INCOME_KEY_DESC = "description";
    public static final String INCOME_KEY_AMOUNT = "amount";
    public static final String INCOME_KEY_SYNC = "status";


    public static final String ACCOUNT_KEY_ID = "_id";
    public static final String ACCOUNT_KEY_TYPE = "type";
    public static final String ACCOUNT_KEY_DATE = "date";
    public static final String ACCOUNT_KEY_DESC = "description";
    public static final String ACCOUNT_KEY_AMOUNT = "amount";
    public static final String ACCOUNT_KEY_SYNC = "status";

    public static final String LOGIN_URL = "http://exptracker.devram.info/login";
    public static final String EXPENSE_URL = "http://exptracker.devram.info/expenses";
    public static final String INCOME_URL = "http://exptracker.devram.info/incomes";

}
