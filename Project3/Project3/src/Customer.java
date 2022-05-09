public class Customer {

    // ------
    // Fields

    private String email;
    private String address;
    private int customerID;
    private String username;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String paymentMethod;

    // -------
    // Getters

    public String getEmail() {
        return email;
    }
    public String getAddress() {
        return address;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getFullName() {
        return fullName;
    }
    public int getCustomerID() {
        return customerID;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }

    // -------
    // Setters

    public void setEmail(String email) {
        this.email = email;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
