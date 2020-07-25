# MapReduce-SecondarySort
MapReduce二次排序例子


## 二次排序
(先按第一列排序，再按第二列排序)
####实现原理：
    map分发数据到reduce时，就会按key排序，即调用key的compareTo方法，
	
    只要把第一个数相同的行发到同一个reduce且重写每行间数的大小比较即可
  
* 定义一个新key(IntPair)，由要排序的两个数组成
* map输出&lt;IntPair,key&gt;
* 重写Partitioner,第一个数一样的，发送到同一个reduce
* 重写WritableComparator，按第一个数分组，（IntPair中也要重写compareTo方法）


 ```
输入：


5|67
4|5
4|3
 

输出：

 
4|3
4|5
5|67
 
 
[root@hadoop10 ~]# cat secondarysort.txt 
5|67
4|5
4|3

hdfs dfs -put secondarysort.txt /
hdfs dfs -ls secondarysort.txt  
hadoop jar  MapReduce-SecondarySort-1.0.0-SNAPSHOT.jar org.demo.secondarysort.SortDriver -D input=/secondarysort.txt -D output=/output22 -D mapreduce.job.reduces=4
hdfs dfs -cat /output22/*
[root@hadoop10 ~]# hadoop jar  MapReduce-SecondarySort-1.0.0-SNAPSHOT.jar org.demo.secondarysort.SortDriver -D input=/secondarysort.txt -D output=/output22 -D mapreduce.job.reduces=4
input:/secondarysort.txt
output:/output22
20/07/16 16:51:40 INFO client.RMProxy: Connecting to ResourceManager at hadoop11/192.168.5.11:8032
20/07/16 16:51:40 INFO input.FileInputFormat: Total input paths to process : 1
20/07/16 16:51:41 INFO mapreduce.JobSubmitter: number of splits:1
20/07/16 16:51:41 INFO mapreduce.JobSubmitter: Submitting tokens for job: job_1593589320451_0005
20/07/16 16:51:41 INFO impl.YarnClientImpl: Submitted application application_1593589320451_0005
20/07/16 16:51:41 INFO mapreduce.Job: The url to track the job: http://hadoop11:8088/proxy/application_1593589320451_0005/
20/07/16 16:51:41 INFO mapreduce.Job: Running job: job_1593589320451_0005
20/07/16 16:51:46 INFO mapreduce.Job: Job job_1593589320451_0005 running in uber mode : false
20/07/16 16:51:46 INFO mapreduce.Job:  map 0% reduce 0%
20/07/16 16:51:50 INFO mapreduce.Job:  map 100% reduce 0%
20/07/16 16:51:57 INFO mapreduce.Job:  map 100% reduce 25%
20/07/16 16:51:58 INFO mapreduce.Job:  map 100% reduce 50%
20/07/16 16:51:59 INFO mapreduce.Job:  map 100% reduce 75%
20/07/16 16:52:00 INFO mapreduce.Job:  map 100% reduce 100%
20/07/16 16:52:00 INFO mapreduce.Job: Job job_1593589320451_0005 completed successfully
20/07/16 16:52:00 INFO mapreduce.Job: Counters: 50
	File System Counters
		FILE: Number of bytes read=66
		FILE: Number of bytes written=591088
		FILE: Number of read operations=0
		FILE: Number of large read operations=0
		FILE: Number of write operations=0
		HDFS: Number of bytes read=117
		HDFS: Number of bytes written=13
		HDFS: Number of read operations=15
		HDFS: Number of large read operations=0
		HDFS: Number of write operations=8
	Job Counters 
		Killed reduce tasks=1
		Launched map tasks=1
		Launched reduce tasks=4
		Data-local map tasks=1
		Total time spent by all maps in occupied slots (ms)=1886
		Total time spent by all reduces in occupied slots (ms)=14645
		Total time spent by all map tasks (ms)=1886
		Total time spent by all reduce tasks (ms)=14645
		Total vcore-milliseconds taken by all map tasks=1886
		Total vcore-milliseconds taken by all reduce tasks=14645
		Total megabyte-milliseconds taken by all map tasks=1931264
		Total megabyte-milliseconds taken by all reduce tasks=14996480
	Map-Reduce Framework
		Map input records=4
		Map output records=3
		Map output bytes=36
		Map output materialized bytes=66
		Input split bytes=103
		Combine input records=0
		Combine output records=0
		Reduce input groups=2
		Reduce shuffle bytes=66
		Reduce input records=3
		Reduce output records=3
		Spilled Records=6
		Shuffled Maps =4
		Failed Shuffles=0
		Merged Map outputs=4
		GC time elapsed (ms)=349
		CPU time spent (ms)=3740
		Physical memory (bytes) snapshot=948518912
		Virtual memory (bytes) snapshot=10568540160
		Total committed heap usage (bytes)=589824000
	Shuffle Errors
		BAD_ID=0
		CONNECTION=0
		IO_ERROR=0
		WRONG_LENGTH=0
		WRONG_MAP=0
		WRONG_REDUCE=0
	File Input Format Counters 
		Bytes Read=14
	File Output Format Counters 
		Bytes Written=13
[root@hadoop10 ~]# ^C
[root@hadoop10 ~]# hdfs dfs -cat /output22/*
4	3
4	5
5	67

```
## counter
```  
hdfs dfs -rm /counter.txt
hdfs dfs -ls /counter.txt
cat >counter.txt <<EOF
demo1,281,female,23,73,21
demo2,283,male
demo3,21s
demo4
demo5,23e,male,china
demo9,22,male,usa
EOF
hdfs dfs -put counter.txt /
hdfs dfs -ls /counter.txt  
hadoop jar  MapReduce-SecondarySort-1.0.0-SNAPSHOT.jar org.demo.counter.Counter /counter.txt /output24
hdfs dfs -cat  /output24/*  




运行结果
[root@hadoop10 ~]# hdfs dfs -ls /counter.txt
ls: `/counter.txt': No such file or directory
[root@hadoop10 ~]# cat >counter.txt <<EOF
> demo1,281,female,23,73,21
> demo2,283,male
> demo3,21s
> demo4
> demo5,23e,male,china
> demo9,22,male,usa
> EOF
[root@hadoop10 ~]# hdfs dfs -put counter.txt /
[root@hadoop10 ~]# hdfs dfs -ls /counter.txt  
-rw-r--r--   3 root supergroup         96 2020-07-16 18:35 /counter.txt
[root@hadoop10 ~]# hadoop jar  MapReduce-SecondarySort-1.0.0-SNAPSHOT.jar org.demo.counter.Counter /counter.txt /output24  
Exception in thread "main" java.lang.IllegalArgumentException: java.net.URISyntaxException: Relative path in absolute URI: D:%5C360Downloads%5CMapReduce-SecondarySort-master%5Ccounter.txt
	at org.apache.hadoop.fs.Path.initialize(Path.java:205)
	at org.apache.hadoop.fs.Path.<init>(Path.java:171)
	at org.demo.counter.Counter.main(Counter.java:71)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.apache.hadoop.util.RunJar.run(RunJar.java:221)
	at org.apache.hadoop.util.RunJar.main(RunJar.java:136)
