package moe.ahao.spring.boot.mybatis.tk.id;

import tk.mybatis.mapper.genid.GenId;

import java.util.UUID;

/**
 * 主键生成器, UUID
 *
 * <pre>
 *     @Id
 *     @KeySql(genId = UUIdGenId.class)
 *     private String id;
 * </pre>
 */
public class UUIDGenId implements GenId<String> {
    @Override
    public String genId(String table, String column) {
        return UUID.randomUUID().toString();
    }
}
