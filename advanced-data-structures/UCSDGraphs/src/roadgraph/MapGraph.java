/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
package roadgraph;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import geography.GeographicPoint;
import util.GraphLoader;

/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which represents a graph of geographic locations Nodes in the graph are intersections between
 *
 */
public class MapGraph {

	/**
	 * Vertices registered in the graph.
	 */
    private Map<GeographicPointNode, Set<MapEdge>> vertices = new HashMap<>();

    /**
     * Get the number of vertices (road intersections) in the graph
     * 
     * @return The number of vertices in the graph.
     */
    public int getNumVertices() {
        return vertices.size();
    }

    /**
     * Return the intersections, which are the vertices in this graph.
     * 
     * @return The vertices in this graph as GeographicPoints
     */
    public Set<GeographicPoint> getVertices() {
        return Collections.unmodifiableSet(vertices.keySet());
    }

    /**
     * Get the number of road segments in the graph
     * 
     * @return The number of edges in the graph.
     */
    public int getNumEdges() {
        return (int)vertices.values().stream().flatMap(Set::stream).collect(Collectors.toSet()).size();
    }

    /**
     * Add a node corresponding to an intersection at a Geographic Point If the location is already in the graph or null, this method does not change
     * the graph.
     * 
     * @param location The location of the intersection
     * @return true if a node was added, false if it was not (the node was already in the graph, or the parameter is null).
     */
    public boolean addVertex(GeographicPoint location) {
        Objects.requireNonNull(location);
        // 
        return vertices.putIfAbsent(GeographicPointNode.of(location), new HashSet<>()) == null;
    }

    /**
     * Adds a directed edge to the graph from pt1 to pt2. Precondition: Both GeographicPoints have already been added to the graph
     * 
     * @param from The starting point of the edge
     * @param to The ending point of the edge
     * @param roadName The name of the road
     * @param roadType The type of the road
     * @param length The length of the road, in km
     * @throws IllegalArgumentException If the points have not already been added as nodes to the graph, if any of the arguments is null, or if the
     * length is less than 0.
     */
    public void addEdge(GeographicPoint from, GeographicPoint to, String roadName, String roadType, double length) throws IllegalArgumentException {
        Optional.ofNullable(from).orElseThrow(() -> new IllegalArgumentException("nul from point."));
        Optional.ofNullable(to).orElseThrow(() -> new IllegalArgumentException("nul to point."));
        Optional.ofNullable(roadName).orElseThrow(() -> new IllegalArgumentException("nul roadName."));
        Optional.ofNullable(roadType).orElseThrow(() -> new IllegalArgumentException("nul roadType."));
        if (Double.compare(length, 0.0D) < 0) {
        	    // safe way to check negative length.
            throw new IllegalArgumentException("negative length.");
        }
        // check if "from" and "to" have been register.
        checkIfPointIsInGraph(from);
        // Add new edge for "from".
        vertices.get(from).add(new MapEdge(checkIfPointIsInGraph(to), length, roadName, roadType));
        
    }
    
    /**
     * Checks if <code>GeographicPoint</code> has been register in the <code>vertices</code> map,
     * if it is not registered throws an <code>IllegalArgumentException</code>.
     * @param point
     */
    private GeographicPointNode checkIfPointIsInGraph(GeographicPoint point) {
        return vertices.keySet().stream()
                                .filter(key -> key.equals(point))
                                .findAny()
                                .orElseThrow(() -> new IllegalArgumentException("The point:" + point.toString() + " is not in the Graph."));
    }
    
