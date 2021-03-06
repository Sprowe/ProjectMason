package edu.wpi.teamo.database.map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NodeCSV {

    public static Stream<Node> read(InputStreamReader reader) throws IOException {
        List<Node> nodes = new ArrayList<>();

        CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
        CSVParser parser = new CSVParser(reader, format);
        for (CSVRecord record : parser) {
            Node node = new Node(
                    record.get("nodeID"),
                    Integer.parseInt(record.get("xcoord")),
                    Integer.parseInt(record.get("ycoord")),
                    record.get("floor"),
                    record.get("building"),
                    record.get("nodeType"),
                    record.get("longName"),
                    record.get("shortName")
            );
            nodes.add(node);
        }
        parser.close();

        return nodes.stream();
    }

    public static void write(String filepath, Stream<Node> nodeStream) throws IOException {
        FileWriter csvWriter = new FileWriter(filepath);
        csvWriter.append("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName\n");
        for (Node node : nodeStream.collect(Collectors.toList())) {
            csvWriter.append(
                    String.format("%s,%d,%d,%s,%s,%s,%s,%s\n",
                            node.getNodeID(),
                            node.getXPos(),
                            node.getYPos(),
                            node.getFloor(),
                            node.getBuilding(),
                            node.getNodeType(),
                            node.getLongName(),
                            node.getShortName()
            ));
        }
        csvWriter.flush();
        csvWriter.close();
    }

}
