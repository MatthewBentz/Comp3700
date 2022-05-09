import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrderView extends JFrame {

    public JTextField txtOrderID = new JTextField(30);
    public JTextField txtOrderDate = new JTextField(30);
    public JTextField txtCustomerName = new JTextField(30);
    public JTextField txtTotalCost = new JTextField(30);
    public JTextField txtTotalTax = new JTextField(30);

    public JButton btnLoad = new JButton("Load");
    public JButton btnSave = new JButton("Save");

    public OrderView() {

        this.setTitle("Order View");
        this.setSize(new Dimension(600, 300));
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel line1 = new JPanel();
        line1.add(new JLabel("Order ID"));
        line1.add(txtOrderID);
        this.getContentPane().add(line1);

        JPanel line2 = new JPanel();
        line2.add(new JLabel("Order Date"));
        line2.add(txtOrderDate);
        this.getContentPane().add(line2);

        JPanel line3 = new JPanel();
        line3.add(new JLabel("Customer Name"));
        line3.add(txtCustomerName);
        this.getContentPane().add(line3);

        JPanel line4 = new JPanel();
        line4.add(new JLabel("TotalCost"));
        line4.add(txtTotalCost);
        this.getContentPane().add(line4);

        JPanel line5 = new JPanel();
        line5.add(new JLabel("TotalTax"));
        line5.add(txtTotalTax);
        this.getContentPane().add(line5);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnLoad);
        buttonPanel.add(btnSave);

        btnLoad.addActionListener(e -> {

            int id = Integer.parseInt(txtOrderID.getText());
            OrderModel order = StoreManager.getInstance().getDataAccess().loadOrder(id);

            txtOrderDate.setText(order.orderDate);
            txtCustomerName.setText(order.customerName);
            txtTotalTax.setText(String.valueOf(order.totalTax));
            txtTotalCost.setText(String.valueOf(order.totalCost));
        });

        btnSave.addActionListener(e -> {

            OrderModel order = new OrderModel();
            order.orderDate = txtOrderDate.getText();
            order.customerName = txtCustomerName.getText();
            order.orderID = Integer.parseInt(txtOrderID.getText());
            order.totalTax = Double.parseDouble(txtTotalTax.getText());
            order.totalCost = Double.parseDouble(txtTotalCost.getText());

            StoreManager.getInstance().getDataAccess().saveOrder(order);
        });

        this.getContentPane().add(buttonPanel);
    }
}
