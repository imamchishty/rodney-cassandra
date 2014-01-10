package com.shedhack.rodney.entity.definition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shedhack.rodney.annotation.Cassandra;

/**
 * <pre>
 * This object holds metadata about a single entity. The data it holds 
 * includes metadata for the entire entity (fields, columns, types, id).
 * </pre>
 * 
 * @author ichishty
 */
public class EntityDefinition
{

    private Cassandra cassandra;

    private Class<?> entityClass;

    private IdDefinition idDefinition;

    private Map<String, ColumnDefinition> columns = new HashMap<String, ColumnDefinition>();
    
    /**
     * Definition for the entire entity. It will contain metadata for all
     * fields, columns and the Id.
     * 
     * @param entityClass
     * @param cassandra
     * @param idDefinition
     * @param columns
     */
    public EntityDefinition(Class<?> entityClass, Cassandra cassandra, IdDefinition idDefinition, Map<String, ColumnDefinition> columns)
    {
        super();
        this.cassandra = cassandra;
        this.idDefinition = idDefinition;
        this.columns = columns;
        this.entityClass = entityClass;
    }

    /**
     * @return the cassandra
     */
    public Cassandra getCassandra()
    {
        return cassandra;
    }

    /**
     * @return the columns
     */
    public Map<String, ColumnDefinition> getColumns()
    {
        return columns;
    }

    /**
     * @return the entityClass
     */
    public Class<?> getEntityClass()
    {
        return entityClass;
    }

    /**
     * @return the idDefinition
     */
    public IdDefinition getIdDefinition()
    {
        return idDefinition;
    }
   
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "EntityDefinition [cassandra=" + cassandra + ", entityClass=" + entityClass + ", idDefinition=" + idDefinition
                + ", columns=" + columns + "]";
    }
    
    

}
