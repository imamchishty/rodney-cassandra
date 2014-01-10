package com.shedhack.rodney.enums;

/**
 * <pre>
 * Collection types supported by Cassandra. In order to map correctly please use interfaces that match (case-insensitive) 
 * Set, Map or List. Concrete implementations such as HashSet, HashMap, ArrayList will not be supported.
 * </pre>
 * 
 * @author ichishty
 */
public enum CQLCollection
{
    SET, MAP, LIST, NONE;

    private static CQLCollection[] values = CQLCollection.values();

    /**
     * Maps a string value back to the correct CQLCollection
     */
    public static CQLCollection valueOfIgnoreCase(String value)
    {
        return valueOf(value.toUpperCase());
    }

    /**
     * Returns true if the enum contains the argument.
     */
    public static boolean contains(String value)
    {
        value = value.toUpperCase();

        for (CQLCollection collection : values)
        {
            if (collection.name().equals(value))
            {
                return true;
            }
        }
        return false;
    }
}
