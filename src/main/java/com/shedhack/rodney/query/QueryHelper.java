/**
 * 
 */
package com.shedhack.rodney.query;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import com.shedhack.rodney.entity.definition.ColumnDefinition;
import com.shedhack.rodney.entity.definition.EntityDefinition;

/**
 * Query Helper Class
 * 
 * @author ichishty
 */
public class QueryHelper
{
    private static ObjectWriter jsonWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    /**
     * Returns all fields and the associate values, including the Id.
     * 
     * @param object
     * @param entityDefinition
     * @return
     */
    public static QueryNameValuesWrapper getAllFieldsAndValues(Object object, EntityDefinition entityDefinition)
    {
        QueryNameValuesWrapper wrapper = new QueryNameValuesWrapper();

        try
        {
            for (ColumnDefinition columnDef : entityDefinition.getColumns().values())
            {
                columnDef.getField().setAccessible(true);
                wrapper.addEntries(columnDef.getColumn().name(),
                        columnDef.getColumn().json() ? jsonWriter.writeValueAsString(columnDef.getField().get(object)) : columnDef
                                .getField().get(object));
            }

            entityDefinition.getIdDefinition().getField().setAccessible(true);
            wrapper.addEntries(entityDefinition.getIdDefinition().getId().name(), entityDefinition.getIdDefinition().getField()
                    .get(object));

        }
        catch (Exception ex)
        {
            throw new RodneyQueryException("Unable to create QueryNameValuesWrapper.", ex);
        }

        return wrapper;
    }
}
