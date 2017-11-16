/**
 * 
 */
package application;

import java.util.Optional;

/**
 * @author arielsalazar
 */
public final class Parameters {

    private Parameters() {
        // No op.
    }
    
    public static <T>void requireNonNull(T param, String predicate) throws IllegalArgumentException {
        Optional.ofNullable(param).orElseThrow(() -> new IllegalArgumentException(predicate));
    }
    
    public static void nonNegative(double value, String predicate) throws IllegalArgumentException {
        if (Double.compare(value, 0.0D) < 0) {
            throw new IllegalArgumentException(predicate);
        }
    }
}
