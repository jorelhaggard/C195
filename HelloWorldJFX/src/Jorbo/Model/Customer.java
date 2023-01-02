package Jorbo.Model;

import Jorbo.Util.JDBC;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class models the customer objects used throughout the application
 */
public class Customer {

    private Integer customerID;
    private String customerName;
    private Date createDate;
    private String createdBy;
    private String address;
    private String city;
    private String postalCode;
    private String phone;

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    private String country;
    private Date lastUpdate;
    private String lastUpdateBy;


    public Customer(int customerID, String customerName, Date createDate, String createdBy, String address, String city, String postalCode, String phone, String country, Date lastUpdate, String lastUpdateBy) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.phone = phone;
        this.country = country;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    /**
     * This is the constructor used to populate the Customer table
     * @param id customer ID
     * @param name customer Name
     * @param country customer Country
     * @param div customer first division
     * @param address customer address
     * @param phone customer phone
     * @param postal customer postal code
     */
    public Customer(int id, String name, String country, String div, String address, String phone, String postal){
        this.customerID = id;
        this.customerName = name;
        this.country = country;
        this.city = div;
        this.address = address;
        this.phone = phone;
        this.postalCode = postal;
    }

    /**
     * This static method converts a customer name to the
     * respective customer ID
     * @param name customer name
     * @return customer id
     * @throws SQLException
     */
    public static int custNameToID(String name) throws SQLException {
        int ID = 0;
        PreparedStatement ps = JDBC.makeConnection().prepareStatement("SELECT Customer_ID FROM customers WHERE Customer_Name = ?");
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            ID = rs.getInt("Customer_ID");
        }
        return ID;
    }

    /**
     * This static method takes a customer id and returns the customer's name.
     * @param ID customer id
     * @return customer name
     * @throws SQLException
     */
    public static String custIDToName(int ID) throws SQLException {
        String name = "";
        PreparedStatement ps = JDBC.makeConnection().prepareStatement("SELECT Customer_Name FROM customers WHERE Customer_ID = ?");
        ps.setInt(1, ID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            name = rs.getString("Customer_Name");
        }
        return name;
    }

}
