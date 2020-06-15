package info.devram.dailyexpenses.Repository;

import java.util.List;

interface DatabaseService<T> {

    public Boolean addData(T obj);

    public List<T> getAll();

    public T getOne();

    public int onUpdate(T obj);

    public void onDelete(T obj);

    public int getCount();
}
