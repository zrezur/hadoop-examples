import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.hadoop.io.AvroKeyValue;
import org.apache.avro.mapred.FsInput;
import org.apache.avro.mapreduce.AvroJob;
import org.apache.avro.mapreduce.AvroKeyValueInputFormat;
import org.apache.avro.reflect.ReflectDatumReader;
import org.apache.avro.reflect.ReflectDatumWriter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.dp.avro.AvroPairByKeyFilterMapper;
import org.dp.avro.MyKey;
import org.dp.avro.MyValue;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AvroTests {

    private Configuration conf;

    @Before
    public void setup() {
        conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://hostname:9000/");
        conf.set("mapreduce.framework.name", "yarn");
        conf.set("yarn.resourcemanager.address", "hostname:8032");
        conf.set("yarn.nodemanager.aux - services", "mapreduce_shuffle");
    }

    @Test
    public void createKevValueAvroFile() throws IOException {

        FileSystem fs = FileSystem.get(conf);
        FSDataOutputStream outputStream = fs.create(new Path("/inputAvro/data.avro"));

        Schema schema = AvroKeyValue.getSchema(MyKey.getClassSchema(), MyValue.getClassSchema());

        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(new ReflectDatumWriter<GenericRecord>(schema));
        dataFileWriter.create(schema, outputStream);

        AvroKeyValue<MyKey, MyValue> avroKeyValue = new AvroKeyValue<MyKey, MyValue>(new GenericData.Record(schema));

        avroKeyValue.setKey(new MyKey("1"));
        avroKeyValue.setValue(new MyValue("value1"));
        dataFileWriter.append(avroKeyValue.get());

        avroKeyValue.setKey(new MyKey("2"));
        avroKeyValue.setValue(new MyValue("value2"));
        dataFileWriter.append(avroKeyValue.get());

        avroKeyValue.setKey(new MyKey("3"));
        avroKeyValue.setValue(new MyValue("value3"));
        dataFileWriter.append(avroKeyValue.get());

        dataFileWriter.close();
    }

    @Test
    public void runAvroFilterJob() throws IOException, ClassNotFoundException, InterruptedException {

        Job job = Job.getInstance(conf);
        job.setJobName("processing avro rows");

        FileInputFormat.setInputPaths(job, new Path("/inputAvro"));
        FileOutputFormat.setOutputPath(job, new Path("/outputAvro"));

        job.setInputFormatClass(AvroKeyValueInputFormat.class);
        job.setMapperClass(AvroPairByKeyFilterMapper.class);
        AvroJob.setInputKeySchema(job, MyKey.getClassSchema());
        AvroJob.setInputValueSchema(job, MyValue.getClassSchema());


        AvroJob.setOutputKeySchema(job, MyKey.getClassSchema());
        AvroJob.setOutputValueSchema(job, MyValue.getClassSchema());

        job.setNumReduceTasks(0);

        boolean result = job.waitForCompletion(true);
        System.out.println("Result is " + result);
    }

    @Test
    public void readKeyValueFromAvroFile() throws IOException {
//        FileSystem fs = FileSystem.get(conf);

        Schema schema = AvroKeyValue.getSchema(MyKey.getClassSchema(), MyValue.getClassSchema());
        FsInput fsInput = new FsInput(new Path("/outputAvro4/part-m-00000.avro"), conf);

        DataFileReader<Object> reader = new DataFileReader<Object>(fsInput, new ReflectDatumReader<Object>(schema));

        while(reader.hasNext()){
            Object someObject = reader.next();
            System.out.println(someObject);
        }

        reader.close();
    }

    @Test
    public void serializeAndDeserializeKeys() throws IOException {
        Schema schema = Schema.createRecord("keys", "keys - doc", "org.dp.avro.gen", false);
        schema.setFields(Arrays.asList(
                new Schema.Field("values", Schema.createArray(MyKey.getClassSchema()), "keys in array", null)
        ));
        System.out.println(schema.toString(true));


        File file = new File("test.avro");
        file.delete();

        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(new ReflectDatumWriter<GenericRecord>(schema));
        dataFileWriter.create(schema, new FileOutputStream(file));

        GenericData.Record record = new GenericData.Record(schema);

        MyKey myKey1 = new MyKey("1");
        MyKey myKey2 = new MyKey("2");
        List<MyKey> keys = new ArrayList<MyKey>();
        keys.add(myKey1);
        keys.add(myKey2);
        record.put("values", keys);

        dataFileWriter.append(record);
        dataFileWriter.close();

        DataFileReader<Object> reader = new DataFileReader<Object>(file, new ReflectDatumReader<Object>(schema));

        while(reader.hasNext()){
            Object someObject = reader.next();
            System.out.println(someObject);
        }
    }
}
