package edu.wpi.teamo.map.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Edge implements EdgeInfo {

    String startNodeID;
    String endNodeID;
    String edgeID;

    public Edge(String edgeID, String startNodeID,  String endNodeID) {
        this.startNodeID = startNodeID;
        this.endNodeID = endNodeID;
        this.edgeID = edgeID;
    }

    /**
     * Create an Edge table in a database.
     * @param db The database to create the table in.
     * @return True if the table was created successfully.
     */
    public static boolean initTable(Database db) {
        boolean initialized = true;
        try {
            db.processUpdate("CREATE TABLE Edge ("
                    + "edgeID varchar(255) primary key, "
                    + "startNode varchar(255), "
                    + "endNode varchar(255))"
            );
        } catch (SQLException e) {
            System.out.println("ERROR - COULD NOT INIT NODE TABLE");
            e.printStackTrace();
            initialized = false;
        }
        return initialized;
    }

    /**
     * Create an Edge object from an existing edge in a database.
     * @param db The database object to fetch the edge from.
     * @param id The id of the edge to be fetched.
     * @return The edge object.
     * @throws SQLException When the id is incorrect, or database is not properly initialized.
     */
    public static Edge getByID(Database db, String id) throws SQLException {
        ResultSet rs = db.processQuery("SELECT * FROM Edge WHERE Edge.edgeID = '" + id + "'");
        if (!rs.next())
            throw new SQLException();
        return new Edge(rs.getString("edgeID"),
                rs.getString("startNode"),
                rs.getString("endNode"));
    }

    /**
     * Retrieve all edges from a database.
     * @param db The database to retrieve the edges from.
     * @return A stream of Edge objects.
     * @throws SQLException When the database is not properly initialized.
     */
    public static Stream<Edge> getAll(Database db) throws SQLException {
        ResultSet rs = db.processQuery("SELECT * FROM Edge");
        ArrayList<Edge> edges = new ArrayList<>();
        while (rs.next())
            edges.add(new Edge(rs.getString("edgeID"),
                    rs.getString("startNode"),
                    rs.getString("startNode")));
        return edges.stream();
    }

    /**
     * Update a database with the information stored in the Edge object.
     * @param db The database to update.
     * @throws SQLException When the database is not properly initialized,
     * or the edge object's attributes are invalid.
     */
    public void update(Database db) throws SQLException {
        // Apache Derby does not have upsert, so we must try both an insert and update.
        try {
            db.processUpdate("INSERT INTO Edge (edgeID, startNode, endNode) VALUES " +
                    String.format("('%s', '%s', '%s')",
                            this.edgeID,
                            this.startNodeID,
                            this.endNodeID));
        } catch (SQLException e) {
            // Item with this ID already exists in the DB, try insert.
            db.processUpdate("UPDATE Edge SET " +
                    String.format("edgeID = '%s', startNode = '%s', endNode = '%s' ",
                            this.edgeID,
                            this.startNodeID,
                            this.endNodeID)  +
                    "WHERE Edge.edgeID = '" + this.edgeID + "'");

        }
    }

    @Override
    public String getStartNodeID() {
        return startNodeID;
    }

    @Override
    public String getEndNodeID() {
        return endNodeID;
    }

    @Override
    public String getEdgeID() {
        return edgeID;
    }
}