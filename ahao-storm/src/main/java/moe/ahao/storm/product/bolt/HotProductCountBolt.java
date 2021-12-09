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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HotProductCountBolt extends BaseRichBolt {
    private static final int LRU_SIZE = 1000;
    private static final int PRE_WARM_SIZE = 10;

    private final LRUMap<Long, Long> lru = new LRUMap<>(LRU_SIZE);
    private CuratorFramework zk;
    private String zkNodePath;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.zk = this.getZKClient("192.168.75.134:2181");
        this.zkNodePath = "/hot-product-" + context.getThisTaskId();
        this.initZKNode(zkNodePath);

        new Thread(new PreWarmTask()).start();
        new Thread(new HotTask()).start();
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


    public class PreWarmTask implements Runnable {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                String preWarmData = lru.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue())
                    .map(Map.Entry::getValue)
                    .map(Object::toString)
                    .limit(PRE_WARM_SIZE)
                    .collect(Collectors.joining(","));

                try {
                    zk.setData().forPath(zkNodePath, preWarmData.getBytes(StandardCharsets.UTF_8));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public class HotTask implements Runnable {
        @Override
        public void run() {
            List<Long> lastHotIdList = new ArrayList<>();
            while (!Thread.interrupted()) {
                // 1. 排序
                List<Map.Entry<Long, Long>> list = lru.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toList());

                // 2. 统计后95%的访问量平均值
                int listSize = list.size();
                int percent95 = (int) (listSize * 0.95);
                int percent5  = listSize - percent95;
                long percent95Sum = 0;
                for (int i = listSize - 1; i >= percent5; i--) {
                    long count = list.get(i).getValue();
                    percent95Sum += count;
                }
                long percent95Avg = (long) (percent95Sum * 1.0 / percent95);

                // 3. 找出超过访问量平均值10倍的商品
                List<Long> hotIdList = new ArrayList<>();
                for (Map.Entry<Long, Long> entry : list) {
                    Long id = entry.getKey();
                    Long count = entry.getValue();
                    if (count > percent95Avg * 10) {
                        // 3.1. 如果不在上次的热点商品集合里
                        if(!lastHotIdList.contains(id)) {
                            // TODO 将缓存热点反向推送到流量分发的nginx中
                            // TODO 将缓存热点，那个商品对应的完整的缓存数据，发送请求到缓存服务去获取，反向推送到所有的后端应用nginx服务器上去
                        }

                        // 3.2. 记录本次的热门商品id
                        hotIdList.add(id);
                    }
                }

                // 4. 清理过时的热点数据
                for (Long hotId : lastHotIdList) {
                    if(!hotIdList.contains(hotId)) {
                        // TODO 清理热点数据
                    }
                }
                lastHotIdList = hotIdList;
            }
        }
    }

    private void initZKNode(String path) {
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
