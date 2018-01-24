/**
 * 
 */
package roadgraph;

/**
 * @author arielsalazar
 */
public enum SpeedLimit {
    MOTORWAY("motorway", 180),
    TRUNK("trunk", 120),
    PRIMARY("primary", 90),
    SECONDARY("secondary", 70),
    TERTIARY("tertiary", 70),
    UNCLASSIFIED("unclassified", 50),
    RESIDENTIAL("residential", 40),
    MOTORWAY_LINK("motorway_link", 50),
    TRUNK_LINK("trunk_link", 50),
    PRIMARY_LINK("primary_link", 50),
    SECONDARY_LINK("secondary_link", 50),
    TERTIARY_LINK("tertiary_link", 50),
    LIVING_STREET("living_street", 40),
    ;
    
    private String name;
    private int value;
    
    private SpeedLimit(String name, int value) {
        this.name = name;
        this.value = value;
    }
    
    public String getName() {
        return name;
    }
    
    public int getValue() {
        return value;
    }
    
    public static SpeedLimit of(String name) {
        for (SpeedLimit sl : values()) {
            if (sl.name.equalsIgnoreCase(name)) {
                return sl;
            }
        }
        return SpeedLimit.UNCLASSIFIED;
    }
}
