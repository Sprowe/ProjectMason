package edu.wpi.teamo.database.request;

import edu.wpi.teamo.database.Database;
import edu.wpi.teamo.database.map.Edge;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class RequestDBTests {

    @Test
    public void requestMedicine() throws Exception {
        String assigned = "as";
        Stream<String> locs = Stream.of(new String[]{ "loc" });
        String amount = "a";
        String type = "t";

        Database.setTesting ("requestMedicine");
        Database db = Database.getInstance ();
        RequestDB rDB = new RequestDB(db);
        String id = rDB.requestMedicine(type, amount, locs, assigned);

        try {
            MedicineRequest medReqToCheck = rDB.getMedicineRequest(id);
            assertEquals("loc", medReqToCheck.getLocations().findFirst().orElseThrow(Exception::new));
            assertEquals(assigned, medReqToCheck.getAssigned());
            assertEquals(amount, medReqToCheck.getAmount());
            assertEquals(type, medReqToCheck.getType());
            assertEquals(id, medReqToCheck.getID());
        } catch (SQLException e) {
            fail(e.getMessage());
        }

        db.close();
    }

    @Test
    public void requestSanitation() throws Exception {
        String assigned = "a";
        String details = "d";
        Stream<String> locs = Stream.of(new String[]{ "loc" });

        Database.setTesting ("requestSanitation");
        Database db = Database.getInstance ();
        RequestDB rDB = new RequestDB(db);
        String id = rDB.requestSanitation(locs, assigned, details);

        try {
            SanitationRequest sanReqToCheck = rDB.getSanitationRequest(id);
            assertEquals("loc", sanReqToCheck.getLocations().findFirst().orElseThrow(Exception::new));
            assertEquals(assigned, sanReqToCheck.getAssigned());
            assertEquals(id, sanReqToCheck.getID());
        } catch (SQLException e) {
            fail(e.getMessage());
        }

        db.close();
    }

    @Test
    public void getAllSanitationRequests() throws SQLException, ClassNotFoundException {
        Database.setTesting ("getAllSanitationRequests");
        Database db = Database.getInstance ();
        RequestDB rDB = new RequestDB(db);
        String id1 = rDB.requestSanitation(Stream.of(new String[]{ "loc" }), "assigned1", "det");
        String id2 = rDB.requestSanitation(Stream.of(new String[]{ "loc" }), "assigned2", "det");
        String id3 = rDB.requestSanitation(Stream.of(new String[]{ "loc" }), "assigned3", "det");

        /* get all, convert to stream of the ID's, put into list */
        Stream<SanitationRequest> requestStream = rDB.getAllSanitationRequests();
        Stream<String> idStream = requestStream.map(r -> r.getID());
        List<String> idListToCheck = idStream.collect(Collectors.toList());

        /* list must be length 3 */
        assertEquals(3, idListToCheck.size());

        /* check if ids are the same */
        assertTrue(idListToCheck.contains(id1));
        assertTrue(idListToCheck.contains(id2));
        assertTrue(idListToCheck.contains(id3));

        db.close();
    }

    @Test
    public void getAllMedicineRequests() throws SQLException, ClassNotFoundException {
        Database.setTesting ("getAllMedicineRequests");
        Database db = Database.getInstance ();
        RequestDB rDB = new RequestDB(db);
        String[] loc = { "loc" };
        String id1 = rDB.requestMedicine("t", "a", Stream.of(loc), "assigned1");
        String id2 = rDB.requestMedicine("t", "a", Stream.of(loc), "assigned2");
        String id3 = rDB.requestMedicine("t", "a", Stream.of(loc), "assigned3");

        /* get all, convert to stream of the ID's, put into list */
        Stream<MedicineRequest> requestStream = rDB.getAllMedicineRequests();
        Stream<String> idStream = requestStream.map(MedicineRequest::getID);
        List<String> idListToCheck = idStream.collect(Collectors.toList());

        /* list must be length 3 */
        assertEquals(3, idListToCheck.size());

        /* check if ids are the same */
        assertTrue(idListToCheck.contains(id1));
        assertTrue(idListToCheck.contains(id2));
        assertTrue(idListToCheck.contains(id3));

        db.close();
    }

    @Test
    public void getSanitationRequest() throws Exception {
        String[] locationID = { "l" };
        String assigned = "a";
        String details = "d";

        Database.setTesting ("getSanitationRequest");
        Database db = Database.getInstance ();
        RequestDB rDB = new RequestDB(db);

        assertThrows(SQLException.class, () -> rDB.getSanitationRequest("idontexist"));

        String id = rDB.requestSanitation(Stream.of(locationID), assigned, details);

        try {
            SanitationRequest sanReqToCheck = rDB.getSanitationRequest(id);
            assertEquals(locationID [0], sanReqToCheck.getLocations().findFirst().orElseThrow(Exception::new));
            assertEquals(assigned, sanReqToCheck.getAssigned());
            assertEquals(id, sanReqToCheck.getID());
        } catch (SQLException e) {
            fail(e.getMessage());
        }

        db.close();
    }

    @Test
    public void getMedicineRequest() throws Exception {
        String assigned = "as";
        String[] location = { "l" };
        String amount = "a";
        String type = "t";

        Database.setTesting ("getMedicineRequest");
        Database db = Database.getInstance ();
        RequestDB rDB = new RequestDB(db);

        assertThrows(SQLException.class, () -> rDB.getMedicineRequest("idontexist"));

        String id = rDB.requestMedicine(type, amount, Stream.of(location), assigned);

        try {
            MedicineRequest medReqToCheck = rDB.getMedicineRequest(id);
            assertEquals(location[0], medReqToCheck.getLocations().findFirst().orElseThrow(Exception::new));
            assertEquals(assigned, medReqToCheck.getAssigned());
            assertEquals(amount, medReqToCheck.getAmount());
            assertEquals(type, medReqToCheck.getType());
            assertEquals(id, medReqToCheck.getID());
        } catch (SQLException e) {
            fail(e.getMessage());
        }

        db.close();
    }

    @Test
    public void removeSanitationRequest() throws SQLException, ClassNotFoundException {
        Database.setTesting ("removeSanitationRequest");
        Database db = Database.getInstance ();
        RequestDB rDB = new RequestDB(db);
        String[] loc = { "loc" };
        /* store request */
        String sanID = rDB.requestSanitation(Stream.of(loc), "assigned", "details");

        /* make sure it was stored */
        assertTrue(rDB.sanitationRequestExists(sanID));

        /* remove it */
        rDB.removeSanitationRequest(sanID);

        /* make sure its gone */
        assertThrows(SQLException.class, () -> rDB.getSanitationRequest(sanID));

        db.close();
    }

    @Test
    public void removeMedicineRequest() throws SQLException, ClassNotFoundException {
        Database.setTesting ("removeMedicineRequest");
        Database db = Database.getInstance ();
        RequestDB rDB = new RequestDB(db);
        String[] loc = { "loc" };

        /* store request */
        String medID = rDB.requestMedicine("type", "amount", Stream.of(loc), "assigned");

        /* make sure it was stored */
        assertTrue(rDB.medicineRequestExists(medID));

        /* remove it */
        rDB.removeMedicineRequest(medID);

        /* make sure its gone */
        assertThrows(SQLException.class, () -> rDB.getMedicineRequest(medID));

        db.close();
    }

    @Test
    public void setSanitationCompleted() throws SQLException, ClassNotFoundException {
        Database.setTesting ("setSanitationCompleted");
        Database db = Database.getInstance ();
        RequestDB rDB = new RequestDB(db);
        String[] loc = { "loc" };

        /* store request */
        String sanID = rDB.requestSanitation(Stream.of(loc), "assigned", "details");

        /* make sure it starts false */
        assertFalse(rDB.getSanitationRequest(sanID).isComplete());

        /* set it to true */
        rDB.setSanitationCompleted(sanID);

        /* check that it's been set */
        assertTrue(rDB.getSanitationRequest(sanID).isComplete());

        db.close();
    }

    @Test
    public void setMedicineCompleted() throws SQLException, ClassNotFoundException {
        Database.setTesting ("setMedicineCompleted");
        Database db = Database.getInstance ();
        RequestDB rDB = new RequestDB(db);

        /* store request */
        String medID = rDB.requestMedicine("type", "amount", Stream.of(new String[]{ "locationID" }), "assigned");

        /* make sure it starts false */
        assertFalse(rDB.getMedicineRequest(medID).isComplete());

        /* set it to true */
        rDB.setMedicineCompleted(medID);

        /* check that it's been set */
        assertTrue(rDB.getMedicineRequest(medID).isComplete());

        db.close();
    }

    @Test
    public void sanitationRequestExists() throws SQLException, ClassNotFoundException {
        Database.setTesting ("sanitationRequestExists");
        Database db = Database.getInstance ();
        RequestDB rDB = new RequestDB(db);
        String[] loc = { "loc" };

        /* store request */
        String sanID = rDB.requestSanitation(Stream.of(loc), "assigned", "details");

        /* check existence */
        assertTrue(rDB.sanitationRequestExists(sanID));

        /* remove it */
        rDB.removeSanitationRequest(sanID);

        /* check that it doesnt exist */
        assertFalse(rDB.sanitationRequestExists(sanID));

        db.close();
    }

    @Test
    public void medicineRequestExists() throws SQLException, ClassNotFoundException {
        Database.setTesting ("medicineRequestExists");
        Database db = Database.getInstance ();
        RequestDB rDB = new RequestDB(db);
        String[] loc = { "loc" };

        /* store request */
        String medID = rDB.requestMedicine("type", "amount", Stream.of(loc), "assigned");

        /* check existence */
        assertTrue(rDB.medicineRequestExists(medID));

        /* remove it */
        rDB.removeMedicineRequest(medID);

        /* check that it doesnt exist */
        assertFalse(rDB.medicineRequestExists(medID));

        db.close();
    }
}
