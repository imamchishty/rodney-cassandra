package com.shedhack.rodney.enums;

/**
 * Data types supported by Cassandra.
 * 
 * @author ichishty
 */
public enum CQLDataType
{
    DYNAMIC(Object.class), LONG(Long.class), STRING(String.class), 
    DATE(java.util.Date.class), ASCII(String.class), BIGINT(Long.class), 
    BOOLEAN(Boolean.class), DECIMAL(Float.class), DOUBLE(Double.class), 
    FLOAT(Float.class), INT(Integer.class), INTEGER(Integer.class), 
    TEXT(String.class), TIMESTAMP(java.util.Date.class), TIMEUUID(java.util.UUID.class), 
    UUID(java.util.UUID.class), VARCHAR(String.class), VARINT(java.math.BigInteger.class);

    CQLDataType(Class<?> clazz)
    {
        this.clazz = clazz;
    }

    private static CQLDataType[] values = CQLDataType.values();

    private Class<?> clazz;

    public Class<?> getDataTypeClass()
    {
        return clazz;
    }

    /**
     * Maps a string value back to the correct CQLDataType
     */
    public static CQLDataType valueOfIgnoreCase(String value)
    {
        return valueOf(value.toUpperCase());
    }

    /**
     * Returns true if the enum contains the argument.
     */
    public static boolean contains(Class<?> clazz)
    {
        String simpleName = clazz.getSimpleName().toUpperCase();

        for (CQLDataType type : values)
        {
            // Check that the data type is supported .toString(), but instead
            // cast Type to Class, you get .isPrimitive()
            if (type.name().equals(simpleName) && (clazz.isPrimitive() || type.getDataTypeClass().equals(clazz)))
            {
                // if the type is a primitive then return or the fully qualified
                // class name is what we expect
                // then we're good to go.
                return true;
            }
        }
        return false;
    }
}
