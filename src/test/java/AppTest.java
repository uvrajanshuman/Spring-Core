import com.refstash.App;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AppTest {
    @Test
    public void test() {
        Assertions.assertEquals(App.greet(), "Hello world!");
    }
}
