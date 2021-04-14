package edu.wpi.teamo.algos;

import edu.wpi.teamo.map.database.Edge;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.LinkedList;

public class AStarTests {
    /**
     * Test for AStar finding an arbitrary path
     */
    @Test
    public void AStarTest1()
    {
        // nodes
        LinkedList<AlgoNode> nodes = new LinkedList<>();
        AlgoNode n1 = new AlgoNode("oPARK00101", 3116,1131,"F1",NodeType.PARK,"Floor1RightParking1","F1RightP1");
        nodes.add(n1);
        AlgoNode n2 = new AlgoNode("oPARK00201", 3116,1155,"F1",NodeType.PARK,"Floor1RightParking2","F1RightP2");
        nodes.add(n2);
        AlgoNode n3 = new AlgoNode("oPARK00301", 3116,1181,"F1",NodeType.PARK,"Floor1RightParking3","F1RightP3");
        nodes.add(n3);
        AlgoNode n4 = new AlgoNode("oPARK00401", 3116,1207,"F1",NodeType.PARK,"Floor1RightParking4","F1RightP4");
        nodes.add(n4);
        AlgoNode n5 = new AlgoNode("oPARK00501", 3116,1233,"F1",NodeType.PARK,"Floor1RightParking5","F1RightP5");
        nodes.add(n5);
        AlgoNode n6 = new AlgoNode("oPARK00601", 3116,1253,"F1",NodeType.PARK,"Floor1RightParking6","F1RightP6");
        nodes.add(n6);
        AlgoNode n7 = new AlgoNode("oPARK00701", 3242,1111,"F1",NodeType.PARK,"Floor1RightParking7","F1RightP7");
        nodes.add(n7);
        AlgoNode n8 = new AlgoNode("oPARK00801", 3242,1133,"F1",NodeType.PARK,"Floor1RightParking8","F1RightP8");
        nodes.add(n8);
        AlgoNode n9 = new AlgoNode("oPARK00901", 3242,1157,"F1",NodeType.PARK,"Floor1RightParking9","F1RightP9");
        nodes.add(n9);
        AlgoNode n10 = new AlgoNode("oPARK01001", 3284,1279,"F1",NodeType.PARK,"Floor1RightParking10","F1RightP10");
        nodes.add(n10);

        // edges
        LinkedList<Edge> edges = new LinkedList<>();
        edges.add(new Edge("oPARK00101_oPARK00301","oPARK00101","oPARK00301"));
        edges.add(new Edge("oPARK00101_oPARK00601","oPARK00101","oPARK00601"));
        edges.add(new Edge("oPARK00101_oPARK00201","oPARK00101","oPARK00201"));
        edges.add(new Edge("oPARK00201_oPARK00701","oPARK00201","oPARK00701"));
        edges.add(new Edge("oPARK00601_oPARK00401","oPARK00601","oPARK00401"));
        edges.add(new Edge("oPARK00601_oPARK00501","oPARK00601","oPARK00501"));
        edges.add(new Edge("oPARK00701_oPARK00401","oPARK00701","oPARK00401"));
        edges.add(new Edge("oPARK00701_oPARK00801","oPARK00701","oPARK00801"));
        edges.add(new Edge("oPARK00801_oPARK00901","oPARK00801","oPARK00901"));
        edges.add(new Edge("oPARK00801_oPARK01001","oPARK00801","oPARK01001"));

        // Test for assignNodeAdjacency logics
        AStarManager.assignNodeAdjacency(nodes, edges);

        AStar a = new AStar(nodes, edges, "","");
        a.setAllTheMess(nodes);

        LinkedList<AlgoNode> pathP10_P5 = a.findPath("oPARK01001","oPARK00501");

        assertEquals(6, pathP10_P5.size());
        assertEquals(n10, pathP10_P5.get(0));
        assertEquals(n8, pathP10_P5.get(1));
        assertEquals(n7, pathP10_P5.get(2));
        assertEquals(n4, pathP10_P5.get(3));
        assertEquals(n6, pathP10_P5.get(4));
        assertEquals(n5, pathP10_P5.get(5));
    }

}