Caused by: java.net.URISyntaxException: Relative path in absolute URI: D:%5C360Downloads%5CMapReduce-SecondarySort-master%5Ccounter.txt
	at java.net.URI.checkPath(URI.java:1823)
	at java.net.URI.<init>(URI.java:745)
	at org.apache.hadoop.fs.Path.initialize(Path.java:202)
	... 8 more
[root@hadoop10 ~]# rm -f MapReduce-SecondarySort-1.0.0-SNAPSHOT.jar 
[root@hadoop10 ~]# rz -E
rz waiting to receive.
[root@hadoop10 ~]# hdfs dfs -rm /counter.txt
ondarySort-1.0.0-SNAPSHOT.jar org.demo.counter.Counter /counter.txt /output24  
20/07/16 18:36:44 INFO fs.TrashPolicyDefault: Namenode trash configuration: Deletion interval = 0 minutes, Emptier interval = 0 minutes.
Deleted /counter.txt
[root@hadoop10 ~]# hdfs dfs -ls /counter.txt
ls: `/counter.txt': No such file or directory
[root@hadoop10 ~]# cat >counter.txt <<EOF
> demo1,281,female,23,73,21
> demo2,283,male
> demo3,21s
> demo4
> demo5,23e,male,china
> demo9,22,male,usa
> EOF
[root@hadoop10 ~]# hdfs dfs -put counter.txt /
[root@hadoop10 ~]# hdfs dfs -ls /counter.txt  
-rw-r--r--   3 root supergroup         96 2020-07-16 18:36 /counter.txt
[root@hadoop10 ~]# hadoop jar  MapReduce-SecondarySort-1.0.0-SNAPSHOT.jar org.demo.counter.Counter /counter.txt /output24  
20/07/16 18:36:52 INFO client.RMProxy: Connecting to ResourceManager at hadoop11/192.168.5.11:8032
20/07/16 18:36:53 WARN mapreduce.JobResourceUploader: Hadoop command-line option parsing not performed. Implement the Tool interface and execute your application with ToolRunner to remedy this.
20/07/16 18:36:53 INFO input.FileInputFormat: Total input paths to process : 1
20/07/16 18:36:53 INFO mapreduce.JobSubmitter: number of splits:1
20/07/16 18:36:53 INFO mapreduce.JobSubmitter: Submitting tokens for job: job_1594894717477_0004
20/07/16 18:36:53 INFO impl.YarnClientImpl: Submitted application application_1594894717477_0004
20/07/16 18:36:53 INFO mapreduce.Job: The url to track the job: http://hadoop11:8088/proxy/application_1594894717477_0004/
20/07/16 18:36:53 INFO mapreduce.Job: Running job: job_1594894717477_0004
20/07/16 18:36:58 INFO mapreduce.Job: Job job_1594894717477_0004 running in uber mode : false
20/07/16 18:36:58 INFO mapreduce.Job:  map 0% reduce 0%
20/07/16 18:37:02 INFO mapreduce.Job:  map 100% reduce 0%
20/07/16 18:37:08 INFO mapreduce.Job:  map 100% reduce 100%
20/07/16 18:37:08 INFO mapreduce.Job: Job job_1594894717477_0004 completed successfully
20/07/16 18:37:08 INFO mapreduce.Job: Counters: 54
	File System Counters
		FILE: Number of bytes read=6
		FILE: Number of bytes written=233139
		FILE: Number of read operations=0
		FILE: Number of large read operations=0
		FILE: Number of write operations=0
		HDFS: Number of bytes read=193
		HDFS: Number of bytes written=0
		HDFS: Number of read operations=6
		HDFS: Number of large read operations=0
		HDFS: Number of write operations=2
	Job Counters 
		Launched map tasks=1
		Launched reduce tasks=1
		Data-local map tasks=1
		Total time spent by all maps in occupied slots (ms)=1996
		Total time spent by all reduces in occupied slots (ms)=1993
		Total time spent by all map tasks (ms)=1996
		Total time spent by all reduce tasks (ms)=1993
		Total vcore-milliseconds taken by all map tasks=1996
		Total vcore-milliseconds taken by all reduce tasks=1993
		Total megabyte-milliseconds taken by all map tasks=2043904
		Total megabyte-milliseconds taken by all reduce tasks=2040832
	Map-Reduce Framework
		Map input records=6
		Map output records=0
		Map output bytes=0
		Map output materialized bytes=6
		Input split bytes=97
		Combine input records=0
		Combine output records=0
		Reduce input groups=0
		Reduce shuffle bytes=6
		Reduce input records=0
		Reduce output records=0
		Spilled Records=0
		Shuffled Maps =1
		Failed Shuffles=0
		Merged Map outputs=1
		GC time elapsed (ms)=74
		CPU time spent (ms)=890
		Physical memory (bytes) snapshot=440643584
		Virtual memory (bytes) snapshot=4222402560
		Total committed heap usage (bytes)=310378496
	ErrorCounter
		length is 3=1
		toolong=3
		tooshort=2
	Shuffle Errors
		BAD_ID=0
		CONNECTION=0
		IO_ERROR=0
		WRONG_LENGTH=0
		WRONG_MAP=0
		WRONG_REDUCE=0
	File Input Format Counters 
		Bytes Read=96
	File Output Format Counters 
		Bytes Written=0
	org.demo.counter.Counter$MyCounterMap$LOG_PROCESSOR_COUNTER
		BAD_RECORDS_LONG=3
		BAD_RECORDS_SHORT=3
[root@hadoop10 ~]# 

```
# join
```
输入：
action表：
product1"trade1
product2"trade2
product3"trade3

alipay表：
product1"pay1
product2"pay2
product2"pay4
product3"pay3

输出：
trade1	pay1
trade2	pay2
trade2	pay4
trade3	pay3

code ------------
https://blog.csdn.net/bitcarmanlee/article/details/51863358

hdfs dfs -ls /employee.txt  /salary.txt
hdfs dfs -rm /employee.txt  /salary.txt
cat >  salary.txt <<EOF
jd,1600
tb,1800
elong,2000
tengxun,2200
EOF

cat > employee.txt <<EOF
jd,david
jd,mike
tb,mike
tb,lucifer
elong,xiaoming
elong,ali
tengxun,xiaoming
tengxun,lilei
xxx,aaa
EOF

hdfs dfs -put employee.txt  salary.txt  /
hdfs dfs -ls /employee.txt  /salary.txt
hdfs dfs -rm -r /output30

hadoop jar MapReduce-SecondarySort-1.0.0-SNAPSHOT.jar org.demo.join.LeftJoin  \
    -D input_dir=/employee.txt,/salary.txt \
    -D output_dir=/output30 \
    -D mapred.textoutputformat.separator=","
hdfs dfs -cat /output30/*


运行结果
[root@hadoop10 ~]# hdfs dfs -ls /employee.txt  /salary.txt
xun,lilei
xxx,aaa
EOF

hdfs dfs -put employee.txt  salary.txt  /
hdfs dfs -ls /employee.txt  /salary.txt
hdfs dfs -rm -r /output30

hadoop jar MapReduce-SecondarySort-1.0.0-SNAPSHOT.jar org.demo.join.LeftJoin -D input_dir=/employee.txt,/salary.txt -D output_dir=/output30 -D mapred.textoutputformat.separator=","
hdfs dfs -cat /output30/*
-rw-r--r--   3 root supergroup        100 2020-07-16 19:24 /employee.txt
-rw-r--r--   3 root supergroup         40 2020-07-16 19:24 /salary.txt
[root@hadoop10 ~]# hdfs dfs -rm /employee.txt  /salary.txt
20/07/16 19:25:18 INFO fs.TrashPolicyDefault: Namenode trash configuration: Deletion interval = 0 minutes, Emptier interval = 0 minutes.
Deleted /employee.txt
20/07/16 19:25:18 INFO fs.TrashPolicyDefault: Namenode trash configuration: Deletion interval = 0 minutes, Emptier interval = 0 minutes.
Deleted /salary.txt
[root@hadoop10 ~]# cat >  salary.txt <<EOF
> jd,1600
> tb,1800
> elong,2000
> tengxun,2200
> EOF
[root@hadoop10 ~]# 
[root@hadoop10 ~]# cat > employee.txt <<EOF
> jd,david
> jd,mike
> tb,mike
> tb,lucifer
> elong,xiaoming
> elong,ali
> tengxun,xiaoming
> tengxun,lilei
> xxx,aaa
> EOF
[root@hadoop10 ~]# 
[root@hadoop10 ~]# hdfs dfs -put employee.txt  salary.txt  /
[root@hadoop10 ~]# hdfs dfs -ls /employee.txt  /salary.txt
-rw-r--r--   3 root supergroup        100 2020-07-16 19:25 /employee.txt
-rw-r--r--   3 root supergroup         40 2020-07-16 19:25 /salary.txt
[root@hadoop10 ~]# hdfs dfs -rm -r /output30
20/07/16 19:25:23 INFO fs.TrashPolicyDefault: Namenode trash configuration: Deletion interval = 0 minutes, Emptier interval = 0 minutes.
Deleted /output30
[root@hadoop10 ~]# 
[root@hadoop10 ~]# hadoop jar MapReduce-SecondarySort-1.0.0-SNAPSHOT.jar org.demo.join.LeftJoin  \
>     -D input_dir=/employee.txt,/salary.txt \
>     -D output_dir=/output30 \
>     -D mapred.textoutputformat.separator=","
20/07/16 19:25:26 INFO Configuration.deprecation: mapred.textoutputformat.separator is deprecated. Instead, use mapreduce.output.textoutputformat.separator
20/07/16 19:25:26 INFO client.RMProxy: Connecting to ResourceManager at hadoop11/192.168.5.11:8032
20/07/16 19:25:27 INFO input.FileInputFormat: Total input paths to process : 2
20/07/16 19:25:27 INFO mapreduce.JobSubmitter: number of splits:2
20/07/16 19:25:27 INFO mapreduce.JobSubmitter: Submitting tokens for job: job_1594894717477_0012
20/07/16 19:25:27 INFO impl.YarnClientImpl: Submitted application application_1594894717477_0012
20/07/16 19:25:27 INFO mapreduce.Job: The url to track the job: http://hadoop11:8088/proxy/application_1594894717477_0012/
20/07/16 19:25:27 INFO mapreduce.Job: Running job: job_1594894717477_0012
20/07/16 19:25:32 INFO mapreduce.Job: Job job_1594894717477_0012 running in uber mode : false
20/07/16 19:25:32 INFO mapreduce.Job:  map 0% reduce 0%
20/07/16 19:25:38 INFO mapreduce.Job:  map 100% reduce 0%
20/07/16 19:25:43 INFO mapreduce.Job:  map 100% reduce 100%
20/07/16 19:25:43 INFO mapreduce.Job: Job job_1594894717477_0012 completed successfully
20/07/16 19:25:43 INFO mapreduce.Job: Counters: 49
	File System Counters
		FILE: Number of bytes read=204
		FILE: Number of bytes written=474002
		FILE: Number of read operations=0
		FILE: Number of large read operations=0
		FILE: Number of write operations=0
		HDFS: Number of bytes read=334
		HDFS: Number of bytes written=145
		HDFS: Number of read operations=12
		HDFS: Number of large read operations=0
		HDFS: Number of write operations=4
	Job Counters 
		Launched map tasks=2
		Launched reduce tasks=2
		Data-local map tasks=2
		Total time spent by all maps in occupied slots (ms)=5026
		Total time spent by all reduces in occupied slots (ms)=5311
		Total time spent by all map tasks (ms)=5026
		Total time spent by all reduce tasks (ms)=5311
		Total vcore-milliseconds taken by all map tasks=5026
		Total vcore-milliseconds taken by all reduce tasks=5311
		Total megabyte-milliseconds taken by all map tasks=5146624
		Total megabyte-milliseconds taken by all reduce tasks=5438464
	Map-Reduce Framework
		Map input records=13
		Map output records=13
		Map output bytes=166
		Map output materialized bytes=216
		Input split bytes=194
		Combine input records=0
		Combine output records=0
		Reduce input groups=5
		Reduce shuffle bytes=216
		Reduce input records=13
		Reduce output records=9
		Spilled Records=26
		Shuffled Maps =4
		Failed Shuffles=0
		Merged Map outputs=4
		GC time elapsed (ms)=222
		CPU time spent (ms)=2810
		Physical memory (bytes) snapshot=882728960
		Virtual memory (bytes) snapshot=8444780544
		Total committed heap usage (bytes)=601358336
	Shuffle Errors
		BAD_ID=0
		CONNECTION=0
		IO_ERROR=0
		WRONG_LENGTH=0
		WRONG_MAP=0
		WRONG_REDUCE=0
	File Input Format Counters 
		Bytes Read=140
	File Output Format Counters 
		Bytes Written=145
[root@hadoop10 ~]# hdfs dfs -cat /output30/*
elong,ali,2000
elong,xiaoming,2000
tengxun,lilei,2200
tengxun,xiaoming,2200
jd,mike,1600
jd,david,1600
tb,lucifer,1800
tb,mike,1800
xxx,aaa,null
[root@hadoop10 ~]# 
```
 

