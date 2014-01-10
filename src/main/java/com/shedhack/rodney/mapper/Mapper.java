package com.shedhack.rodney.mapper;

import java.lang.reflect.Field;

import org.apache.commons.beanutils.PropertyUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.datastax.driver.core.Row;
import com.shedhack.rodney.entity.definition.ColumnDefinition;
import com.shedhack.rodney.entity.definition.EntityDefinition;
import com.shedhack.rodney.enums.CQLCollection;
import com.shedhack.rodney.enums.CQLDataType;
import com.shedhack.rodney.exception.RodneyException;
import com.shedhack.rodney.exception.RodneyMappingException;

/**
 * <pre>
 * Maps the entity to and from the Cassandra row. 
 * In order to do any conversions the {@link EntityDefinition} is required.
 * </pre>
 * 
 * @author ichishty
 */
public class Mapper
{
    private static ObjectMapper JSON_MAPPER = new ObjectMapper();

    /**
     * Maps specific columns to the entity. Fields not provided will be
     * null/default.
     * 
     * @param row
     * @param entityDefinition
     * @param fields
     *            - column names which must be mapped to the entity
     * @return Object entity
     */
    public static Object mapFromCQL(Row row, EntityDefinition entityDefinition, String[] fields)
    {
        Object entity = null;
        try
        {
            Class<?> clazz = Class.forName(entityDefinition.getEntityClass().getName());
            entity = clazz.newInstance();

            for (String field : fields)
            {
                // Field is the ID, so get the value and set it
                if (field.equalsIgnoreCase(entityDefinition.getIdDefinition().getId().name()))
                {
                    // Set the ID
                    Object idValue = valueFromRow(row, entityDefinition.getIdDefinition().getCqlType(), entityDefinition
                            .getIdDefinition().getField().getName());

                    setProperty(entity, entityDefinition.getIdDefinition().getId().name(), idValue);
                }

                else
                {
                    ColumnDefinition columnDefinition = entityDefinition.getColumns().get(field);
                    Object value = getValueType(row, columnDefinition);

                    // Use the definition to map it back to the appropriate
                    // field in the entity
                    setProperty(entity, columnDefinition.getField().getName(), value);
                }
            }

            return entity;

        }
        catch (Exception ex)
        {
            throw new RodneyMappingException("Problem mapping from CQL to entity [" + entity.getClass().getName() + "]. "
                    + ex.getMessage());
        }
    }

    /**
     * Maps the all columns and the Id from the database to the entity.
     * 
     * @param row
     * @param entityDefinition
     * @return Object
     */
    public static Object mapFromCQL(Row row, EntityDefinition entityDefinition)
    {
        Object entity = null;

        try
        {
            Class<?> clazz = Class.forName(entityDefinition.getEntityClass().getName());
            entity = clazz.newInstance();

            // Set the ID
            Object idValue = valueFromRow(row, entityDefinition.getIdDefinition().getCqlType(), entityDefinition.getIdDefinition()
                    .getField().getName());

            setProperty(entity, entityDefinition.getIdDefinition().getId().name(), idValue);

            // Set the remaining fields
            for (String columnNameKey : entityDefinition.getColumns().keySet())
            {
                ColumnDefinition columnDefinition = entityDefinition.getColumns().get(columnNameKey);

                Object value = getValueType(row, columnDefinition);

                // Use the definition to map it back to the appropriate
                // field in the entity
                setProperty(entity, columnDefinition.getField().getName(), value);
            }

            return entity;

        }
        catch (Exception ex)
        {
            throw new RodneyMappingException("Problem mapping from CQL to entity [" + entity.getClass().getName() + "]. "
                    + ex.getMessage());
        }

    }

    /**
     * Attempts to get the actual value from the row, based on the type & column
     * name
     * 
     * @param Row
     * @param ColumnDefinition
     * @return Object
     */
    public static Object getValueType(Row row, ColumnDefinition def)
    {
        // Not a collection type
        if (def.getCollection().equals(CQLCollection.NONE))
        {
            if (def.getColumn().json())
            {
                String json = (String) valueFromRow(row, def.getCqlType(), def.getColumn().name());
                return fromJson(json, def.getField().getType());
            }
            else
            {
                return valueFromRow(row, def.getCqlType(), def.getColumn().name());
            }
        }

        // Get the collection value
        else
        {
            return collectionValue(row, def.getCollection(), def.getCqlType(), def.getColumn().name(), def.getMapValueType());
        }
    }

