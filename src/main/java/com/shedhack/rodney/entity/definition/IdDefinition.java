package com.shedhack.rodney.entity.definition;

import java.lang.reflect.Field;

import com.shedhack.rodney.annotation.Id;
import com.shedhack.rodney.enums.CQLDataType;

/**
 * Responsible for holding the entities Id metadata. Will be held as part of the
 * {@link EntityDefinition} object.
 * 
 * @author ichishty
 */
public class IdDefinition
{
    private Id id;

    private Field field;

    private CQLDataType cqlType;

    /**
     * Constructs a new instance of the IdDefinition type.
     * 
     * @param id
     *            annotation
     * @param field
     *            field marked with the annotation
     * @param cqlType
     *            datatype
     */
    public IdDefinition(Id id, Field field, CQLDataType cqlType)
    {
        super();
        this.id = id;
        this.field = field;
        this.cqlType = cqlType;
    }

    /**
     * @return the id
     */
    public Id getId()
    {
        return id;
    }

    /**
     * @return the field
     */
    public Field getField()
    {
        return field;
    }

    /**
     * @return the cqlType
     */
    public CQLDataType getCqlType()
    {
        return cqlType;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "IdDefinition [id=" + id + ", field=" + field + ", cqlType=" + cqlType + "]";
    }

}
