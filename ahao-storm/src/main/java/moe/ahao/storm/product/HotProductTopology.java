package moe.ahao.storm.product;

import moe.ahao.storm.product.bolt.HotProductCountBolt;
import moe.ahao.storm.product.bolt.LogParseBolt;
import moe.ahao.storm.product.spout.AccessLogKafkaSpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.utils.Utils;

public class HotProductTopology {
    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        // 为这个Spout设置1个executor, 因为kafka分区只有1个
        builder.setSpout(AccessLogKafkaSpout.class.getSimpleName(), new AccessLogKafkaSpout(), 1);
        // 为这个Bolt设置5个executor, 设置5个Task
        builder.setBolt(LogParseBolt.class.getSimpleName(), new LogParseBolt(), 5)
            .setNumTasks(5)
            .shuffleGrouping(AccessLogKafkaSpout.class.getSimpleName());
        // 为这个Bolt设置5个executor, 设置10个Task
        builder.setBolt(HotProductCountBolt.class.getSimpleName(), new HotProductCountBolt(), 5)
            .fieldsGrouping(LogParseBolt.class.getSimpleName(), new Fields(LogParseBolt.FIELD));

        Config config = new Config();
        config.setDebug(true);

        if (args != null && args.length > 0) {
            // 在命令行执行, 提交到Storm集群
            config.setNumWorkers(3);
            StormSubmitter.submitTopologyWithProgressBar(args[0], config, builder.createTopology());
        } else {
            // 本地运行的
            config.setMaxTaskParallelism(3);

            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology(HotProductTopology.class.getSimpleName(), config, builder.createTopology());

            Utils.sleep(30000);

            cluster.shutdown();
        }
    }
}
