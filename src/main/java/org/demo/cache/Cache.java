package org.demo.cache;
import java.io.IOException;
import java.net.URI;
import java.util.Scanner;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * hadoop的全局共享文件 使用DistributedCached
 */
public class Cache {
    private static Logger logger = LoggerFactory.getLogger(Cache.class);

    private static class FileMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        Path path[] = null;

        /**
         * Map函数前调用
         */
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            logger.info("开始启动setup");
            // System.out.println("运行了.........");
            Configuration conf = context.getConfiguration();
            path = DistributedCache.getLocalCacheFiles(conf);
            System.out.println("获取的路径是：  " + path[0].toString());
            FileSystem fsopen = FileSystem.getLocal(conf);
            FSDataInputStream in = fsopen.open(path[0]);
            Scanner scan = new Scanner(in);
            while (scan.hasNext()) {
                System.out.println(Thread.currentThread().getName() + "扫描的内容:  " + scan.next());
            }
            scan.close();
            // System.out.println("size: "+path.length);
        }


        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            // logger.info("Map里的任务");
            System.out.println("map里输出了");
            context.write(new Text(""), new IntWritable(0));
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            logger.info("清空任务了。。。。。。");
        }
    }


    private static class FileReduce extends Reducer<Object, Object, Object, Object> {
        @Override
        protected void reduce(Object arg0, Iterable<Object> arg1, Context arg2)
                throws IOException, InterruptedException {
            System.out.println("我是reduce里面的东西");
        }
    }


    public static void main(String[] args) throws Exception {
        JobConf conf = new JobConf(Cache.class);
        Job job = new Job(conf, "Cache");
        DistributedCache.addCacheFile(new URI("hdfs://hadoop10:9000/cache.db"), job.getConfiguration());
        job.setJarByClass(Cache.class);
        System.out.println("运行模式：  " + conf.get("mapred.job.tracker"));
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setMapperClass(FileMapper.class);
        job.setReducerClass(FileReduce.class);
        FileInputFormat.setInputPaths(job, new Path(args[0])); // 输入路径
        FileOutputFormat.setOutputPath(job, new Path(args[1]));// 输出路径
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}