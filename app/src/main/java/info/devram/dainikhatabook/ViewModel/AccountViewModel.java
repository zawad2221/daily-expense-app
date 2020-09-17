package info.devram.dainikhatabook.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import info.devram.dainikhatabook.Entities.AccountEntity;
import info.devram.dainikhatabook.ErrorHandlers.ApplicationError;
import info.devram.dainikhatabook.Helpers.Config;
import info.devram.dainikhatabook.Mappers.AccountMapper;
import info.devram.dainikhatabook.Values.AccountID;
import info.devram.dainikhatabook.Values.AccountType;
import info.devram.dainikhatabook.Values.Money;

public class AccountViewModel {
    //private static final String TAG = "MainActivityViewModel";
    private static AccountViewModel mInstance;
    private AccountMapper accountMapper;
    private Application application;
    private List<AccountEntity> accountEntities;

    private AccountViewModel(@NonNull Application application) {
        this.application = application;
    }

    public static AccountViewModel getInstance(Application application) {
        if (mInstance == null) {
            return new AccountViewModel(application);
        }
        return mInstance;
    }

    public void init() {
        accountMapper = AccountMapper.getInstance(this.application.getApplicationContext());

        this.accountEntities = new ArrayList<>();
        this.accountEntities = this.accountMapper.getAll(null);
    }

    public List<AccountEntity> getAccounts(String type) {

        return this.accountEntities;
    }

    public void addAccount(AccountEntity accountEntity) throws ApplicationError {
        boolean result;

        if (accountEntity.accountRepoType.
                getRepoType().equalsIgnoreCase(Config.EXPENSE_TABLE_NAME)) {
            result = this.accountMapper.addData(accountEntity, Config.EXPENSE_TABLE_NAME);
        } else {
            result = this.accountMapper.addData(accountEntity, Config.INCOME_TABLE_NAME);
        }

        if (!result) {
            throw new ApplicationError("Error Adding Data");
        } else {
            this.accountEntities.add(accountEntity);
        }
    }

    public List<AccountEntity> getAccountByTypes(List<String> types, String repoType) {

        HashMap<String, List<AccountEntity>> hashMap = this
                .accountMapper.getTypesAsMaps(repoType, types);

        int sum = 0;

        List<AccountEntity> accountEntities = new ArrayList<>();

        for (Map.Entry<String, List<AccountEntity>> entry : hashMap.entrySet()) {
            AccountType accountType = new AccountType(entry.getKey());
            for (AccountEntity accountEntity : entry.getValue()) {
                sum += accountEntity.accountMoney.getAmount();
            }
            Money accountMoney = new Money(sum);
            AccountEntity accountEntity = new AccountEntity();
            accountEntity.setAccountType(accountType);
            accountEntity.setAccountMoney(accountMoney);
            accountEntities.add(accountEntity);
            sum = 0;
        }

        return accountEntities;
    }

    public void editAccount(AccountEntity account) throws ApplicationError {

        boolean result;
        for (AccountEntity accountEntity : this.accountEntities) {
            if (account.accountID.getId().equalsIgnoreCase(accountEntity.accountID.getId())) {

                if (account.accountRepoType.getRepoType()
                        .equalsIgnoreCase(Config.EXPENSE_TABLE_NAME)) {
                    result = this.accountMapper.onUpdate(account, Config.EXPENSE_TABLE_NAME);
                } else {
                    result = this.accountMapper.onUpdate(account, Config.INCOME_TABLE_NAME);
                }

                if (result) {
                    this.accountEntities.set(this.accountEntities.indexOf(accountEntity), account);
                } else {
                    throw new ApplicationError("Error Inserting Data");
                }

            }
        }
    }

    public List<AccountEntity> getAccountByType(String type) {
        return this.accountEntities.stream()
                .filter(accountEntity -> accountEntity.accountType.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    public void deleteAccount(AccountEntity account) throws ApplicationError {
        boolean result;

        int accountEntitiesPosition = 0;


        for (AccountEntity accountEntity : this.accountEntities) {
            if (account.accountID.getId().equalsIgnoreCase(accountEntity.accountID.getId())) {
                accountEntitiesPosition = this.accountEntities.indexOf(accountEntity);
                break;
            }
        }

        if (account.accountRepoType.getRepoType()
                .equalsIgnoreCase(Config.EXPENSE_TABLE_NAME)) {
            result = this.accountMapper
                    .onDelete(account.accountID, Config.EXPENSE_TABLE_NAME);
        } else {
            result = this.accountMapper
                    .onDelete(account.accountID, Config.INCOME_TABLE_NAME);
        }

        if (result) {
            this.accountEntities.remove(accountEntitiesPosition);
        } else {
            throw new ApplicationError("Error Deleting Data");
        }

    }

    public List<AccountEntity> getAccountForSyncing()
    {
        this.init();

        List<AccountEntity> accounts = new ArrayList<>();

        for (AccountEntity accountEntity : this.accountEntities) {
            if (!accountEntity.syncStatus.isSynced()) {
                accounts.add(accountEntity);
            }
        }

        return accounts;
    }

    public void updateAccountAfterSynced(List<AccountEntity> entities)
    {
        List<AccountID> accountIDS = new ArrayList<>();
        List<String> tables = new ArrayList<>();

        for (AccountEntity accountEntity : entities) {
            accountEntity.syncStatus.updateSyncStatus(true);
            accountIDS.add(accountEntity.accountID);
            if (accountEntity.accountRepoType.getRepoType().equalsIgnoreCase(Config.EXPENSE_TABLE_NAME)) {
                tables.add(Config.EXPENSE_TABLE_NAME);
            } else {
                tables.add(Config.INCOME_TABLE_NAME);
            }
        }

        this.accountMapper.batchUpdateSyncStatus(accountIDS, tables);
    }
}
