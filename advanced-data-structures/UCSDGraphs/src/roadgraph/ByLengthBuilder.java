/**
 * 
 */
package roadgraph;

/**
 * @author arielsalazar
 */
public class ByLengthBuilder implements WeightBuilder {

    /* (non-Javadoc)
     * @see roadgraph.WeightBuilder#getValue(double)
     */
    @Override
    public double getValue(double value) {
        return value;
    }

}
