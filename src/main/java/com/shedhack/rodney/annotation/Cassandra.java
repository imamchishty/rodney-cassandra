package com.shedhack.rodney.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * This annotation should be used to mark entities that need to be mapped to/from Cassandra.
 * 
 * Counter tables are not currently supported.
 * 
 * </pre>
 * 
 * @author ichishty
 *
 */
@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Cassandra 
{
    /**
     * Cassandra keyspace name.
     * 
     * @return keyspace name
     */
	String keyspace();

	/**
	 * Cassandra table name.
	 * 
	 * @return table name.
	 */
	String table();
}
