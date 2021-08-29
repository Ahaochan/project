package moe.ahao.storm.product.bolt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import moe.ahao.storm.product.spout.AccessLogKafkaSpout;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

public class LogParseBolt extends BaseRichBolt {
    public static final String FIELD = "id";
    private OutputCollector collector;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        String message = input.getStringByField(AccessLogKafkaSpout.FIELD);
        try {
            JsonNode node = objectMapper.readTree(message);
            JsonNode idNode = node.at("/uri_args/id");
            long id = idNode.asLong();
            collector.emit(new Values(id));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(FIELD));
    }
}
