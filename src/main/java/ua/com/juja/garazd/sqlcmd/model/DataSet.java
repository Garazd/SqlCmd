package ua.com.juja.garazd.sqlcmd.model;

import java.util.List;
import java.util.Set;

public interface DataSet {
    void put(String name, Object value);

    Set<String> getNames();

    List<Object> getValues();

    Object get(String name);

    void updateFrom(DataSet newValue);
}