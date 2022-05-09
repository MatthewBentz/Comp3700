import com.google.gson.Gson;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomersViewController {
    private JTextField customerIDTF;
    private JTextField customerNameTF;
    private JTextField customerPhoneTF;
    private JButton loadCustomerButton;
    private JButton saveCustomerButton;
    private JPanel mainPanel;

    private Client client;

    public CustomersViewController(Client client) {
        this.client = client;

        loadCustomerButton.addActionListener(e -> {
            String customerID = customerIDTF.getText();
            Message message = new Message(Message.LOAD_CUSTOMER, customerID);
            client.sendMessage(message);
        });

        saveCustomerButton.addActionListener(e -> {
            Customer customer = new Customer();

            customer.setCustomerID(customerIDTF.getText());
            customer.setCustomerName(customerNameTF.getText());
            customer.setPhoneNumber(customerPhoneTF.getText());

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
        customerIDTF.setText(customer.getCustomerID());
        customerNameTF.setText(customer.getCustomerName());
        customerPhoneTF.setText(customer.getPhoneNumber());
    }
}
