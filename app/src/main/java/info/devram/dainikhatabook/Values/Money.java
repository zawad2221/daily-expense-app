package info.devram.dainikhatabook.Values;

import java.io.Serializable;

public class Money implements Serializable
{
    private static final long serialVersionUID = 20200912L;
    private int amount;

    public Money(int amount) {
        this.amount = amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Money{" +
                "amount=" + amount +
                '}';
    }
}
