import com.hl.bootssm.components.Sender;
import com.hl.bootssm.utils.DateTools;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RabbitTest extends MainTest {
    @Autowired
    private Sender sender;

    @Test
    public void test() throws Exception {
        while (true) {
            String msg = DateTools.getCurrentDateTime() + "";
            sender.send(msg);
            Thread.sleep(2000);
        }
    }
}