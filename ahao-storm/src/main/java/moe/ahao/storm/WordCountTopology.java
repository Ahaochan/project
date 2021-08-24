package moe.ahao.storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WordCountTopology {
    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        // 为这个Spout设置5个executor
        builder.setSpout("RandomSentence", new RandomSentencesSpout(), 5);
        // 为这个Bolt设置5个executor, 设置10个Task
        builder.setBolt("SplitSentence", new SplitSentence(), 5)
            .setNumTasks(10)
            .shuffleGrouping("RandomSentence");
        // 为这个Bolt设置10个executor, 设置10个Task
        builder.setBolt("WordCount", new WordCount(), 10)
            .fieldsGrouping("SplitSentence", new Fields("word"));

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
            cluster.submitTopology("word-count", config, builder.createTopology());

            Utils.sleep(60000);

            cluster.shutdown();
        }
    }

    public static class RandomSentencesSpout extends BaseRichSpout {
        private SpoutOutputCollector spoutOutputCollector;
        private Random random = new Random();

        @Override
        public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
            // 对 Spout 进行初始化, 创建线程池、创建数据库连接、创建http连接
            this.spoutOutputCollector = spoutOutputCollector;
        }

        @Override
        public void nextTuple() {
            // Task会无限循环调用, 发送数据Tuple出去, 形成数据流Stream
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String[] sentences = {
                "Spring Festival or the Lunar New Year", "is the most important festival of the Chinese traditional holidays",
                "Excitement and happiness are palpable this time of the year", "and they reach the peak on lunar new years eve",
                "It starts with the first day of the lunar new year and ends on the day",
                "Chinese people has been celebrating the Spring Festival for more than years",
                "With various activities and the longest history", "Its the most important",
                "ceremonious and jolliest holiday in our country"
            };
            String sentence = sentences[random.nextInt(sentences.length)];
            System.out.println("=======================>发射句子" + sentence);
            spoutOutputCollector.emit(new Values(sentence)); // 发送一个Tuple
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
            // 发送出去的Tuple的每个field名称
            outputFieldsDeclarer.declare(new Fields("sentence"));
        }
    }

    public static class SplitSentence extends BaseRichBolt {
        private OutputCollector outputCollector;

        @Override
        public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
            this.outputCollector = outputCollector;
        }

        @Override
        public void execute(Tuple tuple) {
            // 每接收一条Tuple就用execute处理
            String sentence = tuple.getStringByField("sentence");
            String[] words = sentence.split(" ");
            for (String word : words) {
                outputCollector.emit(new Values(word));
            }
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
            // 发送出去的Tuple的每个field名称
            outputFieldsDeclarer.declare(new Fields("word"));
        }
    }

    public static class WordCount extends BaseRichBolt {
        private OutputCollector outputCollector;
        private Map<String, Long> wordCountMap = new HashMap<>();

        @Override
        public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
            this.outputCollector = outputCollector;
        }

        @Override
        public void execute(Tuple tuple) {
            String word = tuple.getStringByField("word");
            Long count = wordCountMap.get(word);
            if (count == null) {
                count = 0L;
            }
            count++;
            wordCountMap.put(word, count);
            System.out.println("=======================>单词" + word + "出现次数是" + count);
            outputCollector.emit(new Values(word, count));
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
            outputFieldsDeclarer.declare(new Fields("word", "count"));
        }
    }
}
