package moe.ahao.storm.product.bolt;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.trident.util.LRUMap;
import org.apache.storm.tuple.Tuple;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.Map;
import java.util.stream.Collectors;

public class HotProductCountBolt extends BaseRichBolt {
    private OutputCollector collector;
    private LRUMap<Long, Long> lru = new LRUMap<>(1000);
    private CuratorFramework zk;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.zk = this.getZKClient("192.168.75.134:2181");
        this.initZKNode(context.getThisTaskId());
    }

    @Override
    public void execute(Tuple input) {
        Long id = input.getLongByField(LogParseBolt.FIELD);

        Long count = lru.get(id);
        if (count == null) {
            count = 0L;
        }
        count++;
        lru.put(id, count);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }


    public class Task implements Runnable {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                lru.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue())
                    .map(Map.Entry::getValue)
                    .limit(3)
                    .collect(Collectors.toList());

                // TODO 上传到ZK
                // zk.setData().withVersion(stat.getVersion()).forPath(path, newData);
            }
        }
    }

    private void initZKNode(int taskId) {
        String path = "/hot-product-" + taskId;

        try {
            Stat exists = zk.checkExists().forPath(path);
            if (exists == null) {
                zk.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path, new byte[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private CuratorFramework getZKClient(String connectString) {
        CuratorFramework zk = CuratorFrameworkFactory.builder()
            .connectString(connectString)
            .sessionTimeoutMs(10000)
            .namespace("hot-product-ns")
            .build();
        zk.start();
        return zk;
    }

}
