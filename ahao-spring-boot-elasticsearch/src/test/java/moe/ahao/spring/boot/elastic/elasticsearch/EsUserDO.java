package moe.ahao.spring.boot.elastic.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "es_user")
@Setting(shards = 1, replicas = 1, refreshInterval = "1s")
public class EsUserDO {
    @Id
    @Field(type = FieldType.Keyword)
    private String esId;
    @Field(type = FieldType.Keyword)
    private Long dbId;
    @Field(type = FieldType.Keyword)
    private String username;
}
