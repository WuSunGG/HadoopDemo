package org.demo.wordcount;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCountApp {
     /*mapper，继承 org.apache.hadoop.mapreduce.Mapper*/
    public static class MyMapper extends org.apache.hadoop.mapreduce.Mapper<LongWritable, Text, Text, LongWritable> {
        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, LongWritable>.Context context)
                throws IOException, InterruptedException {
//            获取值
            String line = value.toString();

//            拆分值
            String[] splited = line.split(" ");
            for (String word : splited) {
                context.write(new Text(word), new LongWritable(1));
            }
        }
    }

    public static class MyReducer extends org.apache.hadoop.mapreduce.Reducer<Text, LongWritable, Text, LongWritable> {
        @Override
        protected void reduce(Text k2, Iterable<LongWritable> v2s,
                              Reducer<Text, LongWritable, Text, LongWritable>.Context context) throws IOException, InterruptedException {

            long count = 0L;
            for (LongWritable v2 : v2s) {
                count += v2.get();
            }
            LongWritable v3 = new LongWritable(count);
            context.write(k2, v3);
        }
    }

    //客户端代码，写完交给ResourceManager框架去执行
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, WordCountApp.class.getSimpleName());
        //打成jar执行
        job.setJarByClass(WordCountApp.class);

        //数据在哪里？
        FileInputFormat.setInputPaths(job, args[0]);
        //使用哪个mapper处理输入的数据？
        job.setMapperClass(MyMapper.class);
        //map输出的数据类型是什么？
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        //使用哪个reducer处理输入的数据？
        job.setReducerClass(MyReducer.class);
        //reduce输出的数据类型是什么？
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);
        //数据输出到哪里？
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        //交给yarn去执行，直到执行结束才退出本程序
        job.waitForCompletion(true);
    }
}