import com.paxterya.base.Base;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BaseTest {

    @Test
    public void testNameWithOnePlayer() {
        Map<UUID, String> owners = new HashMap<>();
        owners.put(UUID.randomUUID(), "Veikmaster");
        Base base = new Base(null, owners, null, 0);
        assertEquals("Base of Veikmaster", base.getName());
    }

    @Test
    public void testNameWithTwoPlayers() {
        Map<UUID, String> owners = new HashMap<>();
        owners.put(UUID.randomUUID(), "Veikmaster");
        owners.put(UUID.randomUUID(), "Legmur");

        Base base = new Base(null, owners, null, 0);
        assertTrue("Base of Veikmaster and Legmur".equals(base.getName()) || "Base of Legmur and Veikmaster".equals(base.getName()));
    }

    @Test
    public void testNameWithThreePlayers() {
        Map<UUID, String> owners = new HashMap<>();
        owners.put(UUID.randomUUID(), "Veikmaster");
        owners.put(UUID.randomUUID(), "Legmur");
        owners.put(UUID.randomUUID(), "V1lj4");

        Base base = new Base(null, owners, null, 0);
        String name = base.getName();
        String[] parts = name.split(" ");

        assertTrue(parts[2].endsWith(","), "Comma after first name");
        assertFalse(parts[3].endsWith(","), "No comma after second name");
        assertEquals(6, parts.length, "Correct length");
    }

    @Test
    public void testNameWithFourPlayers() {
        Map<UUID, String> owners = new HashMap<>();
        owners.put(UUID.randomUUID(), "Veikmaster");
        owners.put(UUID.randomUUID(), "Legmur");
        owners.put(UUID.randomUUID(), "V1lj4");
        owners.put(UUID.randomUUID(), "Boromaur");

        Base base = new Base(null, owners, null, 0);
        String name = base.getName();
        String[] parts = name.split(" ");

        assertEquals(7, parts.length, "Correct length");
        assertEquals("and", parts[5], "Second to last word is 'and'");
    }
}