    /**
     * Attempts to get the value for a particular column.
     * 
     * @param Row
     * @param CQLDataType
     *            e.g. STRING, LONG
     * @param String
     *            fieldName
     * @return Object
     */
    public static Object valueFromRow(Row row, CQLDataType type, String fieldName)
    {
        switch (type)
        {
            case DYNAMIC:
                return row.getString(fieldName);

            case STRING:
                return row.getString(fieldName);

            case UUID:
                return row.getUUID(fieldName);

            case DATE:
                return row.getDate(fieldName);

            case LONG:
                return row.getLong(fieldName);

            case INT:
                return row.getInt(fieldName);

            case TEXT:
                return row.getString(fieldName);

            case TIMESTAMP:
                return row.getDate(fieldName);

            case TIMEUUID:
                return row.getUUID(fieldName);

            case BIGINT:
                return row.getLong(fieldName);

            case DOUBLE:
                return row.getDouble(fieldName);

            case FLOAT:
                return row.getFloat(fieldName);

            case DECIMAL:
                return row.getFloat(fieldName);

            case BOOLEAN:
                return row.getBool(fieldName);

            case INTEGER:
                return row.getInt(fieldName);

            default:
                throw new RodneyMappingException("Unable to get row data for " + fieldName + ", the data type isn't supported ["
                        + type + "].");
        }
    }

    /**
     * Converts the string (json) to the required object.
     * 
     * @param json
     *            string
     * @param clazz
     *            type of entity
     * @return Object entity that has been hydrated from json.
     */
    private static Object fromJson(String json, Class<?> clazz)
    {
        try
        {
            if(json == null || json.length() == 0)
            {
                return null;
            }
            
            return JSON_MAPPER.readValue(json, clazz);
        }
        catch (Exception ex)
        {
            throw new RodneyMappingException("Object type requires mapping from json back to the entity, " + ex.getMessage());
        }
    }

    /**
     * @param Row
     * @param CQLCollection
     *            e.g SET, MAP etc
     * @param CQLDataType
     *            data type, e.g. STRING, LONG etc
     * @param String
     *            field
     * @param CQLDataType
     *            map value type (if not a map then will be null)
     * @return
     */
    public static Object collectionValue(Row row, CQLCollection collection, CQLDataType type, String fieldName,
            CQLDataType mapValueType)
    {
        switch (collection)
        {
            case SET:
                return row.getSet(fieldName, type.getDataTypeClass());

            case MAP:
                return row.getMap(fieldName, type.getDataTypeClass(), mapValueType.getDataTypeClass());

            case LIST:
                return row.getList(fieldName, type.getDataTypeClass());

            default:
                throw new RodneyMappingException("Unable to get the collection value for " + fieldName
                        + ". The collection type is unsupported [" + collection + "].");
        }
    }

    /**
     * Set the property on the actual entity
     * 
     * @param entity
     * @param field
     * @param value
     * @throws Exception
     */
    public static void setProperty(Object entity, String field, Object value) throws Exception
    {
        try
        {
            PropertyUtils.setProperty(entity, field, value);
        }
        catch (NoSuchMethodException nsme)
        {
            // No setter, try using reflection
            Class<?> clazz = entity.getClass();
            Field f = clazz.getDeclaredField(field);
            f.setAccessible(true);
            f.set(entity, value);
        }
    }
    
    /**
     * Returns the field value.
     * 
     * @param entity
     * @param field
     * @throws Exception
     */
    public static Object getProperty(Object entity, String field)
    {
        try
        {
            return PropertyUtils.getProperty(entity, field);
        }
        catch(Exception ex)
        {
            throw new RodneyException("Unable to get property[" + field + "]", ex);
        }
        
    }
}
