import com.ahao.entity.PageDTO;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by Ahaochan on 2017/7/21.
 */
public class PageDTOTest {

    /**
     * 1: | 1 2 3 4 5 | ... 99 100
     * 2: | 1 2 3 4 5 | ... 99 100
     * 3: | 1 2 3 4 5 | ... 99 100
     * 4: 1 | 2 3 4 5 6 |  ... 99 100
     * 5: 1 2 | 3 4 5 6 7 |  ... 99 100
     * 6: 1 2 | ... 4 5 6 7 8 |  ... 99 100
     * 95: 1 2 |  ... 93 94 95 96 97 |  ... 99 100
     * 96: 1 2 |  ... 94 95 96 97 98 |  99 100
     * 97: 1 2 |  ... 95 96 97 98 99 |  100
     * 98 : 1 2 |  ... 96 97 98 99 100 |
     * 99 : 1 2 |  ... 96 97 98 99 100 |
     * 100: 1 2 |  ... 96 97 98 99 100 |
     * 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15
     */
    @Test
    public void test(String[] args) {
        test(-1);
        test(0);
        test(1);
        test(2);
        test(3);
        test(4);
        test(5);
        test(6);
        test(95);
        test(96);
        test(97);
        test(98);
        test(99);
        test(100);
        test(101);

    }

    public void test(int page) {
        PageDTO dto = new PageDTO(25 * 100, page, "/admin/users");
        StringBuilder sb = new StringBuilder();
        sb.append(page + " : " + Arrays.toString(dto.getHead().toArray()) + "|" +
                Arrays.toString(dto.getMid().toArray()) + "|" +
                Arrays.toString(dto.getTail().toArray()));

        sb.append(",前一页:" + (dto.getPre() == null ? "null" : dto.getPre().getIndex()));
        sb.append(",后一页:" + (dto.getNext() == null ? "null" : dto.getNext().getIndex()));
        System.out.println(sb.toString());
    }
}
