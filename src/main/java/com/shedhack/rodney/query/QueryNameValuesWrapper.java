package com.shedhack.rodney.query;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper that stores an entities fields and values for use with QueryBuilder.
 * 
 * @author ichishty
 *
 */
public class QueryNameValuesWrapper
{
    private List<String> names = new ArrayList<String>();

    private List<Object> values = new ArrayList<Object>();

    public QueryNameValuesWrapper()
    {}

    public void addEntries(String name, Object value)
    {
        this.names.add(name);
        this.values.add(value);
    }

    /**
     * @return the names
     */
    public List<String> getNames()
    {
        return names;
    }

    /**
     * @param names
     *            the names to set
     */
    public void setNames(List<String> names)
    {
        this.names = names;
    }

    /**
     * @return the values
     */
    public List<Object> getValues()
    {
        return values;
    }

    /**
     * @param values
     *            the values to set
     */
    public void setValues(List<Object> values)
    {
        this.values = values;
    }

    public String[] namesToArray()
    {
        String[] array = new String[names.size()];
        array = names.toArray(array);
        return array;
    }

    public Object[] valuesToArray()
    {
        Object[] array = new Object[values.size()];
        array = values.toArray(array);
        return array;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "QueryNameValuesWrapper [names=" + names + ", values=" + values + "]";
    }

}
