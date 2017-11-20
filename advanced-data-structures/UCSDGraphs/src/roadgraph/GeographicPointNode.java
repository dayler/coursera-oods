/**
 * 
 */
package roadgraph;

import java.util.Objects;

import geography.GeographicPoint;

/**
 * Used to add weight to the <code>GeographicPoint</code>.
 * 
 * @author dayler
 */
public class GeographicPointNode extends GeographicPoint {

    private static final long serialVersionUID = 20171119L;
    
    /**
     * Accumulated weight for the point..
     */
    private double weight = Integer.MAX_VALUE;
    
    private GeographicPointNode(GeographicPoint gp) {
        super(gp.getX(), gp.getY());
    }
    
    public double getWeight() {
        return weight;
    }
    
    public void setWeight(double weight) {
        this.weight = weight;
    }
    
    @Override
    public String toString() {
        return super.toString() + " W:" + weight;
    }
    
    public void clear() {
        weight = Integer.MAX_VALUE;
    }
    
    public static GeographicPointNode of(GeographicPoint gp) {
        Objects.requireNonNull(gp, "null GP");
        return gp instanceof GeographicPointNode ? (GeographicPointNode)gp : new GeographicPointNode(gp);
    }
}
