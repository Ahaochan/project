package moe.ahao.storm.product.bolt;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.trident.util.LRUMap;
import org.apache.storm.tuple.Tuple;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class HotProductCountBolt extends BaseRichBolt {
    private OutputCollector collector;
    private LRUMap<Long, Long> lru = new LRUMap<>(1000);

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        Long id = input.getLongByField(LogParseBolt.FIELD);

        Long count = lru.get(id);
        if(count == null) {
            count = 0L;
        }
        count++;
        lru.put(id, count);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }


    public class Task implements Callable<Map<Long, Long>> {
        @Override
        public Map<Long, Long> call() throws Exception {
            while (!Thread.interrupted()) {
                lru.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue())
                    .limit(3)
                    .collect(Collectors.toList());

                // TODO 上传到ZK
            }
            return null;
        }
    }
}
