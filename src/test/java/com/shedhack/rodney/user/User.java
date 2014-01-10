package com.shedhack.rodney.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.shedhack.rodney.annotation.Cassandra;
import com.shedhack.rodney.annotation.Column;
import com.shedhack.rodney.annotation.Id;
import com.shedhack.rodney.enums.CQLCollection;

/**
 * <pre>
 * Example use of how to use {@link Cassandra Id Column}
 * The complete list of supported data types can be found {@link CQLCollection CQLDataType}
 * </pre>
 * 
 * @author ichishty
 *
 */
@Cassandra(keyspace = "example", table = "users")
public class User implements Serializable
{
    @Id(name = "id")
    private UUID id;
    
    @Column(name = "time_uuid", clusteringKey=true)
    private UUID timeuuid;
    
    @Column(name = "name")
    private String firstName;
    
    @Column(name = "surname")
    private String surnname;
    
    @Column(name = "joined_date")
    private Date joiningDate;

    @Column(name = "age")
    private int age;
    
    @Column(name = "reference_number", nullable=false)
    private Long accountReferenceNumber;

    @Column(name = "private_account")
    private boolean privateAccount;

    @Column(name = "nicknames")
    private List<String> nicknames = new ArrayList<String>();

    @Column(name = "friends")
    private Set<String> friends = new HashSet<String>();

    @Column(name = "posts")
    private Map<Date, String> posts = new HashMap<Date, String>();

    @Column(name = "primary_address", json=true)
    private Address address;

    /**
     * @return the id
     */
    public UUID getId()
    {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(UUID id)
    {
        this.id = id;
    }

    /**
     * @return the timeuuid
     */
    public UUID getTimeuuid()
    {
        return timeuuid;
    }

    /**
     * @param timeuuid the timeuuid to set
     */
    public void setTimeuuid(UUID timeuuid)
    {
        this.timeuuid = timeuuid;
    }

    /**
     * @return the firstName
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * @return the surnname
     */
    public String getSurnname()
    {
        return surnname;
    }

    /**
     * @param surnname the surnname to set
     */
    public void setSurnname(String surnname)
    {
        this.surnname = surnname;
    }

    /**
     * @return the joiningDate
     */
    public Date getJoiningDate()
    {
        return joiningDate;
    }

    /**
     * @param joiningDate the joiningDate to set
     */
    public void setJoiningDate(Date joiningDate)
    {
        this.joiningDate = joiningDate;
    }

    /**
     * @return the age
     */
    public int getAge()
    {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(Integer age)
    {
        this.age = age;
    }

    /**
     * @return the accountReferenceNumber
     */
    public Long getAccountReferenceNumber()
    {
        return accountReferenceNumber;
    }

    /**
     * @param accountReferenceNumber the accountReferenceNumber to set
     */
    public void setAccountReferenceNumber(Long accountReferenceNumber)
    {
        this.accountReferenceNumber = accountReferenceNumber;
    }

    /**
     * @return the privateAccount
     */
    public boolean isPrivateAccount()
    {
        return privateAccount;
    }

    /**
     * @param privateAccount the privateAccount to set
     */
    public void setPrivateAccount(boolean privateAccount)
    {
        this.privateAccount = privateAccount;
    }

    /**
     * @return the nicknames
     */
    public List<String> getNicknames()
    {
        return nicknames;
    }

    /**
     * @param nicknames the nicknames to set
     */
    public void setNicknames(List<String> nicknames)
    {
        this.nicknames = nicknames;
    }

    /**
     * @return the friends
     */
    public Set<String> getFriends()
    {
        return friends;
    }

    /**
     * @param friends the friends to set
     */
    public void setFriends(Set<String> friends)
    {
        this.friends = friends;
    }

    /**
     * @return the posts
     */
    public Map<Date, String> getPosts()
    {
        return posts;
    }

    /**
     * @param posts the posts to set
     */
    public void setPosts(Map<Date, String> posts)
    {
        this.posts = posts;
    }

    /**
     * @return the address
     */
    public Address getAddress()
    {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(Address address)
    {
        this.address = address;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "User [id=" + id + ", timeuuid=" + timeuuid + ", firstName=" + firstName + ", surnname=" + surnname
                + ", joiningDate=" + joiningDate + ", age=" + age + ", accountReferenceNumber=" + accountReferenceNumber
                + ", privateAccount=" + privateAccount + ", nicknames=" + nicknames + ", friends=" + friends + ", posts=" + posts
                + ", address=" + address + "]";
    }
 
}