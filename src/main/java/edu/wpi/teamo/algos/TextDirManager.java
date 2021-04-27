package edu.wpi.teamo.algos;
import javafx.util.*;
import java.util.*;

public class TextDirManager {

    /**
     * Provides a string that entails textual directions for a given path of nodes
     * @param pathToParse path of nodes to dictate a path for
     * @return the directions string
     */
    public static List<String> getTextualDirections(List<AlgoNode> pathToParse) {
        List<String> directions = new LinkedList<>();
        Pair<Integer, Double> lastQuadrant = null;
        for(int i = 0; i < pathToParse.size(); i++) {
            //If user has arrived to destination
            if(i == pathToParse.size() - 1) {
                directions.add("You have arrived at your destination.");
                continue;
            }
            //If still travelling (there is a next node)
            //If we have just started travelling (no initial facing direction)
            if(lastQuadrant == null) {
                directions.add("Proceed towards " + pathToParse.get(i + 1).getLongName());
                //Find next quadrant
                lastQuadrant = getQuadrant(pathToParse.get(i), pathToParse.get(i + 1));
                continue;
            }
            Pair<Integer,Double> currentQuadrant = getQuadrant(pathToParse.get(i), pathToParse.get(i + 1));

            //Determine direction based on quadrant and angle
            switch(lastQuadrant.getKey()){
                case 0:{
                    directions.add("Proceed towards " + pathToParse.get(i + 1).getLongName());
                    break;
                }
                case 1:{
                    if(currentQuadrant.getKey() == 4 || currentQuadrant.getKey() == 5 || (lastQuadrant.getValue() - currentQuadrant.getValue() > 0 && currentQuadrant.getKey() == 1))
                        directions.add("Proceed leftwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 2 || currentQuadrant.getKey() == 6 || (lastQuadrant.getValue() - currentQuadrant.getValue() < 0 && currentQuadrant.getKey() == 1))
                        directions.add("Proceed rightwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 3)
                        directions.add("Proceed backwards to " + pathToParse.get(i + 1).getLongName());
                    else directions.add("Proceed forward to " + pathToParse.get(i + 1).getLongName());
                    break;
                }
                case 2:{
                    if(currentQuadrant.getKey() == 1 || currentQuadrant.getKey() == 6 || (lastQuadrant.getValue() - currentQuadrant.getValue() < 0 && currentQuadrant.getKey() == 2))
                        directions.add("Proceed leftwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 3 || currentQuadrant.getKey() == 7 || (lastQuadrant.getValue() - currentQuadrant.getValue() > 0 && currentQuadrant.getKey() == 2))
                        directions.add("Proceed rightwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 4)
                        directions.add("Proceed backwards to " + pathToParse.get(i + 1).getLongName());
                    else directions.add("Proceed forward to " + pathToParse.get(i + 1).getLongName());
                    break;
                }
                case 3:{
                    if(currentQuadrant.getKey() == 2 || currentQuadrant.getKey() == 7 || (lastQuadrant.getValue() - currentQuadrant.getValue() > 0 && currentQuadrant.getKey() == 3))
                        directions.add("Proceed leftwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 4 || currentQuadrant.getKey() == 8 || (lastQuadrant.getValue() - currentQuadrant.getValue() < 0 && currentQuadrant.getKey() == 3))
                        directions.add("Proceed rightwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 1)
                        directions.add("Proceed backwards to " + pathToParse.get(i + 1).getLongName());
                    else directions.add("Proceed forward to " + pathToParse.get(i + 1).getLongName());
                    break;
                }
                case 4:{
                    if(currentQuadrant.getKey() == 3 || currentQuadrant.getKey() == 8 || (lastQuadrant.getValue() - currentQuadrant.getValue() < 0 && currentQuadrant.getKey() == 4))
                        directions.add("Proceed leftwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 1 || currentQuadrant.getKey() == 5 || (lastQuadrant.getValue() - currentQuadrant.getValue() > 0 && currentQuadrant.getKey() == 4))
                        directions.add("Proceed rightwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 2)
                        directions.add("Proceed backwards to " + pathToParse.get(i + 1).getLongName());
                    else directions.add("Proceed forward to " + pathToParse.get(i + 1).getLongName());
                    break;
                }
                case 5:{
                    if(currentQuadrant.getKey() == 4 || currentQuadrant.getKey() == 8)
                        directions.add("Proceed leftwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 1 || currentQuadrant.getKey() == 6)
                        directions.add("Proceed rightwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 2 || currentQuadrant.getKey() == 3 || currentQuadrant.getKey() == 7)
                        directions.add("Proceed backwards to " + pathToParse.get(i + 1).getLongName());
                    else directions.add("Proceed forward to " + pathToParse.get(i + 1).getLongName());
                    break;
                }
                case 6:{
                    if(currentQuadrant.getKey() == 1 || currentQuadrant.getKey() == 5)
                        directions.add("Proceed leftwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 2 || currentQuadrant.getKey() == 7)
                        directions.add("Proceed rightwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 3 || currentQuadrant.getKey() == 4 || currentQuadrant.getKey() == 8)
                        directions.add("Proceed backwards to " + pathToParse.get(i + 1).getLongName());
                    else directions.add("Proceed forward to " + pathToParse.get(i + 1).getLongName());
                    break;
                }
                case 7:{
                    if(currentQuadrant.getKey() == 2 || currentQuadrant.getKey() == 6)
                        directions.add("Proceed leftwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 3 || currentQuadrant.getKey() == 8)
                        directions.add("Proceed rightwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 1 || currentQuadrant.getKey() == 5 || currentQuadrant.getKey() == 4)
                        directions.add("Proceed backwards to " + pathToParse.get(i + 1).getLongName());
                    else directions.add("Proceed forward to " + pathToParse.get(i + 1).getLongName());
                    break;
                }
                case 8:{
                    if(currentQuadrant.getKey() == 3 || currentQuadrant.getKey() == 7)
                        directions.add("Proceed leftwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 4 || currentQuadrant.getKey() == 5)
                        directions.add("Proceed rightwards to " + pathToParse.get(i + 1).getLongName());
                    else if(currentQuadrant.getKey() == 1 || currentQuadrant.getKey() == 2 || currentQuadrant.getKey() == 6)
                        directions.add("Proceed backwards to " + pathToParse.get(i + 1).getLongName());
                    else directions.add("Proceed forward to " + pathToParse.get(i + 1).getLongName());
                    break;
                }
            }
            lastQuadrant = currentQuadrant;
        }
        return directions;
    }

    /**
     * Returns the quadrant the line segment is angled at
     * Quadrant Enumeration: 1: bottom right, 2: bottom left, 3: top left, 4: top right
     * Boundary Enumeration: 5: East, 6: South, 7: West, 8: North, 0: same point
     * @param current first node
     * @param next next 2nd node
     * @return a pair consisting of the quadrant enumeration and angle in radians
     */
    public static Pair<Integer, Double> getQuadrant(AlgoNode current, AlgoNode next) {

        double angle = Math.tan((double) (next.getY() - current.getY()) / (double) (next.getX() - current.getX()));
        //Quadrants 1 or 3
        if(angle > 0) {
            if(next.getY() - current.getY() > 0 || next.getX() - current.getX() > 0) return new Pair<>(1,angle);
            else return new Pair<>(3,angle);
        }
        //Quadrants 2 or 4
        else if(angle < 0) {
            if(next.getY() - current.getY() > 0 || next.getX() - current.getX() < 0) return new Pair<>(2,angle);
            else return new Pair<>(4,angle);
        }
        //A boundary (angle default to 0)
        else {
            if(next.getY() - current.getY() > 0) return new Pair<>(6,0.0);
            else if(next.getY() - current.getY() < 0) return new Pair<>(8,0.0);
            else if(next.getX() - current.getX() > 0) return new Pair<>(5,0.0);
            else if(next.getX() - current.getX() < 0) return new Pair<>(7,0.0);
            else return new Pair<>(0,0.0);
        }
    }

}
