// make sure the test methods are from JUnit 5 (org.junit.jupiter.api) and not 4.
// Not sure why the old versions keep showing up in auto-imports
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DummyTest {

    @Test
    public void dummy() {
        assertEquals("yes", "yes", "Dummy test");
    }
}