# 分布式缓存
https://www.cnblogs.com/oftenlin/p/3592005.html
``` 
:>cache.db
hdfs dfs -put ./cache.db /
hdfs dfs -rm -r /output40
hadoop jar MapReduce-SecondarySort-1.0.0-SNAPSHOT.jar org.demo.cache.Cache /employee.txt /output40
运行结果
[root@hadoop10 ~]# :>cache.db
[root@hadoop10 ~]# hdfs dfs -put ./cache.db /
put: `/cache.db': File exists
[root@hadoop10 ~]# hdfs dfs -rm -r /output40
20/07/16 19:58:39 INFO fs.TrashPolicyDefault: Namenode trash configuration: Deletion interval = 0 minutes, Emptier interval = 0 minutes.
Deleted /output40
[root@hadoop10 ~]# hadoop jar MapReduce-SecondarySort-1.0.0-SNAPSHOT.jar org.demo.cache.Cache /employee.txt /output40
20/07/16 19:58:43 INFO Configuration.deprecation: mapred.job.tracker is deprecated. Instead, use mapreduce.jobtracker.address
运行模式：  local
20/07/16 19:58:43 INFO client.RMProxy: Connecting to ResourceManager at hadoop11/192.168.5.11:8032
20/07/16 19:58:44 WARN mapreduce.JobResourceUploader: Hadoop command-line option parsing not performed. Implement the Tool interface and execute your application with ToolRunner to remedy this.
20/07/16 19:58:44 INFO input.FileInputFormat: Total input paths to process : 1
20/07/16 19:58:44 INFO mapreduce.JobSubmitter: number of splits:1
20/07/16 19:58:44 INFO mapreduce.JobSubmitter: Submitting tokens for job: job_1594894717477_0016
20/07/16 19:58:44 INFO impl.YarnClientImpl: Submitted application application_1594894717477_0016
20/07/16 19:58:44 INFO mapreduce.Job: The url to track the job: http://hadoop11:8088/proxy/application_1594894717477_0016/
20/07/16 19:58:44 INFO mapreduce.Job: Running job: job_1594894717477_0016
20/07/16 19:58:49 INFO mapreduce.Job: Job job_1594894717477_0016 running in uber mode : false
20/07/16 19:58:49 INFO mapreduce.Job:  map 0% reduce 0%
20/07/16 19:58:53 INFO mapreduce.Job:  map 100% reduce 0%
20/07/16 19:58:57 INFO mapreduce.Job:  map 100% reduce 100%
20/07/16 19:58:57 INFO mapreduce.Job: Job job_1594894717477_0016 completed successfully
20/07/16 19:58:58 INFO mapreduce.Job: Counters: 49
	File System Counters
		FILE: Number of bytes read=69
		FILE: Number of bytes written=235997
		FILE: Number of read operations=0
		FILE: Number of large read operations=0
		FILE: Number of write operations=0
		HDFS: Number of bytes read=198
		HDFS: Number of bytes written=0
		HDFS: Number of read operations=6
		HDFS: Number of large read operations=0
		HDFS: Number of write operations=2
	Job Counters 
		Launched map tasks=1
		Launched reduce tasks=1
		Data-local map tasks=1
		Total time spent by all maps in occupied slots (ms)=1873
		Total time spent by all reduces in occupied slots (ms)=1825
		Total time spent by all map tasks (ms)=1873
		Total time spent by all reduce tasks (ms)=1825
		Total vcore-milliseconds taken by all map tasks=1873
		Total vcore-milliseconds taken by all reduce tasks=1825
		Total megabyte-milliseconds taken by all map tasks=1917952
		Total megabyte-milliseconds taken by all reduce tasks=1868800
	Map-Reduce Framework
		Map input records=9
		Map output records=9
		Map output bytes=45
		Map output materialized bytes=69
		Input split bytes=98
		Combine input records=0
		Combine output records=0
		Reduce input groups=1
		Reduce shuffle bytes=69
		Reduce input records=9
		Reduce output records=0
		Spilled Records=18
		Shuffled Maps =1
		Failed Shuffles=0
		Merged Map outputs=1
		GC time elapsed (ms)=72
		CPU time spent (ms)=800
		Physical memory (bytes) snapshot=434913280
		Virtual memory (bytes) snapshot=4222070784
		Total committed heap usage (bytes)=308805632
	Shuffle Errors
		BAD_ID=0
		CONNECTION=0
		IO_ERROR=0
		WRONG_LENGTH=0
		WRONG_MAP=0
		WRONG_REDUCE=0
	File Input Format Counters 
		Bytes Read=100
	File Output Format Counters 
		Bytes Written=0
