# 简介
`Btrace`是一个可以将自己的代码, 注入到正在运行的代码, 并且不用重启应用的东西.

# 使用方法
`Btrace`最新的版本在官方`maven`仓库找不到, 只放在了[`btrace`自己的仓库](https://dl.bintray.com/btraceio/maven/)里.
所以需要配置下`pom.xml`
```xml
<repository>
    <id>nexus-btrace</id>
    <name>Nexus btrace</name>
    <url>https://dl.bintray.com/btraceio/maven/</url>
</repository>
```
然后`mvn clear install`一下, 将`jar`包下载到本地即可.

# 官方示例
具体的使用方法参考官方教程
https://github.com/btraceio/btrace/tree/master/samples
