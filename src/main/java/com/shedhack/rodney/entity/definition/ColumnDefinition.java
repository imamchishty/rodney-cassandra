package com.shedhack.rodney.entity.definition;

import java.lang.reflect.Field;

import com.shedhack.rodney.annotation.Column;
import com.shedhack.rodney.enums.CQLCollection;
import com.shedhack.rodney.enums.CQLDataType;

/**
 * <pre>
 * Definition is responsible for capturing metadata about the field that has been marked by
 * the {@link Column} annotation. This definition will be used by queries and for mapping.
 * 
 * Will be held as part of the {@link EntityDefinition} object.
 * </pre>
 * 
 * @author ichishty
 */
public class ColumnDefinition
{
    private Field field;

    private Column column;

    private CQLDataType cqlType;

    private CQLCollection collection;

    private CQLDataType mapValueType;

    /**
     * Constructs a new definition.
     * 
     * @param field
     *            which has been marked by {@link Column}
     * @param column
     *            the actual annotation
     * @param cqlType
     *            data type for the field
     * @param collection
     *            if field isn't a collection this this will be null
     * @param cqlMapValueType
     *            if the field is a map then we need to know the value type.
     */
    public ColumnDefinition(Field field, Column column, CQLDataType cqlType, CQLCollection collection, CQLDataType cqlMapValueType)
    {
        super();
        this.field = field;
        this.column = column;
        this.cqlType = cqlType;
        this.collection = collection;
        this.mapValueType = cqlMapValueType;
    }

    /**
     * @return the field
     */
    public Field getField()
    {
        return field;
    }

    /**
     * @return the column
     */
    public Column getColumn()
    {
        return column;
    }

    /**
     * @return the cqlType
     */
    public CQLDataType getCqlType()
    {
        return cqlType;
    }

    /**
     * @return the collection
     */
    public CQLCollection getCollection()
    {
        return collection;
    }

    /**
     * @return the mapValueType
     */
    public CQLDataType getMapValueType()
    {
        return mapValueType;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "ColumnDefinition [field=" + field + ", column=" + column + ", cqlType=" + cqlType + ", collection=" + collection
                + ", mapValueType=" + mapValueType + "]";
    }

}
