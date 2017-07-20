import com.ahao.util.ArrayUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Ahaochan on 2017/7/11.
 */
public class MyTest {
    @Test
    public void test(){
        Collection c = Stream.of(1,2,3,4).collect(Collectors.toList());

        System.out.println("测试:"+ Arrays.toString(ArrayUtils.toArray(c, 1, 2)));
        System.out.println("测试:"+ Arrays.toString(ArrayUtils.toArray(c, 2, 2)));
        System.out.println("测试:"+ Arrays.toString(ArrayUtils.toArray(c, -2, 2)));

        System.out.println("测试:"+ Arrays.toString(ArrayUtils.toArray(c, 3, 2)));
        System.out.println("测试:"+ Arrays.toString(ArrayUtils.toArray(c, 4, 2)));
        System.out.println("测试:"+ Arrays.toString(ArrayUtils.toArray(c, 5, 2)));
        System.out.println("测试:"+ Arrays.toString(ArrayUtils.toArray(c, 1, -1)));

    }
}
