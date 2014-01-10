/**
 * 
 */
package com.shedhack.rodney.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.cassandraunit.CassandraCQLUnit;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.shedhack.rodney.annotation.Cassandra;

/**
 * Test case highlighting how to interact with the repository framework provided
 * by {@link Cassandra}.
 * 
 * <pre>
 * See {@link http://nickgroenke.com/2013/01/05/quick-start-cassandra-unit/ https://github.com/jsevellec/cassandra-unit/wiki/How-to-create-a-json-dataset}
 * Also {@link http://planetcassandra.org/blog/post/cassandra-unit-1201-is-out-cql3-script-support-and-spring-integration}
 * </pre>
 * 
 * @author ichishty
 */
public class UserRepositoryTest
{
    @Rule
    public CassandraCQLUnit cassandraCQLUnit = new CassandraCQLUnit(new ClassPathCQLDataSet("example.cql", "example"));

    private UserRepository userRepo;

    private UUID SAM_ID = UUID.fromString("7da3f8a7-3805-45c0-b7b2-212595b47e9f");

    @Before
    public void setup() throws Exception
    {
        userRepo = new UserRepository(cassandraCQLUnit.session);
    }
    
    /**
     * In this test we expect the user to be found based on the UUID. Once the
     * user is found it will be mapped back to the correct data type.
     */
    @Test
    public void should_find_user_by_id()
    {
        // Arrange & Act. The ID is of UUID type so create a valid one and
        // execute the query.
        User user = userRepo.findById(SAM_ID);

        // Assert
        assertNotNull(user);
        assertEquals(UUID.fromString("a4a70900-24e1-11df-8924-001ff3591711"), user.getTimeuuid());
        assertEquals("sam", user.getFirstName());
        assertEquals("wise", user.getSurnname());
        assertEquals(25, user.getAge());

        // Check the list
        assertFalse(user.getFriends().isEmpty());
        assertEquals(3, user.getFriends().size());
        assertTrue(user.getFriends().contains("frodo"));

        // Check the posts (Map)
        assertEquals(2, user.getPosts().size());

        for (Date mapKey : user.getPosts().keySet())
        {
            assertNotNull(user.getPosts().get(mapKey));
        }

        // Check the set
        assertEquals(2, user.getNicknames().size());
        assertTrue(user.getNicknames().contains("chubby"));

        // Check the JSON object
        assertNotNull(user.getAddress());
        assertEquals("SW11AA", user.getAddress().getPostcode());
        assertEquals("25", user.getAddress().getHouse());
    }

    /**
     * Test to validate if the delete method works as expected.
     */
    @Test
    public void should_delete_user_by_id()
    {
        // Create a new user and check that it has been created
        UUID uuid = UUID.randomUUID();
        User user = new User();
        user.setId(uuid);

        // Act
        userRepo.deleteById(uuid);

        // Assert
        User deletedUser = userRepo.findById(uuid);
        assertNull(deletedUser);
    }

    /**
     * Tests the repositories ability to persist a new entity.
     */
    @Test
    public void should_create_user()
    {
        // Arrange
        UUID uuid = UUID.randomUUID();
        User user = new User();
        user.setId(uuid);
        user.setFirstName("Sherlock");
        user.setAge(50);
        user.setAddress(new Address("212B", "Baker Street"));
        user.setAccountReferenceNumber(100000000L);
        
        // clustering key cannot be null
        user.setTimeuuid(UUID.fromString("a4a70900-24e1-11df-8924-001ff3591711"));

        // Act
        userRepo.upsert(user);
        User sherlock = userRepo.findById(uuid);

        // Assert
        assertNotNull(sherlock);
        assertEquals("Sherlock", sherlock.getFirstName());
        assertEquals("212B", user.getAddress().getHouse());
        assertEquals("Baker Street", user.getAddress().getPostcode());
        assertEquals(50, user.getAge());
        assertTrue(user.getFriends().isEmpty());
        assertTrue(user.getPosts().isEmpty());
        assertTrue(user.getNicknames().isEmpty());
        assertEquals(new Long(100000000), user.getAccountReferenceNumber());
    }

    /**
     * Verifies the update functionality
     */
    @Test
    public void should_update_user()
    {
        // Get the user
        User user = userRepo.findById(SAM_ID);
        Date date = new Date();
        String post = "The hobbit and the goat.";

        // Add a new post
        user.getPosts().put(date, post);
        userRepo.upsert(user);

        // Assert - everything to make sure no loss

        User loaded = userRepo.findById(SAM_ID);
        assertNotNull(user);
        assertEquals(UUID.fromString("a4a70900-24e1-11df-8924-001ff3591711"), loaded.getTimeuuid());
        assertEquals("sam", loaded.getFirstName());
        assertEquals("wise", loaded.getSurnname());
        assertEquals(25, loaded.getAge());

        // Check the list
        assertFalse(loaded.getFriends().isEmpty());
        assertEquals(3, loaded.getFriends().size());
        assertTrue(loaded.getFriends().contains("frodo"));

        // Check the posts (Map)
        assertEquals(3, loaded.getPosts().size());
        assertTrue(loaded.getPosts().containsValue(post));
        for (Date mapKey : loaded.getPosts().keySet())
        {
            assertNotNull(user.getPosts().get(mapKey));
        }

        // Check the set
        assertEquals(2, loaded.getNicknames().size());
        assertTrue(loaded.getNicknames().contains("chubby"));

        // Check the JSON object
        assertNotNull(loaded.getAddress());
        assertEquals("SW11AA", loaded.getAddress().getPostcode());
        assertEquals("25", loaded.getAddress().getHouse());
    }

    /**
     * Verifies if the getAll method works as expected.
     */
    @Test
    public void should_find_all_users()
    {
        List<User> users = userRepo.getAll();
        assertEquals(1, users.size());
    }

}
