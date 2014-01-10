package com.shedhack.rodney.repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import com.datastax.driver.core.Query;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.shedhack.rodney.entity.definition.EntityDefinition;
import com.shedhack.rodney.entity.definition.EntityDefinitionBuilder;
import com.shedhack.rodney.exception.RodneyException;
import com.shedhack.rodney.mapper.Mapper;
import com.shedhack.rodney.query.QueryHelper;
import com.shedhack.rodney.query.QueryNameValuesWrapper;
import com.shedhack.rodney.query.RodneyQueryException;

/**
 * Abstract Repository provides default implementations for CRUD functionality.
 * 
 * @param <T>
 *            Entity Type
 * @param <V>
 *            ID Type
 * @author ichishty
 */
public abstract class AbstractRepository<T extends Serializable, V> implements Repository<T, V>
{
    private Class<T> entityClass;

    private Class<V> idClass;

    private EntityDefinition entityDefinition;

    @SuppressWarnings("unchecked")
    public AbstractRepository()
    {
        entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        idClass = (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        entityDefinition = EntityDefinitionBuilder.build(entityClass);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public T findById(V id)
    {
        Query query = QueryBuilder.select()
                .from(entityDefinition.getCassandra().keyspace(), entityDefinition.getCassandra().table())
                .where(QueryBuilder.eq(entityDefinition.getIdDefinition().getId().name(), id)).limit(1);

        ResultSet results = getSession().execute(query);

        for (Row row : results)
        {
            return (T) Mapper.mapFromCQL(row, entityDefinition);
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> getAll()
    {
        List<T> entities = new ArrayList<T>();

        Query query = QueryBuilder.select().all()
                .from(entityDefinition.getCassandra().keyspace(), entityDefinition.getCassandra().table());

        List<Row> rows = getSession().execute(query).all();
        for (Row row : rows)
        {
            entities.add((T) Mapper.mapFromCQL(row, entityDefinition));
        }

        return entities;
    }

    /**
     * {@inheritDoc}
     */
    public T upsert(T entity)
    {
        QueryNameValuesWrapper wrapper = QueryHelper.getAllFieldsAndValues(entity, entityDefinition);

        // validate the data before updating/inserting
        validate(entity);

        Query query = QueryBuilder.insertInto(entityDefinition.getCassandra().keyspace(), entityDefinition.getCassandra().table())
                .values(wrapper.namesToArray(), wrapper.valuesToArray());
        getSession().execute(query);

        return entity;
    }

    /**
     * Validation, checks for the id, clustering key etc.
     */
    private void validate(T entity)
    {
        // Validate all columns before persisting
        for (String key : entityDefinition.getColumns().keySet())
        {
            // Clustering keys cannot be null
            if (entityDefinition.getColumns().get(key).getColumn().clusteringKey())
            {
                if (Mapper.getProperty(entity, entityDefinition.getColumns().get(key).getField().getName()) == null)
                {
                    throw new RodneyQueryException("Clustering key cannot be null. Please check the value for "
                            + entityDefinition.getColumns().get(key).getField().getName() + " on entity "
                            + entity.getClass().getName());
                }
            }

            // Column cannot be null
            if (!entityDefinition.getColumns().get(key).getColumn().nullable() && 
                    Mapper.getProperty(entity, entityDefinition.getColumns().get(key).getField().getName()) == null)
            {
                throw new RodneyQueryException("Property cannot be null. Please check the value for "
                        + entityDefinition.getColumns().get(key).getField().getName() + " on entity " + entity.getClass().getName());
            }
        }

        // Check that the ID isn't null
        if (Mapper.getProperty(entity, entityDefinition.getIdDefinition().getField().getName()) == null)
        {
            throw new RodneyQueryException("Id cannot be null. Please check the value for "
                    + entityDefinition.getIdDefinition().getField().getName() + " on entity " + entity.getClass().getName());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void deleteById(V id)
    {
        Query query = QueryBuilder.delete()
                .from(entityDefinition.getCassandra().keyspace(), entityDefinition.getCassandra().table())
                .where(QueryBuilder.eq(entityDefinition.getIdDefinition().getId().name(), id));

        getSession().execute(query);
    }

    /**
     * {@inheritDoc}
     */
    public EntityDefinition getEntityDefinition()
    {
        return entityDefinition;
    }

    /**
     * {@inheritDoc}
     */
    public String getKeySpace()
    {
        return entityDefinition.getCassandra().keyspace();
    }

    /**
     * {@inheritDoc}
     */
    public String getTable()
    {
        return entityDefinition.getCassandra().table();
    }
}
