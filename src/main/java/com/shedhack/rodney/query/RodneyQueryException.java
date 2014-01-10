/**
 * 
 */
package com.shedhack.rodney.query;

import com.shedhack.rodney.exception.RodneyException;

/**
 * Query Helper exception.
 * 
 * @author ichishty
 *
 */
public class RodneyQueryException extends RodneyException
{
    private static final long serialVersionUID = 1L;

    public RodneyQueryException(String string, Exception ex)
    {
        super(string, ex);
    }

    public RodneyQueryException(String string)
    {
        super(string);
    }
}
