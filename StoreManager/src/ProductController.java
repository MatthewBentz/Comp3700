import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductController implements ActionListener {

    ProductView myView;
    DataAccess myDAO;

    public ProductController(ProductView view, DataAccess dao) {
        myView = view;
        myDAO = dao;
        myView.btnLoad.addActionListener(this);
        myView.btnSave.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == myView.btnLoad) {
            loadProductAndDisplay();
        }

        if (e.getSource() == myView.btnSave) {
            saveProduct();
        }
    }

    private void saveProduct() {
        ProductModel productModel = new ProductModel();

        try {
            productModel.productID = Integer.parseInt(myView.txtProductID.getText());
            productModel.name = myView.txtProductName.getText();
            productModel.price = Double.parseDouble(myView.txtProductPrice.getText());
            productModel.quantity = Double.parseDouble(myView.txtProductQuantity.getText());

            myDAO.saveProduct(productModel);
            JOptionPane.showMessageDialog(null, "Product saved successfully!");
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid format for ProductID");
            ex.printStackTrace();
        }
    }

    private void loadProductAndDisplay() {
        try {
            int productID = Integer.parseInt(myView.txtProductID.getText());
            ProductModel productModel = myDAO.loadProduct(productID);

            myView.txtProductName.setText(productModel.name);
            myView.txtProductPrice.setText(String.valueOf(productModel.price));
            myView.txtProductQuantity.setText(String.valueOf(productModel.quantity));
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid format for ProductID");
            ex.printStackTrace();
        }
    }
}
