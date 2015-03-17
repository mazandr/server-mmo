package com.win.strategy.battle.pathfinder;

/**
 *
 * @author omelnyk
 */
import java.util.ArrayList;
import java.util.Collections;

import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.pathfinder.heuristics.AStarHeuristic;

public class AStar {

    private AreaMap map;
    private AStarHeuristic heuristic;
    /**
     * closedList The list of Nodes not searched yet, sorted by their distance
     * to the goal as guessed by our heuristic.
     */
    private ArrayList<Node> closedList;
    private SortedNodeList openList;

    public AStar(AreaMap map, AStarHeuristic heuristic) {
        this.map = map;
        this.heuristic = heuristic;

        closedList = new ArrayList<>();
        openList = new SortedNodeList();
    }

    public Path calcShortestPath(BMCell start, BMCell goal) {
        return calcShortestPath(start.getPosX(), start.getPosY(), goal.getPosX(), goal.getPosY());
    }

    public Path calcShortestPath(int startX, int startY, int goalX, int goalY) {
        boolean checkStart = map.setStartLocation(startX, startY);
        boolean checkGoal = map.setGoalLocation(goalX, goalY);

        //Check if the goal node is blocked (if it is, it is impossible to find a path there)
        if (!checkGoal) {
            return null;
        }

        map.getStartNode().setDistanceFromStart(0);
        closedList.clear();
        openList.clear();
        openList.add(map.getStartNode());

        //while we haven't reached the goal yet
        while (openList.size() != 0) {

            //get the first Node from non-searched Node list, sorted by lowest distance from our goal as guessed by our heuristic
            Node current = openList.getFirst();

            // check if our current Node location is the goal Node. If it is, we are done.
            if (current.getX() == map.getGoalLocationX() && current.getY() == map.getGoalLocationY()) {
                return reconstructPath(current);
            }

            //move current Node to the closed (already searched) list
            openList.remove(current);
            closedList.add(current);

            //go through all the current Nodes neighbors and calculate if one should be our next step
            for (Node neighbor : current.getNeighborList()) {
                boolean neighborIsBetter;

                //if we have already searched this Node, don't bother and continue to the next one
                if (closedList.contains(neighbor)) {
                    continue;
                }

                //also just continue if the neighbor is an obstacle
                if (!neighbor.isObstacle()) {

                    // calculate how long the path is if we choose this neighbor as the next step in the path
                    float neighborDistanceFromStart = (current.getDistanceFromStart() + map.getDistanceBetween(current, neighbor));

                    //add neighbor to the open list if it is not there
                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                        neighborIsBetter = true;
                        //if neighbor is closer to start it could also be better
                    } else {
                        neighborIsBetter = neighborDistanceFromStart < current.getDistanceFromStart();
                    }
                    // set neighbors parameters if it is better
                    if (neighborIsBetter) {
                        neighbor.setPreviousNode(current);
                        neighbor.setDistanceFromStart(neighborDistanceFromStart);
                        neighbor.setHeuristicDistanceFromGoal(heuristic.getEstimatedDistanceToGoal(neighbor.getX(), neighbor.getY(), map.getGoalLocationX(), map.getGoalLocationY()));
                    }
                }
            }
        }
        return null;
    }

    private Path reconstructPath(Node node) {
        Path path = new Path();
        int checker = map.getMapHeight() * map.getMapWith();
        while (!(node.getPreviousNode() == null)) {
            path.prependWayPoint(node);
            node = node.getPreviousNode();

            if (path.getLength() > checker) {
                break;
            }
        }
        return path;
    }

    private class SortedNodeList {

        private ArrayList<Node> list = new ArrayList<>();

        public Node getFirst() {
            return list.get(0);
        }

        public void clear() {
            list.clear();
        }

        public void add(Node node) {
            list.add(node);
            Collections.sort(list);
        }

        public void remove(Node n) {
            list.remove(n);
        }

        public int size() {
            return list.size();
        }

        public boolean contains(Node n) {
            return list.contains(n);
        }
    }
}
