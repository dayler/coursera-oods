/**
 * 
 */
package roadgraph;

import java.util.Objects;

import geography.GeographicPoint;

/**
 * Represent an edge of vertex point.
 *  
 * @author arielsalazar
 */
public class MapEdge {
    
	/**
	 * Point to reach form initial vertex.
	 */
    private GeographicPoint toPoint;
    
    /**
     * Name of edge.
     */
    private String name;
    
    /**
     * Type of edge.
     */
    private String type;
    
    /**
     * Value of edge.
     */
    private double value;
    
    /**
     * MapEdge's Constructor.
     * @param toPoint
     * @param value
     * @param name
     * @param type
     */
    public MapEdge(GeographicPoint toPoint, double value, String name, String type) {
        Objects.requireNonNull(toPoint);
        //  
        this.toPoint = toPoint;
        this.value = value;
        this.name = name;
        this.type = type;
    }
    
    public GeographicPoint getToPoint() {
        return toPoint;
    }
    
    public String getName() {
        return name;
    }
    
    public String getType() {
        return type;
    }
    
    public double getValue() {
        return value;
    }
}
