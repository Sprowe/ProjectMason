package edu.wpi.teamo.database.map;

import edu.wpi.teamo.database.Database;

import java.io.*;
import java.net.URL;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.sql.SQLException;

import javafx.util.Pair;
import java.util.List;

public class MapDB implements IMapService {
    Database db;

//    public MapDB(String nodeCSVFilepath, String edgeCSVFilepath) throws FileNotFoundException, SQLException, ClassNotFoundException {
//
//        /* read csv files */
//        Stream<Node> nodeStream = NodeCSV.read(nodeCSVFilepath);
//        Stream<Edge> edgeStream = EdgeCSV.read(edgeCSVFilepath);
//
//        /* initialize database */
//        db = Database.getInstance();
//        Node.initTable(db);
//        Edge.initTable(db);
//
//        /* save to the database */
//        storeNodes(db, nodeStream);
//        storeEdges(db, edgeStream);
//
//    }

    public MapDB(String databaseName) throws SQLException, ClassNotFoundException {
        /* derive init command from custom name */
        Database.setTesting(databaseName);
        db = Database.getInstance();
        Node.initTable(db);
        Edge.initTable(db);
    }

    public MapDB(Database database) throws SQLException {
        this.db = database;
        Node.initTable(db);
        Edge.initTable(db);
        loadMapIfEmpty(db);
    }

    public MapDB() throws SQLException, ClassNotFoundException {
        db = Database.getInstance();
        Node.initTable(db);
        Edge.initTable(db);
        loadMapIfEmpty(db);
    }

