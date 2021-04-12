package edu.wpi.teamo.map.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Node implements NodeInfo {

    private int xPos;
    private int yPos;
    private String nodeID;
    private String floor;
    private String building;
    private String nodeType;
    private String longName;
    private String shortName;

    public Node(String nodeID, int xPos, int yPos, String floor,
                String building, String nodeType, String longName, String shortName ){
        this.shortName = shortName;
        this.nodeType = nodeType;
        this.building = building;
        this.longName = longName;
        this.nodeID = nodeID;
        this.floor = floor;
        this.yPos = yPos;
        this.xPos = xPos;
    }

    /**
     * Create an Node table in a database.
     * @param db The database to create the table in.
     * @return True if the table was created successfully.
     */
    public static boolean initTable(Database db) {
        boolean initialized = true;
        try {
            db.processUpdate("CREATE TABLE Node ("
                    + "nodeID VARCHAR(255) PRIMARY KEY, "
                    + "xcoord INT, "
                    + "ycoord INT, "
                    + "floor VARCHAR(255), "
                    + "building VARCHAR(255), "
                    + "nodeType VARCHAR(255), "
                    + "longName VARCHAR(255), "
                    + "shortName VARCHAR(255))"
            );
        } catch (SQLException e) {
            System.out.println("ERROR - COULD NOT INIT NODE TABLE");
            e.printStackTrace();
            initialized = false;
        }
        return initialized;
    }

    /**
     * Create an Node object from an existing edge in a database.
     * @param db The database object to fetch the node from.
     * @param id The id of the node to be fetched.
     * @return The node object.
     * @throws SQLException When the id is incorrect, or database is not properly initialized.
     */
    public static Node getByID (Database db, String id) throws SQLException {
        ResultSet rs = db.processQuery("SELECT * FROM Node WHERE Node.nodeID = '" + id + "'");
        if (!rs.next())
            throw new SQLException();

        return new Node(
            rs.getString("nodeID"),
            rs.getInt("xcoord"),
            rs.getInt("ycoord"),
            rs.getString("floor"),
            rs.getString("building"),
            rs.getString("nodeType"),
            rs.getString("longName"),
            rs.getString("shortName")
        );
    }

    /**
     * Retrieve all nodes from a database.
     * @param db The database to retrieve the nodes from.
     * @return A stream of Node objects.
     * @throws SQLException When the database is not properly initialized.
     */
    public static Stream<Node> getAll(Database db) throws SQLException {
        ArrayList<Node> nodes = new ArrayList<>();
        ResultSet rs = db.processQuery("SELECT * FROM Node");
        while (rs.next())
            nodes.add(new Node(
                rs.getString("nodeID"),
                rs.getInt("xcoord"),
                rs.getInt("ycoord"),
                rs.getString("floor"),
                rs.getString("building"),
                rs.getString("nodeType"),
                rs.getString("longName"),
                rs.getString("shortName")
            ));
        return nodes.stream();
    }

    /**
     * Update a database with the information stored in the Node object.
     * @param db The database to update.
     * @throws SQLException When the database is not properly initialized,
     * or the node object's attributes are invalid.
     */
    public void update(Database db) throws SQLException {
        // Apache Derby does not have upsert, so we must try both an insert and update.
        try {
            db.processUpdate("INSERT INTO Node (nodeID, xcoord, ycoord, floor, building, nodeType, " +
                    "longName, shortName) VALUES " +
                    String.format("('%s',%s,%s,'%s','%s','%s','%s','%s')",
                            this.nodeID,
                            this.xPos,
                            this.yPos,
                            this.floor,
                            this.building,
                            this.nodeType,
                            this.longName,
                            this.shortName));
        } catch (SQLException e) {
            // Item with this ID already exists in the DB, try insert.
            e.printStackTrace();
            db.processUpdate("UPDATE Node SET " +
                    String.format("nodeID = '%s', xcoord = %s, ycoord = %s, floor = '%s', " +
                                    "building = '%s', nodeType = '%s', longName = '%s', " +
                                    "shortName = '%s' ",
                            this.nodeID,
                            this.xPos,
                            this.yPos,
                            this.floor,
                            this.building,
                            this.nodeType,
                            this.longName,
                            this.shortName)
                    + "WHERE Node.nodeID = '" + this.nodeID+ "'");

        }
    }

    //TODO: Fix the update method's error detection so the set function is not needed
    //had to add this because the try catch above was not working
    public void set(Database db) throws SQLException {

        db.processUpdate("UPDATE Node SET " +
                String.format("nodeID = '%s', xcoord = %s, ycoord = %s, floor = '%s', " +
                                "building = '%s', nodeType = '%s', longName = '%s', " +
                                "shortName = '%s' ",
                        this.nodeID,
                        this.xPos,
                        this.yPos,
                        this.floor,
                        this.building,
                        this.nodeType,
                        this.longName,
                        this.shortName)
                + "WHERE Node.nodeID = '" + this.nodeID+ "'");

    }

    /**
     * This function updates x and y coordinate of a node object
     *
     * @param xcoord
     * @param ycoord
     */
    public void changeXY(int xcoord, int ycoord) {
        this.xPos = xcoord;
        this.yPos = ycoord;
    }

    public int getXPos() {
        return xPos;
    }

    public void setXPos(int xPos) {
        this.xPos = xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public void setYPos(int yPos) {
        this.yPos = yPos;
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}