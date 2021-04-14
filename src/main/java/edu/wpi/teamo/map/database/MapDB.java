package edu.wpi.teamo.map.database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.stream.Stream;

public class MapDB implements IMapService {
    Database db;
    HashMap<String, Node> nodes;
    HashMap<String, Edge> edges;

    public MapDB(String nodeCSVFilepath, String edgeCSVFilepath) throws FileNotFoundException, SQLException, ClassNotFoundException {

        /* read csv files into hashmaps */
        nodes = NodeCSV.read(nodeCSVFilepath);
        edges = EdgeCSV.read(edgeCSVFilepath);

        /* initialize database */
        db = new Database();
        Node.initTable(db);
        Edge.initTable(db);

        /* save to the database */
        storeNodes(db, nodes);
        storeEdges(db, edges);

    }

    public MapDB(String databaseName) throws SQLException, ClassNotFoundException {
        /* derive init command from custom name */
        String uri = String.format("jdbc:derby:memory:%s;create=true", databaseName);
        nodes = new HashMap<>();
        edges = new HashMap<>();
        db = new Database(uri);
        Node.initTable(db);
        Edge.initTable(db);
    }

    public MapDB() throws SQLException, ClassNotFoundException {
        nodes = new HashMap<>();
        edges = new HashMap<>();
        db = new Database();
        Node.initTable(db);
        Edge.initTable(db);
    }

    public void loadEdgesFromFile(String filepath) throws FileNotFoundException, SQLException {
        /* read file into hashmap */
        edges = EdgeCSV.read(filepath);
        /* save to the database */
        storeEdges(db, edges);
    }

    public void loadNodesFromFile(String filepath) throws FileNotFoundException, SQLException {
        /* read file into hashmap */
        nodes = NodeCSV.read(filepath);
        /* save to the database */
        storeNodes(db, nodes);
    }

    private static void storeNodes(Database db, HashMap<String, Node> nodes) throws SQLException {
        for (String key : nodes.keySet()) {
            Node node = nodes.get(key);
            node.update(db);
        }
    }

    private void storeEdges(Database db, HashMap<String, Edge> edges) throws SQLException {
        for (String key : edges.keySet()) {
            Edge edge = edges.get(key);
            edge.update(db);
        }
    }

    /**
     * Check if a specified node exists in the database
     * @param id Id of the node in question
     * @return True if node exists
     */
    public boolean nodeExists(String id) {
        try {
            Node.getByID(db, id);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean setNodePosition(String id, int newX, int newY) throws SQLException {
        boolean wasUpdated = true;
        if (nodeExists(id)) {
            /* get node object from hashtable (we could get it from database but in this case its faster) */
            Node node = nodes.get(id);

            /* update node */
            node.setXPos(newX);
            node.setYPos(newY);

            /* update database */
            node.update(db);
        }
        else { //if node doesnt exist
            wasUpdated = false;
        }
        return wasUpdated;
    }

    public void setNodeLongName(String id, String newName) throws SQLException {
        if (nodeExists(id)) {

            /* get node object from hashtable (we could get it from database but in this case its faster) */
            Node node = nodes.get(id);

            /* update node */
            node.setLongName(newName);

            /* update database */
            node.update(db);

        }
    }

    @Override
    public Stream<NodeInfo> getAllNodes() {
        return nodes.values().stream().map((Node n) -> (NodeInfo) n);
    }

    @Override
    public Stream<EdgeInfo> getAllEdges() {
        return edges.values().stream().map((Edge e) -> (EdgeInfo) e);
    }

    public NodeInfo getNode(String id) {
        NodeInfo nodeInfo = nodes.get(id);
        return nodeInfo;
    }

    public EdgeInfo getEdge(String id) {
        EdgeInfo edgeInfo = edges.get(id);
        return edgeInfo;
    }

    public void addNode(String nodeID, int xPos, int yPos, String floor,
                        String building, String nodeType, String longName, String shortName) throws SQLException {

        Node n = new Node (nodeID, xPos, yPos, floor, building, nodeType, longName, shortName);
        n.update(db);
        nodes.put(nodeID, n);
    }

    public void addEdge(String edgeID, String startNodeID, String endNodeID) throws SQLException {
        Edge e = new Edge(edgeID, startNodeID, endNodeID);
        e.update(db);
        edges.put(edgeID, e);
    }

    public void deleteNode(String id) throws SQLException {
        nodes.get(id).delete(db);
        nodes.remove(id);
    }

    public void deleteEdge(String id) throws SQLException {
        edges.get(id).delete(db);
        edges.remove(id);
    }

    public void writeEdgesToCSV(String filepath) throws SQLException, IOException {
        Stream<Edge> edgeStream = Edge.getAll(db);
        EdgeCSV.write(filepath, edgeStream);
    }

    public void writeNodesToCSV(String filepath) throws SQLException, IOException {
        Stream<Node> nodeStream = Node.getAll(db);
        NodeCSV.write(filepath, nodeStream);
    }

}
