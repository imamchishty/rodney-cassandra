package com.shedhack.rodney.repository;

import java.util.List;

import com.datastax.driver.core.Session;

/**
 * Repository for typical CRUD actions.
 * 
 * @param <T>
 *            Entity type
 * @param <V>
 *            Row Key class type
 * @author ichishty
 */
public interface Repository<T, V>
{
    /**
     * Finds the entity based on the primary key
     * 
     * @param id
     * @return T entity
     */
    T findById(V id);

    /**
     * <pre>
     * Save/Insert/Update the entity. 
     * Please beware that this is not always ideal as this will update every field.
     * </pre>
     * 
     * @param entity
     * @return entity
     */
    T upsert(T entity);

    /**
     * Returns all entities for the given type.
     * 
     * @return List<T>
     */
    List<T> getAll();

    /**
     * Global session to Cassandra.
     * 
     * @return Session
     */
    Session getSession();

    /**
     * Keyspace name (database name)
     * 
     * @return name of the keyspace
     */
    String getKeySpace();

    /**
     * Table name
     * 
     * @return table name.
     */
    String getTable();

    /**
     * Deletes an entity based on the primary key.
     * 
     * @param id
     *            key of the entity to be removed.
     */
    void deleteById(V id);
    
}