    private static File[] getResourceFolderFiles (String folder) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(folder);
        String path = url.getPath();
        return new File(path).listFiles();
    }

    private void loadMapIfEmpty(Database db) {
        try {
            if (Node.getAll(db).count() == 0) {
                System.out.println("Loading nodes from default file.");
                loadNodesFromResource("bwOnodes.csv");
            }
            if (Edge.getAll(db).count() == 0) {
                System.out.println("Loading edges from default file.");
                loadEdgesFromResource("bwOedges.csv");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadMapFromCSV(String filepath) throws IOException, SQLException {
        deleteMap();
        Pair<Stream<Node>, Stream<Edge>> mapStreamPair = MapCSV.readMapFile(filepath);
        storeEdges(db, mapStreamPair.getValue());
        storeNodes(db, mapStreamPair.getKey());
    }

    public void loadEdgesFromFile(String filepath) throws IOException, SQLException {
        deleteEdges();
        /* read file */
        File file = new File(filepath);
        Stream<Edge> edgeStream = EdgeCSV.read(new FileReader(file));
        /* save to the database */
        storeEdges(db, edgeStream);
    }

    public void loadNodesFromFile(String filepath) throws IOException, SQLException {
        deleteNodes();
        /* read file */
        File file = new File(filepath);
        Stream<Node> nodeStream = NodeCSV.read(new FileReader(file));
        /* save to the database */
        storeNodes(db, nodeStream);
    }

    public void loadEdgesFromResource(String filepath) throws IOException, SQLException {
        deleteEdges();
        /* read file */
        InputStream resourceIStream = getClass().getResourceAsStream(filepath);
        if (resourceIStream != null) {
            InputStreamReader iReader = new InputStreamReader(resourceIStream);
            Stream<Edge> edgeStream = EdgeCSV.read(iReader);
            /* save to the database */
            storeEdges(db, edgeStream);
        }
        else {
            throw new IOException("Cant get resource");
        }
    }

    public void loadNodesFromResource(String filepath) throws IOException, SQLException {
        deleteNodes();
        /* read file */
        InputStream resourceIStream = getClass().getResourceAsStream(filepath);
        if (resourceIStream != null) {
            InputStreamReader iReader = new InputStreamReader(resourceIStream);
            Stream<Node> nodeStream = NodeCSV.read(iReader);
            /* save to the database */
            storeNodes(db, nodeStream);
        }
        else {
            throw new IOException("Cant get resource");
        }
    }

    private static void storeNodes(Database db, Stream<Node> nodeStream) throws SQLException {
        for (Node node : nodeStream.collect(Collectors.toList())) {
            node.update(db);
        }
    }

    private void storeEdges(Database db, Stream<Edge> edgeStream) throws SQLException {
        for (Edge edge : edgeStream.collect(Collectors.toList())) {
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

    /**
     * Check if a specified edge exists in the database
     * @param id Id of the edge in question
     * @return True if edge exists
     */
    public boolean edgeExists(String id) {
        try {
            Edge.getByID(db, id);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public void setNodePosition(String id, int newX, int newY) throws SQLException {
        /* get node object from database */
        Node node = Node.getByID(db, id);

        /* update node */
        node.setXPos(newX);
        node.setYPos(newY);

        /* update database */
        node.update(db);
    }

    @Override
    public void setNodeShortName(String id, String shortName) throws SQLException {
        /* get node object from database */
        Node node = Node.getByID(db, id);
        /* update node */
        node.setShortName(shortName);
        /* update database */
        node.update(db);
    }

    @Override
    public void setNodeBuilding(String id, String building) throws SQLException {
        /* get node object from database */
        Node node = Node.getByID(db, id);
        /* update node */
        node.setBuilding(building);
        /* update database */
        node.update(db);
    }

    @Override
    public void setNodeFloor(String id, String floor) throws SQLException {
        /* get node object from database */
        Node node = Node.getByID(db, id);
        /* update node */
        node.setFloor(floor);
        /* update database */
        node.update(db);
    }

    @Override
    public void setNodeID(String oldID, String newID) throws SQLException {
        /* get node object from database */
        Node node = Node.getByID(db, oldID);

        /* delete from database */
        node.delete(db);

        /* update node */
        node.setNodeID(newID);

        /* update database */
        node.update(db);
    }

    public void setNodeLongName(String id, String newName) throws SQLException {
        /* get node object from database */
        Node node = Node.getByID(db, id);

        /* update node */
        node.setLongName(newName);

        /* update database */
        node.update(db);
    }

    @Override
    public void setNodeType(String id, String type) throws SQLException {
        /* get node object from database */
        Node node = Node.getByID(db, id);
        /* update node */
        node.setNodeType(type);
        /* update database */
        node.update(db);
    }

    @Override
    public void setNodeValid(String id, boolean valid) throws SQLException {
        Node node = Node.getByID(db, id);
        node.setValid(valid);
        node.update(db);
    }

    public void setEdgeID(String oldID, String newID) throws SQLException {
        /* get edge object from database */
        Edge edge = Edge.getByID(db, oldID);

        /* delete from database */
        edge.delete(db);

        /* update node */
        edge.setEdgeID(newID);

        /* update database */
        edge.update(db);
    }

    @Override
    public void setEdgeValid(String id, boolean valid) throws SQLException {
        Edge edge = Edge.getByID(db, id);
        edge.setValid(valid);
        edge.update(db);
    }

    @Override
    public void setEdgeStartID(String edgeID, String startNodeID) throws SQLException {
        /* get edge object from hashtable (we could get it from database but in this case its faster) */
        Edge edge = Edge.getByID(db, edgeID);

        /* update node */
        edge.setStartID(startNodeID);

        /* update database */
        edge.update(db);
    }

    @Override
    public void setEdgeEndID(String edgeID, String endNodeID) throws SQLException {
        /* get edge object from hashtable (we could get it from database but in this case its faster) */
        Edge edge = Edge.getByID(db, edgeID);

        /* update node */
        edge.setEndID(endNodeID);

        /* update database */
        edge.update(db);
    }

    @Override
    public Stream<NodeInfo> getAllNodes() throws SQLException {
        return Node.getAll(db).map((Node n) -> (NodeInfo) n);
    }

    @Override
    public Stream<EdgeInfo> getAllEdges() throws SQLException {
        return Edge.getAll(db).map((Edge e) -> (EdgeInfo) e);
    }

    public NodeInfo getNode(String id) throws SQLException {
        NodeInfo nodeInfo = Node.getByID(db, id);
        return nodeInfo;
    }

    public EdgeInfo getEdge(String id) throws SQLException {
        EdgeInfo edgeInfo = Edge.getByID(db, id);
        return edgeInfo;
    }

    public void addNode(String nodeID, int xPos, int yPos, String floor,
                        String building, String nodeType, String longName, String shortName) throws SQLException {
        if (nodeExists(nodeID)) throw new SQLException("A node with that ID already exists.");
        Node n = new Node (nodeID, xPos, yPos, floor, building, nodeType, longName, shortName);
        n.update(db);
    }

    public void addEdge(String edgeID, String startNodeID, String endNodeID) throws SQLException {
        if (edgeExists(edgeID)) throw new SQLException("An edge with that ID already exists.");
        Edge e = new Edge(edgeID, startNodeID, endNodeID);
        e.update(db);
    }

    public void deleteNode(String id) throws SQLException {
        Node.getByID(db, id).delete(db);
    }

    public void deleteEdge(String id) throws SQLException {
        Edge.getByID(db, id).delete(db);
    }

    @Override
    public void deleteMap() throws SQLException {
        deleteEdges();
        deleteNodes();
    }

    private void deleteEdges() throws SQLException {
        List<Edge> edges = Edge.getAll(db).collect(Collectors.toList());
        for (Edge edge : edges) { edge.delete(db); }
    }

    private void deleteNodes() throws SQLException {
        List<Node> nodes = Node.getAll(db).collect(Collectors.toList());
        for (Node node : nodes) { node.delete(db); }
    }

    public void writeEdgesToCSV(String filepath) throws SQLException, IOException {
        Stream<Edge> edgeStream = Edge.getAll(db);
        EdgeCSV.write(filepath, edgeStream);
    }

    public void writeNodesToCSV(String filepath) throws SQLException, IOException {
        Stream<Node> nodeStream = Node.getAll(db);
        NodeCSV.write(filepath, nodeStream);
    }

    @Override
    public void writeMapToCSV(String filepath) throws SQLException, IOException {
        Stream<Node> nodeStream = Node.getAll(db);
        Stream<Edge> edgeStream = Edge.getAll(db);
        MapCSV.writeMapFile(filepath, nodeStream, edgeStream);
    }

    @Override
    public void closeConnection() throws SQLException {
        db.close();
    }
}
