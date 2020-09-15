package info.devram.dainikhatabook.Values;

import java.io.Serializable;

public class AccountSyncStatus implements Serializable
{
    private static final long serialVersionUID = 20200912L;
    private boolean isSynced = false;

    public AccountSyncStatus() {}

    public boolean isSynced() {
        return isSynced;
    }

    public void updateSyncStatus(boolean status)
    {
        this.isSynced = status;
    }

    @Override
    public String toString() {
        return "AccountSyncStatus{" +
                "isSynced=" + isSynced +
                '}';
    }
}
