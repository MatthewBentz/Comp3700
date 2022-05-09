import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainScreen extends JFrame {

    public JButton orderButton = new JButton("Order View");
    public JButton productButton = new JButton("Product View");
    public JButton customerButton = new JButton("Customer View");

    public MainScreen() {

        this.setTitle("Main Screen");
        this.setSize(new Dimension(600, 300));
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(productButton);
        buttonPanel.add(customerButton);
        buttonPanel.add(orderButton);

        productButton.addActionListener(e -> {
            StoreManager.getInstance().getProductView().setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            StoreManager.getInstance().getProductView().setVisible(true);
        });

        orderButton.addActionListener(e -> {
            StoreManager.getInstance().getOrderView().setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            StoreManager.getInstance().getOrderView().setVisible(true);
        });

        customerButton.addActionListener(e -> {
            StoreManager.getInstance().getCustomerView().setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            StoreManager.getInstance().getCustomerView().setVisible(true);
        });

        this.getContentPane().add(buttonPanel);
    }
}
