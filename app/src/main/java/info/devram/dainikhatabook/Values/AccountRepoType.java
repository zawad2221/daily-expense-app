package info.devram.dainikhatabook.Values;

import java.io.Serializable;

import info.devram.dainikhatabook.Helpers.Config;

public class AccountRepoType implements Serializable
{
    private static final long serialVersionUID = 20200912L;
    private String repoType;

    public AccountRepoType(String repoType) {
        if (repoType == null) {
            this.repoType = Config.EXPENSE_TABLE_NAME;
        } else {
            this.repoType = repoType;
        }

    }

    public String getRepoType() {
        return repoType;
    }
}
