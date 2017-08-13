import com.ahao.util.StringHelper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Ahaochan on 2017/7/30.
 */
public class JsoupTest {


    @Test
    public void getProvince() throws IOException {
        String url = "http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/201703/t20170310_1471429.html";
        Document doc = Jsoup.connect(url).get();
        Elements msoNormals = doc.getElementsByClass("MsoNormal");
        JSONArray array = new JSONArray(msoNormals.size());
        for (Element msoNormal : msoNormals) {
            Elements spans = msoNormal.getElementsByTag("span");
            JSONObject province = new JSONObject();

            spans.stream()
                    .filter(s -> StringHelper.isNotEmpty(s.text()))
                    .forEach(s -> {
                        String text = StringHelper.trim(s.text());
                        if (StringHelper.isNumeric(text) && StringHelper.endsWith(text, "00")) {
                            province.put("code", text);
                        } else if (province.containsKey("code")) {
                            province.put("city", text);
                        }
                    });
            if (province.containsKey("code") && !StringHelper.equals(province.getString("city"),
                    "市辖区", "省直辖县级行政区划")) {
                array.add(province);
            }
        }

        System.out.println(array.toJSONString());
    }

    @Test
    public void test() {
        System.out.println("[" + StringHelper.trim("　市辖区") + "]");
    }

}
