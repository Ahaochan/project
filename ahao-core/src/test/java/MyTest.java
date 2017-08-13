import com.ibm.icu.text.NumberFormat;
import org.junit.Test;

import java.util.Locale;

/**
 * Created by Ahaochan on 2017/7/11.
 */
public class MyTest {
    @Test
    public void test(){
        Locale chineseNumbers = new Locale("C@numbers=hansfin");
        NumberFormat formatter = NumberFormat.getInstance(chineseNumbers);
        System.out.println(formatter.format(61305));
    }
}
