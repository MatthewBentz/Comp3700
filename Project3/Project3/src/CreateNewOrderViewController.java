import javax.swing.*;
import java.util.Random;
import com.google.gson.Gson;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateNewOrderViewController
{
    private final Client client;
    private JTextField orderIDTF;
    private JTextField productTF;
    private JTextField quantityTF;
    private JButton addProductButton;
    private JButton createOrderButton;
    private JList orderLineList;
    private JPanel mainPanel;

    private DefaultListModel<OrderLine> listModel;
    private Order order;

    public CreateNewOrderViewController(Client client) {
        this.client = client;

        // -----------------
        // Set orderID in UI

        int orderId = new Random().nextInt(100000);
        this.orderIDTF.setText(""+ orderId);
        this.orderIDTF.setEditable(false);

        // ---------------------------
        // Create order object to save

        this.order = new Order();
        order.setOrderID(orderId);

        listModel = new DefaultListModel<>();

        this.orderLineList.setModel(listModel);

        // ------------------------------------------------

        addProductButton.addActionListener(e ->
        {
            String productID = productTF.getText();
            Message message = new Message(Message.LOAD_PRODUCT_TO_ADD, productID);
            client.sendMessage(message);
        });

        // ------------------------------------------------

        createOrderButton.addActionListener(e ->
        {
            double totalTax = order.getTotalCost()*0.09;
            order.setTotalTax(totalTax);
            order.setDate("04/30/22");
            order.setCustomerID(client.getUser().getUserID());

            Message message = new Message(Message.SAVE_ORDER, new Gson().toJson(order));
            client.sendMessage(message);

            for (int i = 0; i < listModel.getSize(); i++)
            {
                OrderLine element = listModel.getElementAt(i);
                Message orderLineMessage = new Message(Message.SAVE_ORDER_LINE, new Gson().toJson(element));
                client.sendMessage(orderLineMessage);
            }
        });
    }

    // ------------------------------------------------

    public void updateOrder(Product product)
    {
        int quantity = Integer.parseInt(this.quantityTF.getText());
        double totalPrice = quantity*product.getPrice();
        product.setQuantity(product.getQuantity()-quantity);

        Message message = new Message(Message.SAVE_PRODUCT, new Gson().toJson(product));
        client.sendMessage(message);

        OrderLine orderLine = new OrderLine();
        orderLine.setOrderID(order.getOrderID());
        orderLine.setQuantity(quantity);
        orderLine.setProductID(product.getProductID());
        orderLine.setCost(totalPrice);
        listModel.addElement(orderLine);
        this.order.setTotalCost(this.order.getTotalCost()+totalPrice);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }
}
