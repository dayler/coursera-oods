/**
 * 
 */
package roadgraph;

/**
 * @author arielsalazar
 */
public class ByDurationBuilder implements WeightBuilder {
    
    private SpeedLimit speedLimit;
    
    public ByDurationBuilder(String type) {
        speedLimit = SpeedLimit.of(type);
    }
    
    @Override
    public double getValue(double value) {
        return (value * 60) / speedLimit.getValue();
    }
}
