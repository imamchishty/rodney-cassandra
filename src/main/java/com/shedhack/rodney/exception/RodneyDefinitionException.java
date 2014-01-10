/**
 * 
 */
package com.shedhack.rodney.exception;

import com.shedhack.rodney.entity.definition.EntityDefinition;

/**
 * <pre>
 * Exception is thrown when {@link EntityDefinition ColumnDefinition IdDefinition} is invalid.
 * </pre>
 * 
 * @author ichishty
 */
public class RodneyDefinitionException extends RodneyException
{
    private static final long serialVersionUID = 1L;

    public RodneyDefinitionException(String string)
    {
        super(string);
    }

}
