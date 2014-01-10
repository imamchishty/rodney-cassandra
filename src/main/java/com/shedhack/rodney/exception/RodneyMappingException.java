/**
 * 
 */
package com.shedhack.rodney.exception;

/**
 * Mapping exception.
 * 
 * @author ichishty
 *
 */
public class RodneyMappingException extends RodneyException
{
    private static final long serialVersionUID = 1L;

    public RodneyMappingException(String string)
    {
        super(string);
    }

    public RodneyMappingException(String string, Exception ex)
    {
        super(string, ex);
    }

}
