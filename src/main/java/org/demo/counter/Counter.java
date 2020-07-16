package org.demo.counter;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Counter {
    // 逗号
    private static String TAB_SEPARATOR = ",";

    public static class MyCounterMap extends
            Mapper<LongWritable, Text, Text, Text> {
        // 定义枚举对象
        public static enum LOG_PROCESSOR_COUNTER {
            BAD_RECORDS_LONG, BAD_RECORDS_SHORT
        };

        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] arr_value = value.toString().split(TAB_SEPARATOR);
            if (arr_value.length > 3) {
                /* 自定义计数器 */
                context.getCounter("ErrorCounter", "toolong").increment(1);
                /* 枚举计数器 */
                context.getCounter(LOG_PROCESSOR_COUNTER.BAD_RECORDS_LONG).increment(1);
            } else if (arr_value.length < 3) {
                // 自定义计数器
                context.getCounter("ErrorCounter", "tooshort").increment(1);
                // 枚举计数器
                context.getCounter(LOG_PROCESSOR_COUNTER.BAD_RECORDS_SHORT).increment(1);
            }else {
                // 自定义计数器
                context.getCounter("ErrorCounter", "length is 3").increment(1);
                // 枚举计数器
                context.getCounter(LOG_PROCESSOR_COUNTER.BAD_RECORDS_SHORT).increment(1);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        // 读取配置文件
        Configuration conf = new Configuration();
        // 如果输出目录存在，则删除
        Path mypath = new Path(args[1]);
        FileSystem hdfs = mypath.getFileSystem(conf);
        if (hdfs.isDirectory(mypath)) {
            hdfs.delete(mypath, true);
        }

        // 新建一个任务
        Job job = new Job(conf, "Counter");

        // 主类
        job.setJarByClass(Counter.class);

        // Mapper
        job.setMapperClass(MyCounterMap.class);

        // 输入目录
        FileInputFormat.addInputPath(job, new Path(args[0]));
        // 输出目录
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // 提交任务，并退出
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}