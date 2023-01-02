package Jorbo.Model;

/**
 * This class models the various reports displayed on the reports screen.
 */
public class Report {

    private String month;
    private int newAccount;
    private int genRev;
    private int annRev;
    private int closeAccount;
    private int[][] typesArray;
    private int apptID;
    private String type;
    private String desc;
    private String start;
    private String end;
    private String title;
    private int custID;
    private String userName;
    private String lastUpdate;
    private int totalCreated;

    /**
     * This constructor is used for the types Report.
     * @param month row in table
     * @param newAccount number of this type
     * @param genRev number of this type
     * @param annRev number of this type
     * @param closeAccount number of this type
     */
    public Report(String month, int newAccount, int genRev, int annRev, int closeAccount){
        this.newAccount = newAccount;
        this.genRev = genRev;
        this.annRev = annRev;
        this.closeAccount = closeAccount;
        this.month = month;
    }

    public Report(String month, int[][]typesArray){
        this.month = month;
        this.typesArray = typesArray;
    }

    /**
     * this constructor is used for the third report
     * @param user row of table
     * @param lastUpdate timestamp of last update
     * @param totalCreated total # of existing records created
     */
    public Report(String user, String lastUpdate, int totalCreated){
        this.userName = user;
        this.lastUpdate = lastUpdate;
        this.totalCreated = totalCreated;
    }
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getNewAccount() {
        return newAccount;
    }

    public void setNewAccount(int newAccount) {
        this.newAccount = newAccount;
    }

    public int getGenRev() {
        return genRev;
    }

    public void setGenRev(int genRev) {
        this.genRev = genRev;
    }

    public int getAnnRev() {
        return annRev;
    }

    public void setAnnRev(int annRev) {
        this.annRev = annRev;
    }

    public int getCloseAccount() {
        return closeAccount;
    }

    public void setCloseAccount(int closeAccount) {
        this.closeAccount = closeAccount;
    }

    public int[][] getTypesArray() {
        return typesArray;
    }

    public void setTypesArray(int[][] typesArray) {
        this.typesArray = typesArray;
    }

    public int getApptID() {
        return apptID;
    }

    public void setApptID(int apptID) {
        this.apptID = apptID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCustID() {
        return custID;
    }

    public void setCustID(int custID) {
        this.custID = custID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getTotalCreated() {
        return totalCreated;
    }

    public void setTotalCreated(int totalCreated) {
        this.totalCreated = totalCreated;
    }
}
