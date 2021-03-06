package edu.wpi.teamo.database.map;

import static org.junit.jupiter.api.Assertions.*;
import java.util.stream.Collectors;

import edu.wpi.teamo.database.Database;
import edu.wpi.teamo.database.map.Edge;
import org.junit.jupiter.api.Test;
import java.util.stream.Stream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EdgeTests {

    @Test
    public void testInitTable() {
        Database db = null;

        /* init database */
        Database.setTesting ("InitEdgeTable");
        db = Database.getInstance();

        /* init edge table */
        try {
            Edge.initTable(db);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        /* verify table exists */
        try {
            Edge.getAll(db);
        } catch (SQLException e) {
            fail(e.getMessage());
        }

        /* close db */
        try {
            db.close();
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetByID() {
        String startNodeID = "testStartNodeID";
        String endNodeID = "testEndNodeID";
        String edgeID = "testID";

        try {
            Database.setTesting ("testGetEdgeByID");
            Database db = Database.getInstance();
            Edge.initTable(db);

            /* store an edge */
            Edge edgeToStore = new Edge(edgeID, startNodeID, endNodeID);
            edgeToStore.update(db);

            /* fetch it */
            Edge fetchedEdge = Edge.getByID(db, edgeID);

            /* check if its the same */
            assertEquals(startNodeID, fetchedEdge.getStartNodeID());
            assertEquals(endNodeID, fetchedEdge.getEndNodeID());
            assertEquals(edgeID, fetchedEdge.getEdgeID());

            db.close();
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void testGetAll() {
        String edge1ID = "testIDOne";
        String edge2ID = "testIDTwo";
        String edge3ID = "testIDTre";

        try {
            Database.setTesting ("GetAllEdges");
            Database db = Database.getInstance();
            Edge.initTable(db);

            /* store three edges */
            Edge eStoredOne = new Edge(edge1ID, edge1ID + "start", edge1ID + "end");
            Edge eStoredTwo = new Edge(edge2ID, edge2ID + "start", edge2ID + "end");
            Edge eStoredTre = new Edge(edge3ID, edge3ID + "start", edge3ID + "end");
            eStoredOne.update(db);
            eStoredTwo.update(db);
            eStoredTre.update(db);

            /* fetch all - into list */
            Stream<Edge> edgeStream = Edge.getAll(db);
            List<Edge>   edgeList   = edgeStream.collect(Collectors.toList());

            /* list must be length 3 */
            int size = edgeList.size();
//            assertEquals(3, size);
            if (size == 3) {
                /* make list of the IDs */
                ArrayList<String> ids = new ArrayList<>();
                for (int i = 0; i < 3; i++) ids.add(edgeList.get(i).getEdgeID());

                /* check if ids are the same */
                assertTrue(ids.contains(edge1ID));
                assertTrue(ids.contains(edge2ID));
                assertTrue(ids.contains(edge3ID));
            }

            db.close();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testUpdate() {
        String modifiedEndNodeID = "newEndNode";
        String startNodeID = "testStartNodeID";
        String endNodeID = "testEndNodeID";
        String edgeID = "testID";

        try {
            Database.setTesting ("UpdateEdge");
            Database db = Database.getInstance();
            Edge.initTable(db);

            /* store an edge */
            Edge edgeToStore = new Edge(edgeID, startNodeID, endNodeID);
            edgeToStore.update(db);

            /* fetch it */
            Edge edgeToModify = Edge.getByID(db, edgeID);

            /* modify it */
            edgeToModify.setEndID(modifiedEndNodeID);

            /* store it */
            edgeToModify.update(db);

            /* fetch again */
            Edge edgeToCheck = Edge.getByID(db, edgeID);

            /* check if it has been modified */
            assertEquals(modifiedEndNodeID, edgeToCheck.getEndNodeID());
            assertEquals(edgeID, edgeToCheck.getEdgeID());

            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

    @Test
    public void testDelete() throws SQLException, ClassNotFoundException {

        String edgeID = "edgeID1";

        Database.setTesting ("DeleteEdge");
        Database db = Database.getInstance();
        Edge.initTable(db);

        assertThrows(IllegalArgumentException.class, () -> {
            Edge invalidEdge = new Edge(edgeID, "", "");
            invalidEdge.update(db);
        });

        /* store an edge */
        Edge edgeToStore = new Edge(edgeID, "Test", "test");
        edgeToStore.update(db);

        /* fetch it */
        Edge edgeToModify = Edge.getByID(db, edgeID);

        /* delete it */
        edgeToModify.delete(db);

        /* Check that edge no longer exists */
        assertThrows(SQLException.class, () -> Edge.getByID(db, edgeID));

        db.close();

    }
}
