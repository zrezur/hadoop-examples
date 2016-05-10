package org.dp.smrim;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import org.assertj.core.data.MapEntry;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class EngineTest {

    public static final String RWA = "RWA";
    public static final String CAD = "CAD";
    public static final String KEY = "KEY";
    private Engine sut = new Engine();

    @Test
    public void shouldMapTransferObjectToAnotherObject() {
        List<Variables> input = Lists.newArrayList(
                new Variables(new Pair<String, Double>(RWA, 1.0),
                        new Pair<String, Double>(CAD, 2.0)),
                new Variables(new Pair<String, Double>(RWA, 3.0),
                        new Pair<String, Double>(CAD, 4.0))
        );

        List<VariablesFlat> output = sut.map(input, new Function<Variables, VariablesFlat>() {
            public VariablesFlat apply(Variables input) {
                double cad = input.getVariable(CAD);
                double rwa = input.getVariable(RWA);
                return new VariablesFlat(cad, rwa);
            }
        });

        assertThat(output).contains(new VariablesFlat(2.0, 1.0), new VariablesFlat(4.0, 3.0));
    }

    @Test
    public void shouldReduceByDefinedKey() {
        List<Variables> input = Lists.newArrayList(
                new Variables(new Pair<String, String>(KEY, "1"),
                        new Pair<String, Double>(RWA, 1.0),
                        new Pair<String, Double>(CAD, 2.0)),
                new Variables(new Pair<String, String>(KEY, "2"),
                        new Pair<String, Double>(RWA, 3.0),
                        new Pair<String, Double>(CAD, 4.0)),
                new Variables(new Pair<String, String>(KEY, "1"),
                        new Pair<String, Double>(RWA, 5.0),
                        new Pair<String, Double>(CAD, 6.0)),
                new Variables(new Pair<String, Double>(RWA, 7.0),
                        new Pair<String, Double>(CAD, 8.0))
        );

        Map<String, VariablesFlat> output = sut.from(input)
                .groupBy(new Function<Variables, String>() {
                    public String apply(Variables input) {
                        String result = input.getVariable(KEY);
                        if(result == null){
                            return "";
                        }
                        return result;
                    }
                }).fold(new Supplier<VariablesFlat>() {
                            public VariablesFlat get() {
                                return new VariablesFlat();
                            }
                        },
                        new Engine.FoldOperation<VariablesFlat, Variables>() {
                            public void apply(VariablesFlat variablesFlat, Variables variables) {
                                double cadInput = variables.getVariable(CAD);
                                double rwaInput = variables.getVariable(RWA);
                                variablesFlat.setCad(cadInput + variablesFlat.getCad());
                                variablesFlat.setRwa(rwaInput + variablesFlat.getRwa());
                            }
                        });


        assertThat(output).containsOnly(MapEntry.entry("1", new VariablesFlat(8.0, 6.0)),
                MapEntry.entry("2", new VariablesFlat(4.0, 3.0)),
                MapEntry.entry("", new VariablesFlat(8.0, 7.0)));

    }

    public static class Variables {
        private Map<String, Object> data = new HashMap<String, Object>();

        public Variables(Pair... pairs) {
            add(pairs);
        }

        public <T> void add(String key, T value) {
            data.put(key, value);
        }

        public final <T> void add(Pair<String, T>... pairs) {
            for (Pair<String, T> pair : pairs) {
                data.put(pair.getKey(), pair.getValue());
            }
        }

        public <T> T getVariable(String name) {
            return (T) data.get(name);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Variables variables = (Variables) o;

            return data != null ? data.equals(variables.data) : variables.data == null;

        }

        @Override
        public int hashCode() {
            return data != null ? data.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "Variables{" +
                    "data=" + data +
                    '}';
        }
    }

    public static class VariablesFlat {
        private double cad;
        private double rwa;

        public VariablesFlat() {
        }

        public VariablesFlat(double cad, double rwa) {
            this.cad = cad;
            this.rwa = rwa;
        }

        public double getCad() {
            return cad;
        }

        public double getRwa() {
            return rwa;
        }

        public void setCad(double cad) {
            this.cad = cad;
        }

        public void setRwa(double rwa) {
            this.rwa = rwa;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            VariablesFlat that = (VariablesFlat) o;

            if (Double.compare(that.cad, cad) != 0) return false;
            return Double.compare(that.rwa, rwa) == 0;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            temp = Double.doubleToLongBits(cad);
            result = (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(rwa);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }

        @Override
        public String toString() {
            return "VariablesFlat{" +
                    "cad=" + cad +
                    ", rwa=" + rwa +
                    '}';
        }
    }

    private static class Pair<K, V> {
        private K key;
        private V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }
}