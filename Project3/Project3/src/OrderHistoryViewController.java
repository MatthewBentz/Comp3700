import javax.swing.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class OrderHistoryViewController
{
    // ---------
    // UI Fields

    private JTable orderTable;
    private JButton cancelButton;
    private JTextField orderIDTF;
    private JButton refreshButton;
    private DefaultTableModel tableModel;
    private JPanel mainPanel;

    public OrderHistoryViewController(Client client)
    {
        tableModel = new DefaultTableModel();
        orderTable.setModel(tableModel);

        cancelButton.addActionListener(e ->
        {
            String orderID = orderIDTF.getText();
            Message deleteMessage = new Message(Message.DELETE_REQUEST, orderID);
            client.sendMessage(deleteMessage);
        });

        refreshButton.addActionListener(e ->
        {
            int customerID = client.getUser().getUserID();
            Message getOrdersMessage = new Message(Message.LOAD_CUSTOMER_ORDERS, String.valueOf(customerID));
            client.sendMessage(getOrdersMessage);
        });
    }

    public void updateTable(ArrayList<Order> orderList)
    {
        // ------------------------------------
        // Delete previous orders after refresh

        /*for (int i = 0; i < tableModel.getRowCount(); i++)
        {
            tableModel.removeRow(i + 1);
        }*/

        // ---------------------------------
        // Re-add orders from refreshed list

        String[] columnNames = new String[]{"ID", "Date", "Cost", "Tax"};
        tableModel.setColumnIdentifiers(columnNames);

        int row = orderList.size();
        int col = 4;

        String[][] data = new String[row][col];

        for (int i = 0; i < row; i++)
        {
            data[i][0] = String.valueOf(orderList.get(i).getOrderID());
            data[i][1] = orderList.get(i).getDate();
            data[i][2] = String.valueOf(orderList.get(i).getTotalCost());
            data[i][3] = String.valueOf(orderList.get(i).getTotalTax());
            tableModel.addRow(data[i]);
        }
    }

    public JPanel getMainPanel() { return mainPanel; }
}
