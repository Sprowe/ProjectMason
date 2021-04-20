package edu.wpi.teamo.database.request;

import edu.wpi.teamo.database.request.MedicineRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.sql.SQLException;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import edu.wpi.teamo.database.Database;

public class MedicineRequestTest {

    @Test
    public void testInitTable() throws SQLException, ClassNotFoundException {
        Database db = new Database(Database.getMemoryURIFromName("testInitTable"));
        MedicineRequest.initTable(db);
        String[] loc = {"loc"};
        MedicineRequest s = new MedicineRequest("id", "test", "10", false, Stream.of(loc), "me");
        s.update(db);
    }

    @Test
    public void testGetByID() throws Exception {
        Database db = new Database(Database.getMemoryURIFromName("testGetByID"));
        MedicineRequest.initTable(db);
        String[] loc = { "loc" };
        MedicineRequest s = new MedicineRequest("id", "test", "10", false, Stream.of(loc), "me");
        s.update(db);
        assertEquals("id", MedicineRequest.getByID(db, "id").getID());
        assertEquals("loc", MedicineRequest.getByID(db, "id").getLocationIDs().findFirst().orElseThrow(Exception::new));
    }

    @Test
    public void testGetAll() throws SQLException, ClassNotFoundException {
        Database db = new Database(Database.getMemoryURIFromName("testGetAll"));
        MedicineRequest.initTable(db);
        String[] loc = { "loc" };
        MedicineRequest s = new MedicineRequest("id", "test", "10", false, Stream.of(loc), "me");
        s.update(db);
        assertEquals(1, MedicineRequest.getAll(db).count());
    }

    @Test
    public void testUpdate() throws SQLException, ClassNotFoundException {
        Database db = new Database(Database.getMemoryURIFromName("testUpdate"));
        MedicineRequest.initTable(db);
        String[] loc = { "loc" };
        MedicineRequest s = new MedicineRequest("id", "test", "10", false, Stream.of(loc), "me");
        s.update(db);
        assertEquals(false, MedicineRequest.getByID(db, "id").isComplete());
        s.setComplete(true);
        s.update(db);
        assertEquals(true, MedicineRequest.getByID(db, "id").isComplete());
    }

    @Test
    public void testDelete() throws SQLException, ClassNotFoundException {
        Database db = new Database(Database.getMemoryURIFromName("testDelete"));
        MedicineRequest.initTable(db);
        String[] loc = { "loc" };
        MedicineRequest s = new MedicineRequest("id", "test", "10", false, Stream.of(loc), "me");
        s.update(db);
        assertEquals("id", MedicineRequest.getByID(db, "id").getID());
        s.delete(db);
        assertThrows(SQLException.class, () -> MedicineRequest.getByID(db, "id").getID());
    }
}
