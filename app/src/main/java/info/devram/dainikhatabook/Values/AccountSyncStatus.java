package info.devram.dainikhatabook.Values;

public class AccountSyncStatus {

    private boolean isSynced = false;

    public AccountSyncStatus() {}

    public boolean isSynced() {
        return isSynced;
    }

    public void updateSyncStatus(boolean status)
    {
        this.isSynced = status;
    }
}
