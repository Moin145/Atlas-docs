import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JUnitTest {
    int x;
    int y;

    @BeforeEach
    void setUp() {
        x = 10;
        y = 20;
        System.out.println("Values initialized: x = " + x + ", y = " + y);
    }

    @AfterEach
    void tearDown() {
        System.out.println("Test completed.\n");
    }

    @Test
    void testAddition() {
        System.out.println("Running Test 01: Addition");
        assertEquals(30, x + y);
    }

    @Test
    void testSubtraction() {
        System.out.println("Running Test 02: Subtraction");
        assertEquals(-10, x - y);
    }

    @Test
    void testMultiplication() {
        System.out.println("Running Test 03: Multiplication");
        assertEquals(200, x * y);
    }

    @Test
    void testDivision() {
        System.out.println("Running Test 04: Division");
        assertEquals(0, x / y); // since 10 / 20 = 0 (integer division)
    }

    @Test
    void testStringMatch() {
        System.out.println("Running Test 05: String Matching");
        String loginid = "Prasunamba";
        assertEquals("Prasunamba", loginid);
    }
}
