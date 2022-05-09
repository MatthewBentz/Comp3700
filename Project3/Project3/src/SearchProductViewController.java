import javax.swing.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class SearchProductViewController
{
    // ---------
    // UI Fields

    private JTable productsTable;
    private JButton searchButton;
    private JTextField queryTextField;
    private DefaultTableModel tableModel;

    private JPanel mainPanel;

    public SearchProductViewController(Client client)
    {
        tableModel = new DefaultTableModel();
        productsTable.setModel(tableModel);

        searchButton.addActionListener(e ->
        {
            String productName = queryTextField.getText();
            Message searchMessage = new Message(Message.SEARCH_REQUEST, productName);
            client.sendMessage(searchMessage);
        });
    }

    public void updateResultsTable(ArrayList<Product> productList)
    {
        String[] columnNames = new String[] { "ID", "Name", "Price", "Quantity" };
        tableModel.setColumnIdentifiers(columnNames);

        int row = productList.size();
        int col = 4;

        String[][] data = new String[row][col];

        for (int i = 0; i < row; i++)
        {
            data[i][0] = String.valueOf(productList.get(i).getProductID());
            data[i][1] = productList.get(i).getName();
            data[i][2] = String.valueOf(productList.get(i).getPrice());
            data[i][3] = String.valueOf(productList.get(i).getQuantity());
            tableModel.addRow(data[i]);
        }
    }

    public JPanel getMainPanel() { return mainPanel; }
}
