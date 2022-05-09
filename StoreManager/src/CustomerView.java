import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerView extends JFrame {

    public JTextField txtCustomerID = new JTextField(30);
    public JTextField txtCustomerName = new JTextField(30);
    public JTextField txtCustomerAddress = new JTextField(30);

    public JButton btnLoad = new JButton("Load");
    public JButton btnSave = new JButton("Save");

    public CustomerView() {

        this.setTitle("Customer View");
        this.setSize(new Dimension(600, 300));
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel line1 = new JPanel();
        line1.add(new JLabel("Customer ID"));
        line1.add(txtCustomerID);
        this.getContentPane().add(line1);

        JPanel line2 = new JPanel();
        line2.add(new JLabel("Customer Name"));
        line2.add(txtCustomerName);
        this.getContentPane().add(line2);

        JPanel line3 = new JPanel();
        line3.add(new JLabel("Address"));
        line3.add(txtCustomerAddress);
        this.getContentPane().add(line3);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnLoad);
        buttonPanel.add(btnSave);

        btnLoad.addActionListener(e -> {

            int id = Integer.parseInt(txtCustomerID.getText());
            CustomerModel customer = StoreManager.getInstance().getDataAccess().loadCustomer(id);

            txtCustomerName.setText(customer.customerName);
            txtCustomerAddress.setText(customer.address);
        });

        btnSave.addActionListener(e -> {

            CustomerModel customer = new CustomerModel();
            customer.address = txtCustomerAddress.getText();
            customer.customerName = txtCustomerName.getText();
            customer.customerID = Integer.parseInt(txtCustomerID.getText());

            StoreManager.getInstance().getDataAccess().saveCustomer(customer);
        });

        this.getContentPane().add(buttonPanel);
    }
}
