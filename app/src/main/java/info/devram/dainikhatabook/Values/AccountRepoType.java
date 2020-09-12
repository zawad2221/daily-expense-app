package info.devram.dainikhatabook.Values;

import info.devram.dainikhatabook.Helpers.Config;

public class AccountRepoType {

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