    /**
     * Find the path from start to goal using breadth first search
     * @param start The starting location
     * @param goal The goal location
     * @return The list of intersections that form the shortest (unweighted) path from start to goal (including both start and goal).
     */
    public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
        // Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return bfs(start, goal, temp);
    }
    
    /**
     * Find the path from start to goal using breadth first search
     * @param start The starting location
     * @param goal The goal location
     * @param nodeSearched A hook for visualization. See assignment instructions for how to use it.
     * @return The list of intersections that form the shortest (unweighted) path from start to goal (including both start and goal).
     */
    public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal, Consumer<GeographicPoint> nodeSearched) {
        Optional.ofNullable(start).orElseThrow(() -> new IllegalArgumentException("nul start."));
        Optional.ofNullable(start).orElseThrow(() -> new IllegalArgumentException("nul goal."));
        Optional.ofNullable(start).orElseThrow(() -> new IllegalArgumentException("nul nodeSearched."));
        // 
        if (start.equals(goal)) {
            // start are the same o goal.
            return Arrays.asList(start);
        }
        // BFS implementation.
        Map<GeographicPointNode, GeographicPointNode>parent = new HashMap<>();
        Queue<GeographicPointNode>queue = new LinkedList<>(); // point to be visited.
        queue.add(GeographicPointNode.of(start));
        Set<GeographicPointNode>visited = new HashSet<>(); // have been visit points.
        do {
            GeographicPointNode current = queue.poll();
            // look for vizualization.
            nodeSearched.accept(current);
            if (current.equals(goal)) {
                // was find element.
                break;
            }
            // goes through all neighbors nodes. 
            for (MapEdge edge : vertices.get(current)) {
                if (visited.contains(edge.getToPoint())) {
                    // do nothing.
                    continue;
                }
                // visit vertex.
                visited.add(edge.getToPoint());
                parent.put(edge.getToPoint(), current);
                queue.offer(edge.getToPoint());
            }
        } while (!queue.isEmpty());
        // build path.
        return parent.isEmpty() ? Collections.emptyList() : buildPath(parent, start, goal);
    }
    
    /**
     * @param parent
     * @param start
     * @param goal
     * @return Builds the path to get the "goal" point based on the BFS implementation.
     */
    private static List<GeographicPoint> buildPath(Map<GeographicPointNode, GeographicPointNode>parent, GeographicPoint start, GeographicPoint goal) {
        LinkedList<GeographicPoint>path = new LinkedList<>();
        GeographicPoint current = goal;
        // while (!current.equals(start)) {
        while (!start.equals(current)) {
            path.addFirst(current);
            current = parent.get(current);
        }
        // add start.
        path.addFirst(start);
        return path;
    }
    
    /**
     * Find the path from start to goal using Dijkstra's algorithm
     * @param start The starting location
     * @param goal The goal location
     * @return The list of intersections that form the shortest path from start to goal (including both start and goal).
     */
    public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
        // Dummy variable for calling the search algorithms
        // You do not need to change this method.
        Consumer<GeographicPoint> temp = (x) -> {
        };
        return dijkstra(start, goal, temp);
    }

    /**
     * Find the path from start to goal using Dijkstra's algorithm
     * @param start The starting location
     * @param goal The goal location
     * @param nodeSearched A hook for visualization. See assignment instructions for how to use it.
     * @return The list of intersections that form the shortest path from start to goal (including both start and goal).
     */
    public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal, Consumer<GeographicPoint> nodeSearched) {
        return findPath(start, goal, nodeSearched, (gpStart, gpGoal) -> 0.0D);
    }
    
    /**
     * 
     * @param start
     * @param goal
     * @param nodeSearched
     * @param moveCost
     * @return The minimal path between <code>start</code> and <code>goal</code> node.
     */
    public List<GeographicPoint> findPath(GeographicPoint start,
                                          GeographicPoint goal,
                                          Consumer<GeographicPoint> nodeSearched,
                                          BiFunction<GeographicPoint, GeographicPoint, Double>moveCost) {
        Objects.requireNonNull(moveCost, "moveCost");
        // 
        if (start.equals(goal)) {
            return Collections.emptyList();
        }
        // clean weight of all nodes.
        vertices.keySet().forEach(GeographicPointNode::clear);
        vertices.values().stream().flatMap(Set::stream).map(MapEdge::getToPoint).forEach(GeographicPointNode::clear);
        // find path.
        GeographicPointNode source = GeographicPointNode.of(start);
        source.setWeight(0);
        Set<GeographicPointNode>settled = new HashSet<>();
        Map<GeographicPointNode, GeographicPointNode>parentMap = new HashMap<>();
        Queue<GeographicPointNode>unsettle = new PriorityBlockingQueue<>(1024, Comparator.comparing(GeographicPointNode::getWeight));
        unsettle.add(source);
        // iterate till unsettle nodes.
        while (!unsettle.isEmpty()) {
            GeographicPointNode current = unsettle.poll(); // get lowest distance.
            if (current.equals(goal)) {
                // stop, is not needed find more paths.
                break;
            }
            for (MapEdge edge : vertices.get(current)) {
                GeographicPointNode gp = edge.getToPoint();
                if (settled.contains(gp)) {
                    continue;
                }
                // weight between current source and goal point. 
                double tmp = current.getWeight() + edge.getValue() + moveCost.apply(gp, goal);
                if (tmp < gp.getWeight()) { // if tmp is less than current weight.
                    parentMap.put(gp, current);
                    // update weight.
                    gp.setWeight(tmp);
                }
                unsettle.add(gp);
            }
            settled.add(current);
            // hook visualization.
            nodeSearched.accept(current);
        }
        System.out.println("settled:" + settled.size());
        // build and get path.
        return parentMap.isEmpty() ? Collections.emptyList() : buildPath(parentMap, start, goal);
    }

    /**
     * Find the path from start to goal using A-Star search
     * 
     * @param start The starting location
     * @param goal The goal location
     * @return The list of intersections that form the shortest path from start to goal (including both start and goal).
     */
    public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
        // Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return aStarSearch(start, goal, temp);
    }
    
    /**
     * Find the path from start to goal using A-Star search
     * 
     * @param start The starting location
     * @param goal The goal location
     * @param nodeSearched
     *            A hook for visualization. See assignment instructions for how to
     *            use it.
     * @return The list of intersections that form the shortest path from start to
     *         goal (including both start and goal).
     */
    public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal, Consumer<GeographicPoint> nodeSearched) {
        return findPath(start, goal, nodeSearched, (gp, gpGoal) -> gp.distance(gpGoal));
    }
    
    public static void main2(String[] args) {
        System.out.print("Making a new map...");
        MapGraph firstMap = new MapGraph();
        System.out.print("DONE. \nLoading the map...");
        GraphLoader.loadRoadMap("data/testdata/simpletest.map", firstMap);
        
//        GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
//        GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);
//        List<GeographicPoint>path = firstMap.bfs(testStart, testEnd);
//        System.out.println();
//        System.out.println("path:" + path);

        // You can use this method for testing.

        /*
         * Here are some test cases you should try before you attempt the Week 3 End of
         * Week Quiz, EVEN IF you score 100% on the programming assignment.
         */

        MapGraph simpleTestMap = new MapGraph();
        GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);

        GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
        GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);

        System.out.println();
        System.out.println("Test 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
        List<GeographicPoint> testroute = simpleTestMap.dijkstra(testStart, testEnd);
//        List<GeographicPoint> testroute2 = simpleTestMap.aStarSearch(testStart, testEnd);
//        System.out.println(testroute);
//        System.out.println(testroute2);
        System.out.println(testroute.stream().map(gp -> "[Lat: " + gp.getX() + " Lon: " + gp.getY() + "]").collect(Collectors.toList()));
//        System.out.println(testroute2.stream().map(gp -> "[Lat: " + gp.getX() + " Lon: " + gp.getY() + "]").collect(Collectors.toList()));

//        MapGraph testMap = new MapGraph();
//        GraphLoader.loadRoadMap("data/maps/utc.map", testMap);

        // A very simple test using real data
//        testStart = new GeographicPoint(32.869423, -117.220917);
//        testEnd = new GeographicPoint(32.869255, -117.216927);
//        System.out.println("Test 2 using utc: Dijkstra should be 13 and AStar should be 5");
//        testroute = testMap.dijkstra(testStart, testEnd);
//        testroute2 = testMap.aStarSearch(testStart, testEnd);

        // A slightly more complex test using real data
//        testStart = new GeographicPoint(32.8674388, -117.2190213);
//        testEnd = new GeographicPoint(32.8697828, -117.2244506);
//        System.out.println("Test 3 using utc: Dijkstra should be 37 and AStar should be 10");
//        testroute = testMap.dijkstra(testStart, testEnd);
//        testroute2 = testMap.aStarSearch(testStart, testEnd);

        /* Use this code in Week 3 End of Week Quiz */
        /*
         * MapGraph theMap = new MapGraph();
         * System.out.print("DONE. \nLoading the map...");
         * GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
         * System.out.println("DONE.");
         * 
         * GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
         * GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);
         * 
         * 
         * List<GeographicPoint> route = theMap.dijkstra(start,end);
         * List<GeographicPoint> route2 = theMap.aStarSearch(start,end);
         * 
         */
        
        System.out.println("DONE.");
    }

    public static void main3(String[] args) {
        MapGraph simpleTestMap = new MapGraph();
        GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);

        GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
        GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);

        System.out.println("Test 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
        List<GeographicPoint> testroute = simpleTestMap.dijkstra(testStart, testEnd);
        List<GeographicPoint> testroute2 = simpleTestMap.aStarSearch(testStart, testEnd);

        MapGraph testMap = new MapGraph();
        GraphLoader.loadRoadMap("data/maps/utc.map", testMap);

        // A very simple test using real data
        testStart = new GeographicPoint(32.869423, -117.220917);
        testEnd = new GeographicPoint(32.869255, -117.216927);
        System.out.println("Test 2 using utc: Dijkstra should be 13 and AStar should be 5");
        testroute = testMap.dijkstra(testStart, testEnd);
        testroute2 = testMap.aStarSearch(testStart, testEnd);

        // A slightly more complex test using real data
        testStart = new GeographicPoint(32.8674388, -117.2190213);
        testEnd = new GeographicPoint(32.8697828, -117.2244506);
        System.out.println("Test 3 using utc: Dijkstra should be 37 and AStar should be 10");
        testroute = testMap.dijkstra(testStart, testEnd);
        testroute2 = testMap.aStarSearch(testStart, testEnd);
    }
    
    public static void main(String[] args) {
        /* Use this code in Week 3 End of Week Quiz */

        MapGraph theMap = new MapGraph();
        System.out.print("DONE. \nLoading the map...");
        GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
        System.out.println("DONE.");

        GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
        GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);

        List<GeographicPoint> route = theMap.dijkstra(start, end);
        List<GeographicPoint> route2 = theMap.aStarSearch(start, end);

        System.out.println("Dijkstra: " + route.size() + ", AStarSearch: " + route2.size());
        System.out.println("DONE.");
    }
}
