import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductView extends JFrame {

    public JTextField txtProductID = new JTextField(30);
    public JTextField txtProductName = new JTextField(30);
    public JTextField txtProductPrice = new JTextField(30);
    public JTextField txtProductQuantity = new JTextField(30);

    public JButton btnLoad = new JButton("Load");
    public JButton btnSave = new JButton("Save");

    public ProductView() {

        this.setTitle("Product View");
        this.setSize(new Dimension(600, 300));
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel line1 = new JPanel();
        line1.add(new JLabel("Product ID"));
        line1.add(txtProductID);
        this.getContentPane().add(line1);

        JPanel line2 = new JPanel();
        line2.add(new JLabel("Product Name"));
        line2.add(txtProductName);
        this.getContentPane().add(line2);

        JPanel line3 = new JPanel();
        line3.add(new JLabel("Price"));
        line3.add(txtProductPrice);
        this.getContentPane().add(line3);

        JPanel line4 = new JPanel();
        line4.add(new JLabel("Quantity"));
        line4.add(txtProductQuantity);
        this.getContentPane().add(line4);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnLoad);
        buttonPanel.add(btnSave);

        btnLoad.addActionListener(e -> {

            int id = Integer.parseInt(txtProductID.getText());
            ProductModel product = StoreManager.getInstance().getDataAccess().loadProduct(id);

            txtProductName.setText(product.name);
            txtProductPrice.setText(String.valueOf(product.price));
            txtProductQuantity.setText(String.valueOf(product.quantity));
        });

        btnSave.addActionListener(e -> {

            ProductModel product = new ProductModel();
            product.name = txtProductName.getText();
            product.productID = Integer.parseInt(txtProductID.getText());
            product.price = Double.parseDouble(txtProductPrice.getText());
            product.quantity = Double.parseDouble(txtProductQuantity.getText());

            StoreManager.getInstance().getDataAccess().saveProduct(product);
        });

        this.getContentPane().add(buttonPanel);
    }
}
