package moe.ahao.spring.boot.feishu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import moe.ahao.spring.boot.feishu.feign.dto.FeishuMessageCardURL;

import java.util.List;

@Data
public class MyCardReq {
    @JsonProperty("title")
    private String title;
    @JsonProperty("alarm_message")
    private String alarmMessage;
    @JsonProperty("error_message")
    private String errorMessage;
    @JsonProperty("download_url")
    private FeishuMessageCardURL downloadUrl;
    @JsonProperty("table_raw_array")
    private List<MyTableRow> tableRawArray;

    @Data
    public static class MyTableRow {
        @JsonProperty("no")
        private String no;
        @JsonProperty("status")
        private String status;
    }
}
