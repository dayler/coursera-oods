/**
 * 
 */
package roadgraph;

import java.util.Objects;

/**
 * Represent an edge of vertex point.
 *  
 * @author arielsalazar
 */
public class MapEdge {
    
	/**
	 * Point to reach form initial vertex.
	 */
    private GeographicPointNode toPoint;
    
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
    
    private WeightBuilder builder;
    
    /**
     * MapEdge's Constructor.
     * @param toPoint
     * @param value
     * @param name
     * @param type
     */
    public MapEdge(GeographicPointNode toPoint, double value, String name, String type, WeightBuilder builder) {
        Objects.requireNonNull(toPoint);
        Objects.requireNonNull(builder, "builder");
        //  
        this.toPoint = toPoint;
        this.value = value;
        this.name = name;
        this.type = type;
        this.builder = builder;
    }
    
    public GeographicPointNode getToPoint() {
        return toPoint;
    }
    
    public String getName() {
        return name;
    }
    
    public String getType() {
        return type;
    }
    
    public double getValue() {
        return builder.getValue(value);
    }
    
    @Override
    public String toString() {
        return name + "[" + toPoint + "]";
    }
}