[root@hadoop10 ~]# 

# id	pid	amount
cat > order.txt <<EOF
1001,01,1
1002,02,2
1003,03,3
1004,01,4
1005,02,5
1006,03,6
EOF

# pd.txt 
# pid	pname
cat > product.txt <<EOF
01,xiaomi
02,huawei
03,geli
EOF

hdfs dfs -rm /order.txt /product.txt 
hdfs dfs -put order.txt product.txt /
hdfs dfs -ls /order.txt /product.txt 
hdfs dfs -rm -r /output50
hadoop jar MapReduce-SecondarySort-1.0.0-SNAPSHOT.jar org.demo.cache.DistributedCacheDriver /order.txt /output50
hdfs dfs -cat /output50/*



[root@hadoop10 ~]# hdfs dfs -rm /order.txt /product.txt 
20/07/16 21:31:48 INFO fs.TrashPolicyDefault: Namenode trash configuration: Deletion interval = 0 minutes, Emptier interval = 0 minutes.
Deleted /order.txt
20/07/16 21:31:48 INFO fs.TrashPolicyDefault: Namenode trash configuration: Deletion interval = 0 minutes, Emptier interval = 0 minutes.
Deleted /product.txt
[root@hadoop10 ~]# hdfs dfs -put order.txt product.txt /
[root@hadoop10 ~]# hdfs dfs -ls /order.txt /product.txt 
-rw-r--r--   3 root supergroup         60 2020-07-16 21:31 /order.txt
-rw-r--r--   3 root supergroup         28 2020-07-16 21:31 /product.txt
[root@hadoop10 ~]# hdfs dfs -rm -r /output50
20/07/16 21:31:53 INFO fs.TrashPolicyDefault: Namenode trash configuration: Deletion interval = 0 minutes, Emptier interval = 0 minutes.
Deleted /output50
[root@hadoop10 ~]# hadoop jar MapReduce-SecondarySort-1.0.0-SNAPSHOT.jar org.demo.cache.DistributedCacheDriver /order.txt /output50
20/07/16 21:31:56 INFO client.RMProxy: Connecting to ResourceManager at hadoop11/192.168.5.11:8032
20/07/16 21:31:56 WARN mapreduce.JobResourceUploader: Hadoop command-line option parsing not performed. Implement the Tool interface and execute your application with ToolRunner to remedy this.
20/07/16 21:31:56 INFO input.FileInputFormat: Total input paths to process : 1
20/07/16 21:31:56 INFO mapreduce.JobSubmitter: number of splits:1
20/07/16 21:31:56 INFO mapreduce.JobSubmitter: Submitting tokens for job: job_1594894717477_0031
20/07/16 21:31:57 INFO impl.YarnClientImpl: Submitted application application_1594894717477_0031
20/07/16 21:31:57 INFO mapreduce.Job: The url to track the job: http://hadoop11:8088/proxy/application_1594894717477_0031/
20/07/16 21:31:57 INFO mapreduce.Job: Running job: job_1594894717477_0031
20/07/16 21:32:02 INFO mapreduce.Job: Job job_1594894717477_0031 running in uber mode : false
20/07/16 21:32:02 INFO mapreduce.Job:  map 0% reduce 0%
20/07/16 21:32:07 INFO mapreduce.Job:  map 100% reduce 0%
20/07/16 21:32:07 INFO mapreduce.Job: Job job_1594894717477_0031 completed successfully
20/07/16 21:32:07 INFO mapreduce.Job: Counters: 30
	File System Counters
		FILE: Number of bytes read=0
		FILE: Number of bytes written=117666
		FILE: Number of read operations=0
		FILE: Number of large read operations=0
		FILE: Number of write operations=0
		HDFS: Number of bytes read=155
		HDFS: Number of bytes written=98
		HDFS: Number of read operations=5
		HDFS: Number of large read operations=0
		HDFS: Number of write operations=2
	Job Counters 
		Launched map tasks=1
		Data-local map tasks=1
		Total time spent by all maps in occupied slots (ms)=2157
		Total time spent by all reduces in occupied slots (ms)=0
		Total time spent by all map tasks (ms)=2157
		Total vcore-milliseconds taken by all map tasks=2157
		Total megabyte-milliseconds taken by all map tasks=2208768
	Map-Reduce Framework
		Map input records=6
		Map output records=6
		Input split bytes=95
		Spilled Records=0
		Failed Shuffles=0
		Merged Map outputs=0
		GC time elapsed (ms)=41
		CPU time spent (ms)=470
		Physical memory (bytes) snapshot=171835392
		Virtual memory (bytes) snapshot=2111307776
		Total committed heap usage (bytes)=94371840
	File Input Format Counters 
		Bytes Read=60
	File Output Format Counters 
		Bytes Written=98
[root@hadoop10 ~]# hdfs dfs -cat /output50/*
1001,01,1	xiaomi
1002,02,2	huawei
1003,03,3	geli
1004,01,4	xiaomi
1005,02,5	huawei
1006,03,6	geli
[root@hadoop10 ~]# 



```