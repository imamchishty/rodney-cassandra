package com.shedhack.rodney.user;

import java.util.UUID;

import com.datastax.driver.core.Session;
import com.shedhack.rodney.repository.AbstractRepository;

/**
 * <pre>
 * Simple example of how we can leverage the AbstractRepository to work with
 * Cassandra.
 * 
 * From the example you can see we need to extend the {@link AbstractRepository} passing in the
 * entity and the Id.
 * 
 * The session is required by the {@link AbstractRepository} so we must provide it. 
 * The session can be shared between multiple repositories.
 * In a typical CDI you would probably have it injected.
 * </pre>
 * 
 * @author ichishty
 */
public class UserRepository extends AbstractRepository<User, UUID>
{
    private Session session;

    /**
     * Construct a new repository, passing in the session.
     * 
     * @param session
     */
    public UserRepository(Session session)
    {
        this.session = session;
    }

    /**
     * {@inheritDoc}
     */
    public Session getSession()
    {
        return session;
    }
}
