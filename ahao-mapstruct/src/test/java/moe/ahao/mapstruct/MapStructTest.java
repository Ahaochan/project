package moe.ahao.mapstruct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

public class MapStructTest {

    @Test
    public void shouldMapCarToDto() {
        //given
        Source source = new Source("same", 1);

        //when
        Target target = AhaoMapper.INSTANCE.convert(source);

        //then
        Assertions.assertNotNull(target);
        Assertions.assertEquals(source.getSame(), target.getSame());
        Assertions.assertEquals(source.getSourceInt(), target.getTargetInt());
    }

    @Mapper
    public interface AhaoMapper {
        AhaoMapper INSTANCE = Mappers.getMapper(AhaoMapper.class);
        @Mappings(
            @Mapping(source = "sourceInt", target = "targetInt")
        )
        Target convert(Source source);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Source {
        private String same;
        private int sourceInt;
    }

    @Data
    public static class Target {
        private String same;
        private int targetInt;
    }
}
