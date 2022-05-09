public class User
{
    private int userID;
    private String username;
    private String password;
    private String displayName;
    private boolean isManager;

    // ---------------
    // Getters/Setters

    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean getIsManager() {return isManager;}
    public void setIsManager(boolean isManager) {this.isManager = isManager; }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", displayName='" + displayName + '\'' +
                ", isManager=" + isManager +
                '}';
    }
}
