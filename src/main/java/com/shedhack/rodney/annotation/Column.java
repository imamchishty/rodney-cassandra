package com.shedhack.rodney.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.codehaus.jackson.map.ObjectMapper;

import com.shedhack.rodney.enums.CQLCollection;
import com.shedhack.rodney.repository.AbstractRepository;

/**
 * <pre>
 * Annotation is used to mark fields that will be mapped to/from Cassandra.
 * Fields without this annotation are not handled, i.e. treated as transient.
 * 
 * In order to make use of the object mapper it is recommend to use the
 * {@link AbstractRepository}.
 * 
 * When applied to either a Set, Map or List the type must not be a concrete implementation.
 * This is because the type is matched against {@link CQLCollection}. Thus only use interfaces, e.g:
 * 
 * Map&lt;String, String&gt; map == is valid.
 * HashMap&lt;String, String&gt; map == invalid.
 * </pre>
 * 
 * @author ichishty
 */
@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Column
{
    /**
     * Cassandra Column name that this field maps to.
     * 
     * @return name of the column
     */
    String name();

    /**
     * If false then the field cannot be null before an upsert operation. This
     * will result in a runtime exception.
     */
    boolean nullable() default true;

    /**
     * <pre>
     * Converts the text/string/varchar type to an object using {@link ObjectMapper}.
     *  
     * If the cassandra data type isn't supported, i.e. non string/text/varchar type 
     * then an exception will be thrown. The object that it will be mapped to must not be final.
     * 
     * The type must provide a default contructor and appropriate setters. 
     * 
     * This option will be applied to non-collection fields only.
     * 
     * </pre>
     */
    boolean json() default false;

    /**
     * Clustering Key, cannot be null.
     */
    boolean clusteringKey() default false;

}
