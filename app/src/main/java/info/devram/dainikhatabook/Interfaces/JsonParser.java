package info.devram.dainikhatabook.Interfaces;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public interface JsonParser<T> {

    public void parseFromObject(List<T> object);

    public JSONObject parseFromString(String data);

    public List<T> parseToObject(JSONArray jsonArray);

}
