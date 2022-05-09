import java.awt.*;
import javax.swing.*;
import java.net.Socket;
import java.util.Base64;
import java.util.ArrayList;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

import java.net.InetAddress;
import javax.crypto.SecretKey;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Client
{
    private JButton manageInfoButton;
    private JButton manageOrderButton;
    private JButton createOrderButton;
    private JTextArea messageTextArea;
    private JButton searchProductButton;

    private SecretKey secretKey;
    private byte[] initializationVector;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    private Gson gson;

    private OrderViewController orderViewController;
    private LoginViewController loginViewController;
    private SignUpViewController signUpViewController;
    private ManagerViewController managerViewController;
    private EditInfoViewController editInfoViewController;
    private SearchProductViewController searchProductViewController;
    private OrderHistoryViewController orderHistoryViewController;
    private CreateNewOrderViewController createNewOrderViewController;

    private JPanel mainPanel;
    private JFrame mainFrame;
    private User user;

    public Client()
    {
        // -------------------------------
        // Set up connection to the server

        try
        {
            Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), 12002);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            // -------------------
            // Send the secret key

            secretKey = KeyService.createAESKey();
            String keyString = KeyService.convertSecretKeyToString(secretKey);
            dataOutputStream.writeUTF(keyString);

            // ------------------------------
            // Send the initialization vector

            initializationVector = KeyService.createInitializationVector();
            String vectorString = Base64.getEncoder().encodeToString(initializationVector);
            dataOutputStream.writeUTF(vectorString);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        gson = new Gson();

        Worker worker = new Worker();
        Thread workerThread = new Thread(worker);
        workerThread.start();

        // ------------------------------------------------------
        // Create action listeners / view controllers for buttons

        this.createNewOrderViewController = new CreateNewOrderViewController(this);
        createOrderButton.addActionListener(e ->
        {
            JFrame frame = new JFrame("Create Order");
            frame.setContentPane(createNewOrderViewController.getMainPanel());
            frame.setMinimumSize(new Dimension(800, 400));
            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });

        // ------------------------------------------------

        this.searchProductViewController = new SearchProductViewController(this);
        searchProductButton.addActionListener(e ->
        {
            JFrame frame = new JFrame("Search Products");
            frame.setContentPane(searchProductViewController.getMainPanel());
            frame.setMinimumSize(new Dimension(800, 400));
            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });

        // ------------------------------------------------

        this.editInfoViewController = new EditInfoViewController(this);
        manageInfoButton.addActionListener(e ->
        {
            JFrame frame = new JFrame("Edit Account Info");
            frame.setContentPane(editInfoViewController.getMainPanel());
            frame.setMinimumSize(new Dimension(800, 400));
            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);

            // ----------------------
            // Send current user info

            Message loadMessage = new Message(Message.LOAD_CUSTOMER, String.valueOf(user.getUserID()));
            sendMessage(loadMessage);
        });

        // ------------------------------------------------

        this.orderHistoryViewController = new OrderHistoryViewController(this);
        manageOrderButton.addActionListener(e ->
        {
            JFrame frame = new JFrame("Manage Order History");
            frame.setContentPane(orderHistoryViewController.getMainPanel());
            frame.setMinimumSize(new Dimension(800, 400));
            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);

            // --------------------
            // Load customer orders

            Message loadMessage = new Message(Message.LOAD_CUSTOMER_ORDERS, String.valueOf(user.getUserID()));
            sendMessage(loadMessage);
        });

        this.loginViewController = new LoginViewController(this);
        this.mainFrame = new JFrame("Main Window");
    }

    // ------------------------------------------------

    public void sendMessage(Message message)
    {
        String str = gson.toJson(message);
        try
        {
            byte[] cipherText = KeyService.do_AESEncryption(str, secretKey, initializationVector);
            String cipherTextString = Base64.getEncoder().encodeToString(cipherText);
            dataOutputStream.writeUTF(cipherTextString);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // ------------------------------------------------

    private void processMessage(Message message)
    {
        messageTextArea.append(gson.toJson(message) + "\n");

        switch (message.getId())
        {
            case Message.LOAD_PRODUCT_REPLY:
            {
                Product product = gson.fromJson(message.getContent(), Product.class);
                managerViewController.updateProductInfo(product);
                break;
            }

            case Message.LOAD_PRODUCT_REPLY_TO_ADD:
            {
                Product product = gson.fromJson(message.getContent(), Product.class);
                createNewOrderViewController.updateOrder(product);
                break;
            }

            case Message.LOAD_CUSTOMER_REPLY:
            {
                Customer customer = gson.fromJson(message.getContent(), Customer.class);
                editInfoViewController.updateCustomerInfo(customer);
                break;
            }

            case Message.LOAD_ORDER_REPLY:
            {
                Order order = gson.fromJson(message.getContent(), Order.class);
                orderViewController.updateOrderInfo(order);
                break;
            }

            case Message.LOGIN_RESPONSE_SUCCESS:
            {
                User user = gson.fromJson(message.getContent(), User.class);
                System.out.println(user);
                this.user = user;

                if (user.getIsManager())
                {
                    this.managerViewController = new ManagerViewController(this);
                    this.mainFrame.setTitle("Manage Product");
                    this.mainFrame.setContentPane(managerViewController.getMainPanel());
                    managerViewController.getMainPanel().updateUI();
                }
                else
                {
                    this.mainFrame.setTitle("Customer View");
                    this.mainFrame.setContentPane(this.mainPanel);
                    this.mainPanel.updateUI();
                }

                break;
            }

            case Message.SIGNUP_RESPONSE:
            {
                this.signUpViewController = new SignUpViewController(this);
                this.mainFrame.setTitle("Create Account");
                this.mainFrame.setContentPane(signUpViewController.getMainPanel());
                signUpViewController.getMainPanel().updateUI();
                break;
            }

            case Message.SIGNUP_RESPONSE_FAIL:
            {
                JOptionPane.showMessageDialog(null, "Error creating your account. Please try again.");
                break;
            }

            case Message.SIGNUP_RESPONSE_SUCCESS:
            {
                JOptionPane.showMessageDialog(null, "Account Created.");
                this.loginViewController = new LoginViewController(this);
                this.mainFrame.setTitle("Login");
                this.mainFrame.setContentPane(loginViewController.getMainPanel());
                loginViewController.getMainPanel().updateUI();
                break;
            }

            case Message.SEARCH_RESPONSE:
            {
                Type listType = new TypeToken<ArrayList<Product>>(){}.getType();
                ArrayList<Product> productList = gson.fromJson(message.getContent(), listType);
                searchProductViewController.updateResultsTable(productList);
                break;
            }

            case Message.DELETE_RESPONSE_SUCCESS:
            {
                JOptionPane.showMessageDialog(null, "Order has been cancelled. Please refresh.");
                break;
            }

            case Message.LOAD_CUSTOMER_ORDERS_REPLY:
            {
                Type listType = new TypeToken<ArrayList<Order>>(){}.getType();
                ArrayList<Order> orderList = gson.fromJson(message.getContent(), listType);
                orderHistoryViewController.updateTable(orderList);
                break;
            }

            default:
                return;
        }
    }

    // ------------------------------------------------

    private class Worker implements Runnable
    {
        @Override
        public void run()
        {
            while (true)
            {
                String replyString = null;

                try
                {
                    replyString = dataInputStream.readUTF();
                    byte[] decode = Base64.getDecoder().decode(replyString);

                    replyString = KeyService.do_AESDecryption( decode, secretKey, initializationVector);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                Message message = gson.fromJson(replyString, Message.class);
                processMessage(message);
            }
        }
    }

    // ------------------------------------------------

    public User getUser() {
        return user;
    }
    public JFrame getMainFrame() {
        return mainFrame;
    }
    public LoginViewController getLoginViewController() {
        return loginViewController;
    }

    // ------------------------------------------------

    public static void main(String[] args)
    {
        Client client = new Client();
        client.getMainFrame().setTitle("Login");
        client.getMainFrame().setContentPane(client.getLoginViewController().getMainPanel());
        client.getMainFrame().setMinimumSize(new Dimension(800, 400));
        client.getMainFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.getMainFrame().pack();
        client.getMainFrame().setVisible(true);
    }
}
