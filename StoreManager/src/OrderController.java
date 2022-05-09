import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrderController implements ActionListener {

    OrderView myView;
    DataAccess myDAO;

    public OrderController(OrderView view, DataAccess dao) {
        myView = view;
        myDAO = dao;
        myView.btnLoad.addActionListener(this);
        myView.btnSave.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == myView.btnLoad) {
            loadOrderAndDisplay();
        }

        if (e.getSource() == myView.btnSave) {
            saveOrder();
        }
    }

    private void saveOrder() {
        OrderModel orderModel = new OrderModel();

        try {
            orderModel.orderDate = myView.txtOrderDate.getText();
            orderModel.customerName = myView.txtCustomerName.getText();
            orderModel.orderID = Integer.parseInt(myView.txtOrderID.getText());
            orderModel.totalTax = Double.parseDouble(myView.txtTotalTax.getText());
            orderModel.totalCost = Double.parseDouble(myView.txtTotalCost.getText());

            myDAO.saveOrder(orderModel);
            JOptionPane.showMessageDialog(null, "Order saved successfully!");
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid format for OrderID");
            ex.printStackTrace();
        }
    }

    private void loadOrderAndDisplay() {
        try {
            int orderID = Integer.parseInt(myView.txtOrderID.getText());
            OrderModel orderModel = myDAO.loadOrder(orderID);

            myView.txtOrderDate.setText(orderModel.orderDate);
            myView.txtCustomerName.setText(orderModel.customerName);
            myView.txtTotalTax.setText(String.valueOf(orderModel.totalTax));
            myView.txtTotalCost.setText(String.valueOf(orderModel.totalCost));
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid format for OrderID");
            ex.printStackTrace();
        }
    }
}
