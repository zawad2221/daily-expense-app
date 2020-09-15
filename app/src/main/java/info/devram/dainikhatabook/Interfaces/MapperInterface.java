package info.devram.dainikhatabook.Interfaces;

import java.util.List;

import info.devram.dainikhatabook.Entities.AccountEntity;
import info.devram.dainikhatabook.ErrorHandlers.ApplicationError;
import info.devram.dainikhatabook.Values.AccountID;

public interface MapperInterface {

    public void addData(AccountEntity entity, String table) throws ApplicationError;

    public List<AccountEntity> getAll(String table);

    public AccountEntity getOne(AccountID id, String table);

    public Boolean onUpdate(AccountID id, String table);

    public Boolean onDelete(AccountID id, String table);

    public int getCount();
}
