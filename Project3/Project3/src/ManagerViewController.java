import javax.swing.*;
import com.google.gson.Gson;

public class ManagerViewController
{
    // ------------------------
    // UI Text Fields & Buttons

    private JButton loadProductButton;
    private JButton saveProductButton;

    private JTextField productIDTF;
    private JTextField productNameTF;
    private JTextField productPriceTF;
    private JTextField productQuantityTF;

    private JPanel mainPanel;

    public ManagerViewController(Client client)
    {
        loadProductButton.addActionListener(e ->
        {
            String productID = productIDTF.getText();
            Message message = new Message(Message.LOAD_PRODUCT, productID);
            client.sendMessage(message);
        });

        saveProductButton.addActionListener(e ->
        {
            // ---------------------
            // Create Product object

            Product product = new Product();
            product.setProductID(Integer.parseInt(productIDTF.getText()));
            product.setName(productNameTF.getText());
            product.setPrice(Double.parseDouble(productPriceTF.getText()));
            product.setQuantity(Double.parseDouble(productQuantityTF.getText()));

            // ------------------------------
            // Send to server to save product

            Gson gson = new Gson();
            String productString = gson.toJson(product);
            Message message = new Message(Message.SAVE_PRODUCT, productString);
            client.sendMessage(message);
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void updateProductInfo(Product product)
    {
        productIDTF.setText(String.valueOf(product.getProductID()));
        productNameTF.setText(product.getName());
        productPriceTF.setText(String.valueOf(product.getPrice()));
        productQuantityTF.setText(String.valueOf(product.getQuantity()));
    }
}
