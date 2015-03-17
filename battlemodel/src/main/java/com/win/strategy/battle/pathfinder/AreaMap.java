package com.win.strategy.battle.pathfinder;

/**
 *
 * @author omelnyk
 */
import com.win.strategy.battle.model.entity.BMField;
import java.util.ArrayList;

public class AreaMap {

    private int mapWith;
    private int mapHeight;
    private ArrayList<ArrayList<Node>> map;
    private int startLocationX = 0;
    private int startLocationY = 0;
    private int goalLocationX = 0;
    private int goalLocationY = 0;
    private BMField field;

    public AreaMap(BMField field) {
        this.field = field;
        this.mapWith = field.getMaxSizeX();
        this.mapHeight = field.getMaxSizeY();

        createMap();
        registerEdges();
    }

    private void createMap() {
        Node node;
        map = new ArrayList<>();
        for (int x = 0; x < mapWith; x++) {
            map.add(new ArrayList<Node>());
            for (int y = 0; y < mapHeight; y++) {
                node = new Node(field.getCell(x, y));
                map.get(x).add(node);
            }
        }
    }

    /**
     * Registers the nodes edges (connections to its neighbors).
     */
    private void registerEdges() {
        for (int x = 0; x < mapWith; x++) {
            for (int y = 0; y < mapHeight; y++) {
                Node node = map.get(x).get(y);
                if ((y > 0)) {
                    node.setNorth(map.get(x).get(y - 1));
                }
                if ((y > 0) && (x < mapWith - 1)) {
                    node.setNorthEast(map.get(x + 1).get(y - 1));
                }
                if ((x < mapWith - 1)) {
                    node.setEast(map.get(x + 1).get(y));
                }
                if ((x < mapWith - 1) && (y < mapHeight - 1)) {
                    node.setSouthEast(map.get(x + 1).get(y + 1));
                }
                if ((y < mapHeight - 1)) {
                    node.setSouth(map.get(x).get(y + 1));
                }
                if ((x > 0) && (y < mapHeight - 1)) {
                    node.setSouthWest(map.get(x - 1).get(y + 1));
                }
                if (x > 0) {
                    node.setWest(map.get(x - 1).get(y));
                }
                if (x > 0 && y > 0) {
                    node.setNorthWest(map.get(x - 1).get(y - 1));
                }
            }
        }
    }

    public ArrayList<ArrayList<Node>> getNodes() {
        return map;
    }

    public Node getNode(int x, int y) {
        return map.get(x).get(y);
    }

    public boolean setStartLocation(int x, int y) {
        Node n = map.get(x).get(y);
//        if (!n.isObstacle()) {
            map.get(startLocationX).get(startLocationY).setStart(false);
            n.setStart(true);
            startLocationX = x;
            startLocationY = y;
            return true;
//        }
//        return false;
    }

    public boolean setGoalLocation(int x, int y) {
        Node n = map.get(x).get(y);
        if (!n.isObstacle()) {
            map.get(goalLocationX).get(goalLocationY).setGoal(false);
            n.setGoal(true);
            goalLocationX = x;
            goalLocationY = y;
            return true;
        }
        return false;
    }

    public int getStartLocationX() {
        return startLocationX;
    }

    public int getStartLocationY() {
        return startLocationY;
    }

    public Node getStartNode() {
        return map.get(startLocationX).get(startLocationY);
    }

    public int getGoalLocationX() {
        return goalLocationX;
    }

    public int getGoalLocationY() {
        return goalLocationY;
    }

    public Node getGoalLocation() {
        return map.get(goalLocationX).get(goalLocationY);
    }

    public float getDistanceBetween(Node node1, Node node2) {
        //if the nodes are on top or next to each other, return 1
        //        if (node1.getX() == node2.getX() || node1.getY() == node2.getY()) {
        //            return 1 * (mapHeight + mapWith);
        //        } else { //if they are diagonal to each other return diagonal distance: sqrt(1^2+1^2)
        //            return (float) 1.7 * (mapHeight + mapWith);
        //        }
        int dx = node1.getX() - node2.getX();
        int dy = node1.getY() - node2.getY();

        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public int getMapWith() {
        return mapWith;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void clear() {
        startLocationX = 0;
        startLocationY = 0;
        goalLocationX = 0;
        goalLocationY = 0;
        createMap();
        registerEdges();
    }
}
