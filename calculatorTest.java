import org.junit.Test;
import static org.junit.Assert.*;

public class calculatorTest {
    @Test
    public void testAddition() {
        calculator calc = new calculator();
        assertEquals(5, calc.add(2, 3));
    }
}
