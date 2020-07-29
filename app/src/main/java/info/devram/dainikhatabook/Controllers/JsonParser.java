package info.devram.dainikhatabook.Controllers;

import java.util.List;

public interface JsonParser<T> {

    public void parseJson(List<T> object);
}
