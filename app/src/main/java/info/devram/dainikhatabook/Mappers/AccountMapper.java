package info.devram.dainikhatabook.Mappers;

import android.accounts.Account;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import info.devram.dainikhatabook.Controllers.DatabaseHandler;
import info.devram.dainikhatabook.Entities.AccountEntity;
import info.devram.dainikhatabook.Helpers.Config;
import info.devram.dainikhatabook.Interfaces.MapperInterface;
import info.devram.dainikhatabook.Values.AccountCreatedDate;
import info.devram.dainikhatabook.Values.AccountDescription;
import info.devram.dainikhatabook.Values.AccountID;
import info.devram.dainikhatabook.Values.AccountRepoType;
import info.devram.dainikhatabook.Values.AccountSyncStatus;
import info.devram.dainikhatabook.Values.AccountType;
import info.devram.dainikhatabook.Values.Money;

public class AccountMapper implements MapperInterface
{
    private DatabaseHandler db;
    private static AccountMapper mInstance = null;


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
    public void addData(AccountEntity entity, String table) {

    }

    @Override
    public List<AccountEntity> getAll(String table) {
        List<AccountEntity> accountEntities = new ArrayList<>();
        String query;
        if (table == null) {
            query = "SELECT * FROM " + Config.EXPENSE_TABLE_NAME;
            List<AccountEntity> expEntity = this.getAccount(query);
            AccountRepoType expenseRepo = new AccountRepoType(null);
            expEntity.forEach((expense) -> expense.setAccountRepoType(expenseRepo));
            query = "SELECT * FROM " + Config.INCOME_TABLE_NAME;
            AccountRepoType incomeRepo = new AccountRepoType(Config.INCOME_TABLE_NAME);
            List<AccountEntity> incEntity = this.getAccount(query);
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
        return 0;
    }

    private List<AccountEntity> getAccount(String query)
    {
        Cursor cursor = db.getReadableDatabase().rawQuery(query,null);

        List<AccountEntity> accountEntities = new ArrayList<>();

        if(cursor.moveToFirst()) {
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

            }while (cursor.moveToNext());
        }

        cursor.close();

        return accountEntities;
    }
}
