package model;

import java.util.Arrays;

public class DataSet {
    static class Data {
        private String name;
        private Object value;

        public Data(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }
    }

    public Data[] data = new Data[100];
    public int freeIndex = 0;

    public void put (String name, Object value) {
        for (int index = 0; index < freeIndex; index++) {
            if (data[index].getName().equals(name)) {
                data[index].value = value;
                return;
            }
        }
        data[freeIndex++] = new Data(name, value);
    }

    public String[] getNames() {
        String[] result = new String[freeIndex];
        for (int i = 0; i < freeIndex; i++) {
            result[i] = data[i].getName();
        }
        return result;
    }

    public Object[] getValues() {
        Object[] result = new Object[freeIndex];
        for (int i = 0; i < freeIndex; i++) {
            result[i] = data[i].getValue();
        }
        return result;
    }

    public Object get(String name) {
        for (int i = 0; i < freeIndex; i++) {
            if (data[i].getName().equals(name)) {
                return data[i].getValue();
            }
        }
        return null;
    }

    public void updateFrom(DataSet newValue) {
        for (int index = 0; index < newValue.freeIndex; index++) {
            Data data = newValue.data[index];
            this.put(data.name, data.value);
        }
    }

    @Override
    public String toString() {
        return "{" +
            "names:" + Arrays.toString(getNames()) + ", " +
            "values:" + Arrays.toString(getValues()) +
            "}";
    }
}