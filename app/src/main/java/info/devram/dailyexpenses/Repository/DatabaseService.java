package info.devram.dailyexpenses.Repository;

import androidx.lifecycle.MutableLiveData;

import java.lang.reflect.Type;
import java.util.List;
import info.devram.dailyexpenses.Controllers.DatabaseHandler;

interface DatabaseService {

    public void addData(DatabaseHandler db);

    public MutableLiveData<List<Object>> getAll(DatabaseHandler db);

    public Object getOne(DatabaseHandler db);

    public void update(DatabaseHandler db,Object data);

    public void delete(DatabaseHandler db,Object data);
}
