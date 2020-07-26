package info.devram.dainikhatabook.Repository;

import java.util.List;

interface DatabaseService<T> {

    public void addData(T obj);

    public List<T> getAll();

    public T getOne(int id);

    public Boolean onUpdate(T obj);

    public Boolean onDelete(T obj);

    public int getCount();
}
