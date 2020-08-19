package info.devram.dainikhatabook.Interfaces;

import org.json.JSONArray;

import java.util.List;

public interface JsonParser<T> {

    public void parseFromObject(List<T> object);

    public JSONArray parseFromString(String data);

    public List<T> parseToObject(Object data);

}
