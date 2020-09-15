package info.devram.dainikhatabook.Values;

import java.io.Serializable;

public class AccountCreatedDate implements Serializable
{
    private static final long serialVersionUID = 20200912L;
    private long createdAt;

    public AccountCreatedDate(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "AccountCreatedDate{" +
                "createdAt=" + createdAt +
                '}';
    }
}
