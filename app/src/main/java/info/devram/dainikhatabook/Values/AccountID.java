package info.devram.dainikhatabook.Values;

import java.io.Serializable;

public class AccountID implements Serializable
{
    private static final long serialVersionUID = 20200912L;
    private String id;

    public AccountID(String id)
    {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "AccountID{" +
                "id='" + id + '\'' +
                '}';
    }
}
