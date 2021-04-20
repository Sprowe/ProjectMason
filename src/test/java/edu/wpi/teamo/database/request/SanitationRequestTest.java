package edu.wpi.teamo.database.request;

import edu.wpi.teamo.database.request.SanitationRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.sql.SQLException;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import edu.wpi.teamo.database.Database;

public class SanitationRequestTest {

    @Test
    public void testInitTable() throws SQLException, ClassNotFoundException {
        Database db = new Database(Database.getMemoryURIFromName("testInitTable"));
        SanitationRequest.initTable(db);
        String[] loc = {"loc"};
        SanitationRequest s = new SanitationRequest("id", Stream.of(loc), "me", "det", false);
        s.update(db);
    }

    @Test
    public void testGetByID() throws Exception {
        Database db = new Database(Database.getMemoryURIFromName("testGetByID"));
        SanitationRequest.initTable(db);
        String[] loc = {"loc"};
        SanitationRequest s = new SanitationRequest("id", Stream.of(loc), "me", "det", false);
        s.update(db);
        assertEquals("id", SanitationRequest.getByID(db, "id").getID());
        assertEquals("loc", SanitationRequest.getByID(db, "id").getLocationIDs().findFirst().orElseThrow(Exception::new));
    }

    @Test
    public void testGetAll() throws SQLException, ClassNotFoundException {
        Database db = new Database(Database.getMemoryURIFromName("testGetAll"));
        SanitationRequest.initTable(db);
        String[] loc = {"loc"};
        SanitationRequest s = new SanitationRequest("id", Stream.of(loc), "me", "det", false);
        s.update(db);
        assertEquals(1, SanitationRequest.getAll(db).count());
    }

    @Test
    public void testUpdate() throws SQLException, ClassNotFoundException {
        Database db = new Database(Database.getMemoryURIFromName("testUpdate"));
        SanitationRequest.initTable(db);
        String[] loc = { "loc" };
        SanitationRequest s = new SanitationRequest("id", Stream.of(loc), "me", "det", false);
        s.update(db);
        assertEquals(false, SanitationRequest.getByID(db, "id").isComplete());
        s.setComplete(true);
        s.update(db);
        assertEquals(true, SanitationRequest.getByID(db, "id").isComplete());
    }

    @Test
    public void testDelete() throws SQLException, ClassNotFoundException {
        Database db = new Database(Database.getMemoryURIFromName("testDelete"));
        SanitationRequest.initTable(db);
        String[] loc = {"loc"};
        SanitationRequest s = new SanitationRequest("id", Stream.of(loc), "me", "det", false);
        s.update(db);
        assertEquals("id", SanitationRequest.getByID(db, "id").getID());
        s.delete(db);
        assertThrows(SQLException.class, () -> SanitationRequest.getByID(db, "id").getID());
    }
}
