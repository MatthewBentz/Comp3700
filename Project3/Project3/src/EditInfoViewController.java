import javax.swing.*;
import com.google.gson.Gson;

public class EditInfoViewController
{
    // ------------------------
    // UI Text Fields & Buttons

    private JTextField emailTF;
    private JTextField addressTF;
    private JTextField paymentTF;
    private JTextField usernameTF;
    private JTextField passwordTF;
    private JTextField fullNameTF;
    private JTextField customerIDTF;
    private JTextField phoneNumberTF;
    private JButton saveCustomerButton;

    private JPanel mainPanel;

    public EditInfoViewController(Client client)
    {
        this.customerIDTF.setEditable(false);

        saveCustomerButton.addActionListener(e ->
        {
            // ----------------------------------
            // Create customer object with fields

            Customer customer = new Customer();
            customer.setCustomerID(Integer.parseInt(customerIDTF.getText()));
            customer.setUsername(usernameTF.getText());
            customer.setPassword(passwordTF.getText());
            customer.setFullName(fullNameTF.getText());
            customer.setEmail(emailTF.getText());
            customer.setPaymentMethod(paymentTF.getText());
            customer.setAddress(addressTF.getText());
            customer.setPhoneNumber(phoneNumberTF.getText());

            // -----------------------------
            // Send object to server to save

            Gson gson = new Gson();
            String customerString = gson.toJson(customer);
            Message message = new Message(Message.SAVE_CUSTOMER, customerString);
            client.sendMessage(message);
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void updateCustomerInfo(Customer customer) {
        customerIDTF.setText(String.valueOf(customer.getCustomerID()));
        usernameTF.setText(customer.getUsername());
        passwordTF.setText(customer.getPassword());
        fullNameTF.setText(customer.getFullName());
        emailTF.setText(customer.getEmail());
        paymentTF.setText(customer.getPaymentMethod());
        addressTF.setText(customer.getAddress());
        phoneNumberTF.setText(customer.getPhoneNumber());
    }
}
