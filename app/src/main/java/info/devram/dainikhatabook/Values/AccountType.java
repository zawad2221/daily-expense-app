package info.devram.dainikhatabook.Values;

import java.io.Serializable;

public class AccountType implements Serializable
{
    private static final long serialVersionUID = 20200912L;
    private String type;

    public AccountType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "AccountType{" +
                "type='" + type + '\'' +
                '}';
    }
}
