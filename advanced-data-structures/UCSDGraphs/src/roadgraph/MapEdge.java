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
    
    private GeographicPoint toPoint;
    
    private String name;
    
    private String type;
    
    private double value;
    
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
