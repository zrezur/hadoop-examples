package org.dp.avro;


import org.apache.avro.mapreduce.AvroJob;
import org.apache.avro.mapreduce.AvroKeyValueInputFormat;
import org.apache.avro.mapreduce.AvroKeyValueOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class AvroPairByKeyFilterJob {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        job.setJobName("processing avro rows");

        FileInputFormat.setInputPaths(job, new Path("/inputAvro"));
        FileOutputFormat.setOutputPath(job, new Path(args[0]));

        job.setInputFormatClass(AvroKeyValueInputFormat.class);
        job.setMapperClass(AvroPairByKeyFilterMapper.class);
        job.setOutputFormatClass(AvroKeyValueOutputFormat.class);
        AvroJob.setInputKeySchema(job, MyKey.getClassSchema());
        AvroJob.setInputValueSchema(job, MyValue.getClassSchema());
//        AvroJob.setMapOutputKeySchema(job, MyKey.getClassSchema());
//        AvroJob.setMapOutputValueSchema(job, MyValue.getClassSchema());
        AvroJob.setOutputKeySchema(job, MyKey.getClassSchema());
        AvroJob.setOutputValueSchema(job, MyValue.getClassSchema());

        job.setNumReduceTasks(0);

        boolean result = job.waitForCompletion(true);
        System.out.println("Result is " + result);
    }
}
