import javax.swing.*;
import com.google.gson.Gson;

public class OrderViewController
{
    // ------------------------
    // UI Text Fields & Buttons

    private JButton loadOrderButton;
    private JButton saveOrderButton;

    private JTextField orderIDTF;
    private JTextField totalTaxTF;
    private JTextField orderDateTF;
    private JTextField totalCostTF;
    private JTextField customerIDTF;

    private JPanel mainPanel;

    public OrderViewController(Client client)
    {
        loadOrderButton.addActionListener(e ->
        {
            String orderID = orderIDTF.getText();
            Message message = new Message(Message.LOAD_ORDER, orderID);
            client.sendMessage(message);
        });

        saveOrderButton.addActionListener(e ->
        {
            // -------------------
            // Create Order object

            Order order = new Order();
            order.setOrderID(Integer.parseInt(orderIDTF.getText()));
            order.setDate(orderDateTF.getText());
            order.setCustomerID(Integer.parseInt(customerIDTF.getText()));
            order.setTotalCost(Double.parseDouble(totalCostTF.getText()));
            order.setTotalTax(Double.parseDouble(totalTaxTF.getText()));

            // ----------------------------
            // Send to server to save order

            Gson gson = new Gson();
            String orderString = gson.toJson(order);
            Message message = new Message(Message.SAVE_ORDER, orderString);
            client.sendMessage(message);
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void updateOrderInfo(Order order)
    {
        orderIDTF.setText(String.valueOf(order.getOrderID()));
        orderDateTF.setText(order.getDate());
        customerIDTF.setText(String.valueOf(order.getCustomerID()));
        totalCostTF.setText(String.valueOf(order.getTotalCost()));
        totalTaxTF.setText(String.valueOf(order.getTotalTax()));
    }
}
