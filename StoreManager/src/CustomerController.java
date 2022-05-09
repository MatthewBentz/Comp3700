import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerController implements ActionListener {

    CustomerView myView;
    DataAccess myDAO;

    public CustomerController(CustomerView view, DataAccess dao) {
        myView = view;
        myDAO = dao;
        myView.btnLoad.addActionListener(this);
        myView.btnSave.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == myView.btnLoad) {
            loadCustomerAndDisplay();
        }

        if (e.getSource() == myView.btnSave) {
            saveCustomer();
        }
    }

    private void saveCustomer() {
        CustomerModel customerModel = new CustomerModel();

        try {
            customerModel.address = myView.txtCustomerAddress.getText();
            customerModel.customerName = myView.txtCustomerName.getText();
            customerModel.customerID = Integer.parseInt(myView.txtCustomerID.getText());

            myDAO.saveCustomer(customerModel);
            JOptionPane.showMessageDialog(null, "Customer saved successfully!");
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid format for CustomerID");
            ex.printStackTrace();
        }
    }

    private void loadCustomerAndDisplay() {
        try {
            int customerID = Integer.parseInt(myView.txtCustomerID.getText());
            CustomerModel customerModel = myDAO.loadCustomer(customerID);

            myView.txtCustomerAddress.setText(customerModel.address);
            myView.txtCustomerName.setText(customerModel.customerName);
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid format for CustomerID");
            ex.printStackTrace();
        }
    }
}
