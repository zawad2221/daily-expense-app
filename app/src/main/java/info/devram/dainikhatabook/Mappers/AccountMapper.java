package info.devram.dainikhatabook.Mappers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.devram.dainikhatabook.Controllers.DatabaseHandler;
import info.devram.dainikhatabook.Entities.AccountEntity;
import info.devram.dainikhatabook.ErrorHandlers.ApplicationError;
import info.devram.dainikhatabook.Helpers.Config;
import info.devram.dainikhatabook.Helpers.Util;
import info.devram.dainikhatabook.Interfaces.MapperInterface;
import info.devram.dainikhatabook.Values.AccountCreatedDate;
import info.devram.dainikhatabook.Values.AccountDescription;
import info.devram.dainikhatabook.Values.AccountID;
import info.devram.dainikhatabook.Values.AccountRepoType;
import info.devram.dainikhatabook.Values.AccountSyncStatus;
import info.devram.dainikhatabook.Values.AccountType;
import info.devram.dainikhatabook.Values.Money;

public class AccountMapper implements MapperInterface {
    private static final String TAG = "AccountMapper";
    private static AccountMapper mInstance = null;
    private DatabaseHandler db;
    private List<AccountEntity> accountEntities;

    private AccountMapper(Context context) {
        this.db = DatabaseHandler.getInstance(context);
    }

