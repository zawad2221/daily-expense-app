package info.devram.dainikhatabook.Values;

import java.io.Serializable;

public class AccountDescription implements Serializable
{
    private static final long serialVersionUID = 20200912L;
    private String desc;

    public AccountDescription(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "AccountDescription{" +
                "desc='" + desc + '\'' +
                '}';
    }
}
