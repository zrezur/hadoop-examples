package org.dp.avro;

import org.apache.avro.mapred.AvroKey;
import org.apache.avro.mapred.AvroValue;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class AvroPairByKeyFilterMapper<K, V> extends Mapper<AvroKey<K>, AvroValue<V>, AvroKey<K>, AvroValue<V>> {

    private Set<K> keysToFind = new HashSet<K>();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        keysToFind.add((K)new MyKey("1"));
        keysToFind.add((K)new MyKey("3"));
    }

    @Override
    protected void map(AvroKey<K> key, AvroValue<V> value, Context context) throws IOException, InterruptedException {
        if (keysToFind.contains(key.datum())) {
            context.write(key, value);
        }
    }

}
