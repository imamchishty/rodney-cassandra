package com.shedhack.rodney.user;

public class Address
{
    private String house;
    private String postcode;

    public Address(){}

    public Address(String house, String postcode)
    {
        this.house = house;
        this.postcode = postcode;
    }
    
    /**
     * @return the house
     */
    public String getHouse()
    {
        return house;
    }

    /**
     * @param house the house to set
     */
    public void setHouse(String house)
    {
        this.house = house;
    }

    /**
     * @return the postcode
     */
    public String getPostcode()
    {
        return postcode;
    }

    /**
     * @param postcode the postcode to set
     */
    public void setPostcode(String postcode)
    {
        this.postcode = postcode;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "Address [house=" + house + ", postcode=" + postcode + "]";
    }
    

    
}
