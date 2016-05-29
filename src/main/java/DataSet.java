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

    public Data[] datas = new Data[100];
    public int index;

    public void put (String name, Object value) {
        datas[index++] = new Data(name, value);
    }

    public String[] getName() {
        String[] result = new String[index];
        for (int i = 0; i < index; i++) {
            result[i] = datas[i].getName();
        }
        return result;
    }

    public Object[] getValue() {
        Object[] result = new Object[index];
        for (int i = 0; i < index; i++) {
            result[i] = datas[i].getValue();
        }
        return result;
    }

    @Override
    public String toString() {
        return "DatSet{\n" +
            "names:" + Arrays.toString(getName()) + "\n" +
            "values:" + Arrays.toString(getName()) + "\n" +
            "}";
    }
}