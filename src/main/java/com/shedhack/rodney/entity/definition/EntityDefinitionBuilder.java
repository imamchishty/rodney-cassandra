package com.shedhack.rodney.entity.definition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shedhack.rodney.annotation.Cassandra;
import com.shedhack.rodney.annotation.Column;
import com.shedhack.rodney.annotation.Id;
import com.shedhack.rodney.enums.CQLCollection;
import com.shedhack.rodney.enums.CQLDataType;
import com.shedhack.rodney.exception.RodneyDefinitionException;

/**
 * <pre>
 * Responsible for the construction of an individual {@link EntityDefinition}.
 * </pre>
 * 
 * @author ichishty
 */
public class EntityDefinitionBuilder
{
    /**
     * Builds a new entity definition for a given class type.
     * 
     * <pre>
     *  Each entity must use the appropriate annotations, please refer to
     *  {@link Cassandra Id Column}
     * </pre>
     * 
     * @throws RodneyDefinitionException
     *             if mandatory annotations are missing or invalid.
     * @param entityClass
     * @return EntityDefinition
     */
    public static EntityDefinition build(Class<?> entityClass)
    {
        // Get the cassandra annotation from the entity, this cannot be null.
        Cassandra cassandra = entityClass.getAnnotation(Cassandra.class);

        // Build a default map of columns
        Map<String, ColumnDefinition> columns = new HashMap<String, ColumnDefinition>();
        IdDefinition idDefinition = null;

        // Iterate over all of the declared fields in the entity, check for
        // annotations
        for (Field field : entityClass.getDeclaredFields())
        {
            Annotation[] annotations = field.getDeclaredAnnotations();

            // We're only concerned with fields that have annotations
            for (Annotation annotation : annotations)
            {
                // Column found, attempt to add to create a column definition
                if (annotation instanceof Column)
                {
                    Column column = (Column) annotation;
                    columns.put(column.name(), createColumnDefinition(column, field));
                }

                // Each entity must have an Id, attempt to create the Id
                // definition
                else if (annotation instanceof Id)
                {
                    idDefinition = new IdDefinition((Id) annotation, field, getDataType(field.getType()));
                }
            }
        }

        // Before we return with an Entity Definition we need to verify that all
        // is well.
        // If this validation fails then a runtime exception is thrown.
        validateEntity(entityClass, cassandra, idDefinition);

        // All is well, return the definition.
        return new EntityDefinition(entityClass, cassandra, idDefinition, columns);
    }

    /**
     * Validates the major components of the EntityDefinition object.
     * 
     * @throws RodneyDefinitionException
     * @param entityClass
     * @param cassandra
     * @param idDefinition
     */
    private static void validateEntity(Class<?> entityClass, Cassandra cassandra, IdDefinition idDefinition)
    {
        if (entityClass == null)
        {
            throw new RodneyDefinitionException("Unable to create the entity definition. Null entity class. ");
        }

        if (cassandra == null)
        {
            throw new RodneyDefinitionException("Unable to create the entity definition for " + entityClass.getName()
                    + ", Cassandra annotation is missing.");
        }

        if (idDefinition == null || idDefinition.getId() == null || idDefinition.getField() == null
                || idDefinition.getCqlType() == null)
        {
            throw new RodneyDefinitionException("Unable to create the entity definition for " + entityClass.getName()
                    + ", No Id Definition was created, this could be as a result of missing Id annotation.");
        }

    }

    /**
     * Create a column definition based on the annotation for the entities
     * field.
     * 
     * @throws IllegalStateException
     *             when the definition cannot be created, could be due to
     *             mismatch of types.
     * @param Column
     * @param Field
     * @return ColumnDefinition
     */
    public static ColumnDefinition createColumnDefinition(Column column, Field field)
    {
        CQLDataType type = column.json() ? CQLDataType.DYNAMIC : null;
        CQLCollection collection = CQLCollection.NONE;
        CQLDataType mapValueType = null;

        // If the field is a collection then we need to check if we support it.
        if (CQLCollection.contains(field.getType().getSimpleName()))
        {
            // Get the collection type
            ParameterizedType collectionType = (ParameterizedType) field.getGenericType();
            Class<?> collectionTypeClass = (Class<?>) collectionType.getActualTypeArguments()[0];

            // Map the collection type to our data type enum
            if (type == null)
            {
                type = getDataType(collectionTypeClass);
            }

            // Map the collection to the collections enum
            collection = CQLCollection.valueOfIgnoreCase(field.getType().getSimpleName());

            // If we have a map then we also need the Value type, as the first
            // argument
            // is the Key type.
            if (CQLCollection.MAP.equals(collection))
            {
                Class<?> valueTypeClass = (Class<?>) collectionType.getActualTypeArguments()[1];
                mapValueType = getDataType(valueTypeClass);
            }

            // JSON not supported by collections
            if (column.json())
            {
                throw new RodneyDefinitionException(field.getName()
                        + " has been marked with json. This is a collection and json isn't currently supported.");
            }
        }

        // Not a collection type and the type hasn't been set
        else if (type == null)
        {
            type = getDataType(field.getType());
        }

        return new ColumnDefinition(field, column, type, collection, mapValueType);
    }

    /**
     * Returns the appropriate data type (enum) based on the class type of the
     * field
     * 
     * @param Class
     *            <?>
     * @return CQLDataType
     * @throws IllegalArgumentException
     *             if the data type isn't supported.
     */
    public static CQLDataType getDataType(Class<?> clazz)
    {
        if (CQLDataType.contains(clazz))
        {
            return CQLDataType.valueOfIgnoreCase(clazz.getSimpleName());
        }

        throw new RodneyDefinitionException("Wrong datatype, unsupported " + clazz.getName());
    }

}
