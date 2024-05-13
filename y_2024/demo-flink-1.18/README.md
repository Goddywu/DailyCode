


```shell
# .zshenv 

JAVA_8_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_311.jdk/Contents/Home
JAVA_17_HOME=/Users/admin/software/jdk-17.0.2.jdk/Contents/Home

alias jdk8="export JAVA_HOME=$JAVA_8_HOME"
alias jdk17="export JAVA_HOME=$JAVA_17_HOME"

jdk8
```

```shell
cd /Users/admin/software/flink-1.18.0

# 启动集群
./bin/start-cluster.sh

# 停止集群
./bin/stop-cluster.sh


# 提交任务，--output是自定义入参
./bin/flink run -c com.example.demoflink.SpendReport  /Users/admin/Documents/GitHub/DailyCode/y_2024/demo-flink-1.18/target/demo-flink-1.18-1.0-SNAPSHOT.jar --output tmp-log/1.txt

./bin/flink run -c com.example.demoflink.MyTest  /Users/admin/Documents/GitHub/DailyCode/y_2024/demo-flink-1.18/target/demo-flink-1.18-1.0-SNAPSHOT.jar --output tmp-log/1.txt
```