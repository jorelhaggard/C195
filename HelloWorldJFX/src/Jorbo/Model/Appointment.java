package Jorbo.Model;

import java.security.Timestamp;
import java.sql.Date;
import java.sql.SQLException;

/**
 * This class models my various appointment objects
 */
public class Appointment {

    private int appointmentID;
    private int customerID;
    private int userID;
    private Customer customer;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String startTime;
    private String endTime;
    private String customerName;
    private Date createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Here is the constructor used in the upcoming appointment alert.
     * @param id apptID
     * @param start startTime
     */
    public Appointment(int id, String start){
        this.appointmentID = id;
        this.startTime = start;
    };

    /**
     * This is the constructor used to populate the main Appointment table
     * @param apptid appointmentID
     * @param custyid customerID
     * @param title Title
     * @param type Type
     * @param desc Description
     * @param location Location
     * @param contact Contact
     * @param start Start
     * @param end End
     * @param user User
     * @throws SQLException Just in case
     */
    public Appointment(int apptid, int custyid, String title, String type, String desc, String location, String contact, String start, String end, int user) throws SQLException {
    this.appointmentID = apptid;
    this.customerID = custyid;
    this.title = title;
    this.type = type;
    this.description = desc;
    this.location = location;
    this.startTime = start;
    this.endTime = end;
    this.userID = user;
    this.contact = contact;
    }

    /**
     * This is the constructor used in the second Report.
     * @param apptid appointmentID
     * @param type Type
     * @param desc Description
     * @param start Start Time
     * @param end End Time
     * @param title Title
     * @param custid Customer ID
     */
    public Appointment(int apptid, String type, String desc, String start, String end, String title, int custid){
        this.appointmentID = apptid;
        this.type = type;
        this.description = desc;
        this.startTime = start;
        this.endTime = end;
        this.title = title;
        this.customerID = custid;
    }
}