    public static AccountMapper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AccountMapper(context);
        }
        return mInstance;
    }

    @Override
    public void addData(AccountEntity entity, String table) throws ApplicationError {
        if (table == null) {
            return;
        }

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Config.ACCOUNT_KEY_ID, entity.accountID.getId());
            contentValues.put(Config.ACCOUNT_KEY_TYPE, entity.accountType.getType());
            contentValues.put(Config.ACCOUNT_KEY_DESC, entity.accountDescription.getDesc());
            contentValues.put(Config.ACCOUNT_KEY_AMOUNT, entity.accountMoney.getAmount());
            contentValues.put(Config.ACCOUNT_KEY_DATE, entity.accountCreatedDate.getCreatedAt());
            if (table.equalsIgnoreCase(Config.EXPENSE_TABLE_NAME)) {
                db.getWritableDatabase().insert(Config.EXPENSE_TABLE_NAME,
                        null, contentValues);
            } else {
                db.getWritableDatabase().insert(Config.INCOME_TABLE_NAME,
                        null, contentValues);
            }

        } catch (SQLException e) {
            throw new ApplicationError("Error Inserting Data");
        }


    }

    @Override
    public List<AccountEntity> getAll(String table) {
        accountEntities = new ArrayList<>();
        String query;
        if (table == null) {

            query = "SELECT * FROM " + Config.EXPENSE_TABLE_NAME;

            List<AccountEntity> expEntity = this.getAccount(query, Util.getExpenseTypes());

            AccountRepoType expenseRepo = new AccountRepoType(null);

            expEntity.forEach((expense) -> expense.setAccountRepoType(expenseRepo));

            query = "SELECT * FROM " + Config.INCOME_TABLE_NAME;

            AccountRepoType incomeRepo = new AccountRepoType(Config.INCOME_TABLE_NAME);

            List<AccountEntity> incEntity = this.getAccount(query, Util.getIncomeTypes());

            incEntity.forEach((income) -> income.setAccountRepoType(incomeRepo));
            accountEntities.addAll(expEntity);
            accountEntities.addAll(incEntity);

            return accountEntities;
        }
        return null;
    }

    @Override
    public AccountEntity getOne(AccountID id, String table) {
        return null;
    }

    @Override
    public Boolean onUpdate(AccountID id, String table) {
        return null;
    }

    @Override
    public Boolean onDelete(AccountID id, String table) {
        return null;
    }

    @Override
    public int getCount() {
        return this.accountEntities.size();
    }

    private List<AccountEntity> getAccount(String query, List<String> types) {

        Cursor cursor = db.getReadableDatabase().rawQuery(query, null);

        List<AccountEntity> accountEntities = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                AccountID id = new AccountID(
                        cursor.getString(cursor.getColumnIndex(Config.ACCOUNT_KEY_ID))
                );

                AccountType type = new AccountType(
                        cursor.getString(cursor.getColumnIndex(Config.ACCOUNT_KEY_TYPE))
                );

                Money accountMoney = new Money(
                        cursor.getInt(cursor.getColumnIndex(Config.ACCOUNT_KEY_AMOUNT))
                );

                AccountCreatedDate createdDate = new AccountCreatedDate(
                        cursor.getLong(cursor.getColumnIndex(Config.ACCOUNT_KEY_DATE))
                );

                AccountDescription description = new AccountDescription(
                        cursor.getString(cursor.getColumnIndex(Config.ACCOUNT_KEY_DESC))
                );

                AccountSyncStatus syncStatus = new AccountSyncStatus();

                int boolValue = cursor.getInt(cursor.getColumnIndex(Config.ACCOUNT_KEY_SYNC));
                if (boolValue != 0) {
                    syncStatus.updateSyncStatus(true);
                }

                AccountEntity accountEntity = new AccountEntity(
                        id,
                        type,
                        createdDate,
                        accountMoney,
                        description,
                        syncStatus
                );

                accountEntities.add(accountEntity);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return accountEntities;
    }

    public HashMap<String, List<AccountEntity>> getTypesAsMaps(List<String> types)
    {
        HashMap<String, List<AccountEntity>> hashMap = new HashMap<>();
        for (String type: types) {

            Cursor cursor = db.getReadableDatabase().query(
                    Config.EXPENSE_TABLE_NAME,new String[]{
                            Config.EXPENSE_KEY_ID, Config.EXPENSE_KEY_TYPE, Config.EXPENSE_KEY_DATE,
                            Config.EXPENSE_KEY_AMOUNT, Config.EXPENSE_KEY_DESC, Config.ACCOUNT_KEY_SYNC
                    }, Config.EXPENSE_KEY_TYPE + "=?", new String[]{type},
                    null,null,null
            );
            Log.d(TAG, "getAccount: " + cursor.getCount());

            if (cursor.getCount() > 0) {
                List<AccountEntity> accountEntities = new ArrayList<>();

                if (cursor.moveToFirst()) {
                    do {
                        AccountID id = new AccountID(
                                cursor.getString(cursor.getColumnIndex(Config.ACCOUNT_KEY_ID))
                        );

                        AccountType accountType = new AccountType(
                                cursor.getString(cursor.getColumnIndex(Config.ACCOUNT_KEY_TYPE))
                        );

                        Money accountMoney = new Money(
                                cursor.getInt(cursor.getColumnIndex(Config.ACCOUNT_KEY_AMOUNT))
                        );

                        AccountCreatedDate createdDate = new AccountCreatedDate(
                                cursor.getLong(cursor.getColumnIndex(Config.ACCOUNT_KEY_DATE))
                        );

                        AccountDescription description = new AccountDescription(
                                cursor.getString(cursor.getColumnIndex(Config.ACCOUNT_KEY_DESC))
                        );

                        AccountSyncStatus syncStatus = new AccountSyncStatus();

                        int boolValue = cursor.getInt(cursor.getColumnIndex(Config.ACCOUNT_KEY_SYNC));
                        if (boolValue != 0) {
                            syncStatus.updateSyncStatus(true);
                        }

                        AccountEntity accountEntity = new AccountEntity(
                                id,
                                accountType,
                                createdDate,
                                accountMoney,
                                description,
                                syncStatus
                        );

                        accountEntities.add(accountEntity);

                    } while (cursor.moveToNext());
                }
                hashMap.put(type, accountEntities);
            }

            cursor.close();
        }
        Log.d(TAG, "getAccount: " + hashMap);
        Log.d(TAG, "getAccount: " + hashMap.size());
        return hashMap;
    }
}
