/**
 * 
 */
package com.shedhack.rodney.exception;

/**
 * Parent exception for this project. 
 * 
 * @author ichishty
 *
 */
public class RodneyException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public RodneyException()
    {
        super();
    }

    public RodneyException(String string)
    {
        super(string);
    }

    public RodneyException(String string, Exception ex)
    {
        super(string, ex);
    }
}
