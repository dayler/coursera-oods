/**
 * 
 */
package roadgraph;

import java.util.Objects;

import geography.GeographicPoint;

/**
 * @author dayler
 */
public class GeographicPointNode extends GeographicPoint {

	private static final long serialVersionUID = 20171119L;
	
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
	
	public static GeographicPointNode of(GeographicPoint gp) {
		Objects.requireNonNull(gp, "null GP");
		return new GeographicPointNode(gp);
	}
}
