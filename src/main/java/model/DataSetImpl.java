package model;

import java.util.*;

public class DataSetImpl implements DataSet {

    private Map<String, Object> data = new LinkedHashMap<String, Object>();

    @Override
    public void put(String name, Object value) {
        data.put(name, value);
    }

    @Override
    public Set<String> getNames() {
        return data.keySet();
    }

    @Override
    public List<Object> getValues() {
        return new ArrayList<Object>(data.values());
    }

    @Override
    public Object get(String name) {
        return data.get(name);
    }

    @Override
    public void updateFrom(DataSet newValue) {
        Set<String> columns = newValue.getNames();
        for (String name : columns) {
            Object data = newValue.get(name);
            put(name, data);
        }
    }

    @Override
    public String toString() {
        return "{" +
            "names:" + getNames().toString() + ", " +
            "values:" + getValues().toString() +
            "}";
    }
}