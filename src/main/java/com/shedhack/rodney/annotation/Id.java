package com.shedhack.rodney.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * Each entity must have a valid Id. This annotation is used to mark the appropriate field.
 * 
 * Cannot be applied to collections.
 * </pre>
 * 
 * @author ichishty
 */

@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Id
{
    /**
     * Column that the field maps to.
     * 
     * @return column name.
     */
    String name();
}
