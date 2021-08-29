package moe.ahao.storm.product.spout;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class AccessLogKafkaSpout extends BaseRichSpout {
    public static final String FIELD = "message";
    public static final String HOST = "127.0.0.1:9092";
    private SpoutOutputCollector collector;
    private Consumer<String, String> consumer;

    @Override
    public void open(Map map, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        this.consumer = this.initKafkaConsumer();
    }

    @Override
    public void nextTuple() {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10)); // 拉取 10s 的消息
        for (ConsumerRecord<String, String> record : records) {
            String value = record.value();
            collector.emit(new Values(value));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(FIELD));
    }

    private Consumer<String, String> initKafkaConsumer() {
        Map<String, Object> prop = new HashMap<>();
        prop.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, HOST);
        prop.put(ConsumerConfig.GROUP_ID_CONFIG, "hot-product-group");
        prop.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, String.valueOf(true));
        prop.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, String.valueOf(100));
        prop.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, String.valueOf(15000));
        prop.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        prop.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // prop.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new KafkaConsumer<>(prop);
    }
}
