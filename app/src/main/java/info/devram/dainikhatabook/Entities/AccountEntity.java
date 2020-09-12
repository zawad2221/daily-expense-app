package info.devram.dainikhatabook.Entities;

import java.io.Serializable;

import info.devram.dainikhatabook.Values.AccountCreatedDate;
import info.devram.dainikhatabook.Values.AccountDescription;
import info.devram.dainikhatabook.Values.AccountID;
import info.devram.dainikhatabook.Values.AccountRepoType;
import info.devram.dainikhatabook.Values.AccountSyncStatus;
import info.devram.dainikhatabook.Values.AccountType;
import info.devram.dainikhatabook.Values.Money;

public class AccountEntity implements Serializable
{
    private static final long serialVersionUID = 20200911L;
    public AccountID accountID;
    public AccountType accountType;
    public AccountCreatedDate accountCreatedDate;
    public Money accountMoney;
    public AccountDescription accountDescription;
    public AccountSyncStatus syncStatus;
    public AccountRepoType accountRepoType;

    public AccountEntity() {}

    public AccountEntity(
            AccountID accountID,
            AccountType accountType,
            AccountCreatedDate accountCreatedDate,
            Money accountMoney,
            AccountDescription accountDescription,
            AccountSyncStatus accountSyncStatus
    ) {
        this.accountID = accountID;
        this.accountType = accountType;
        this.accountCreatedDate = accountCreatedDate;
        this.accountMoney = accountMoney;
        this.accountDescription = accountDescription;
        this.syncStatus = accountSyncStatus;
    }

    public void setAccountRepoType(AccountRepoType accountRepoType) {
        this.accountRepoType = accountRepoType;
    }
}
