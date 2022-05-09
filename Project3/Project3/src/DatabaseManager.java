import java.sql.*;
import java.util.ArrayList;
import com.google.gson.Gson;

public class DatabaseManager
{
    private static DatabaseManager databaseManager;
    public static DatabaseManager getInstance() {
        if (databaseManager == null)
        {
            databaseManager = new DatabaseManager();
        }
        return databaseManager;
    }

    private Connection connection;

    private DatabaseManager()
    {
        try
        {
            connection = DriverManager.getConnection("jdbc:sqlite:data/store.db");
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public Message process(String requestString)
    {
        Gson gson = new Gson();
        Message message = gson.fromJson(requestString, Message.class);

        switch (message.getId())
        {
            case Message.LOAD_PRODUCT:
            {
                Product product = loadProduct(Integer.parseInt(message.getContent()));
                return new Message(Message.LOAD_PRODUCT_REPLY, gson.toJson(product));
            }

            case Message.LOAD_PRODUCT_TO_ADD:
            {
                Product product = loadProduct(Integer.parseInt(message.getContent()));
                return new Message(Message.LOAD_PRODUCT_REPLY_TO_ADD, gson.toJson(product));
            }

            case Message.SAVE_PRODUCT:
            {
                Product product = gson.fromJson(message.getContent(), Product.class);
                boolean retVal = saveProduct(product);
                if (retVal) return new Message(Message.SUCCESS, "Product saved");
                else return new Message(Message.FAIL, "Cannot save the product");
            }

            case Message.LOAD_CUSTOMER:
            {
                Customer customer = loadCustomer(Integer.parseInt(message.getContent()));
                return new Message(Message.LOAD_CUSTOMER_REPLY, gson.toJson(customer));
            }

            case Message.SAVE_CUSTOMER:
            {
                Customer customer = gson.fromJson(message.getContent(), Customer.class);
                boolean retVal = saveCustomer(customer);
                if (retVal) return new Message(Message.SUCCESS, "Customer saved");
                else return new Message(Message.FAIL, "Cannot save the customer");
            }

            case Message.LOAD_ORDER:
            {
                Order order = loadOrder(Integer.parseInt(message.getContent()));
                return new Message(Message.LOAD_ORDER_REPLY, gson.toJson(order));
            }

            case Message.SAVE_ORDER:
            {
                Order order = gson.fromJson(message.getContent(), Order.class);
                boolean retVal = saveOrder(order);
                if (retVal) return new Message(Message.SUCCESS, "Order saved");
                else return new Message(Message.FAIL, "Cannot save the order");
            }

            case Message.LOGIN:
            {
                System.out.println("Received a login message");
                System.out.println(message);
                return new Message(Message.SUCCESS, "Account logged in");
            }

            case Message.LOGIN_REQUEST:
            {
                UserInfo userInfo = gson.fromJson(message.getContent(), UserInfo.class);

                User user = loadUser(userInfo.getUsername());
                if (user == null)
                {
                    return new Message(Message.LOGIN_RESPONSE_NO_USERNAME, "This username doesn't exist.");
                }
                else
                {
                    if (!user.getPassword().equals(userInfo.getPassword()))
                    {
                        return new Message(Message.LOGIN_RESPONSE_FAIL_PASSWORD, "The password is incorrect.");
                    }
                    else
                    {
                        return new Message(Message.LOGIN_RESPONSE_SUCCESS, gson.toJson(user));
                    }
                }
            }

            case Message.SIGNUP:
            {
                System.out.println("Sign up request received.");
                System.out.println(message);
                return new Message(Message.SIGNUP_RESPONSE, "Opening create new account window.");
            }

            case Message.SIGNUP_REQUEST:
            {
                System.out.println("Received a create account request message.");
                System.out.println(message);
                UserInfo userInfo = gson.fromJson(message.getContent(), UserInfo.class);

                boolean retVal = createUser(userInfo);
                if (retVal)
                {
                    return new Message(Message.SIGNUP_RESPONSE_SUCCESS, "New user created.");
                }
                else
                {
                    return new Message(Message.SIGNUP_RESPONSE_FAIL, "Error creating new user.");
                }
            }

            case Message.SEARCH_REQUEST:
            {
                System.out.println("Search request received.");
                String keyword = message.getContent();
                ArrayList<Product> productList = searchProducts(keyword);

                return new Message(Message.SEARCH_RESPONSE, gson.toJson(productList));
            }

            case Message.DELETE_REQUEST:
            {
                System.out.println("Delete order request received.");
                String orderID = message.getContent();
                boolean retVal = deleteOrder(orderID);

                return new Message(Message.DELETE_RESPONSE_SUCCESS, String.valueOf(retVal));
            }

            case Message.LOAD_CUSTOMER_ORDERS:
            {
                System.out.println("Retrieving new order table.");
                String customerID = message.getContent();
                ArrayList<Order> orderList = loadCustomerOrders(customerID);

                return new Message(Message.LOAD_CUSTOMER_ORDERS_REPLY, gson.toJson(orderList));
            }

            default:
                return new Message(Message.FAIL, "Cannot process the message");
        }
    }

    // ----------------------------------------------------

    private ArrayList<Order> loadCustomerOrders(String customerID)
    {
        ArrayList<Order> retVal = new ArrayList<>();

        try {
            String query = "SELECT * FROM Orders WHERE CustomerID = " + customerID;

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Order order = new Order();
                order.setOrderID(resultSet.getInt(1));
                order.setDate(resultSet.getString(2));
                order.setTotalCost(resultSet.getDouble(4));
                order.setTotalTax(resultSet.getDouble(5));

                retVal.add(order);
            }

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }

        return retVal;
    }

    // ----------------------------------------------------

    private boolean deleteOrder(String orderID)
    {
        try
        {
            String query = "DELETE FROM Orders WHERE OrderID = " + orderID;
            Statement statement = connection.createStatement();
            statement.execute(query);
            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Database access error!");
            e.printStackTrace();
            return false;
        }
    }

    // ----------------------------------------------------

    private ArrayList<Product> searchProducts(String keyword)
    {
        ArrayList<Product> retVal = new ArrayList<>();

        try
        {
            String query = "SELECT * FROM Products WHERE Name LIKE \'%" + keyword + "%\'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next())
            {
                Product product = new Product();
                product.setProductID(resultSet.getInt(1));
                product.setName(resultSet.getString(2));
                product.setPrice(resultSet.getDouble(3));
                product.setQuantity(resultSet.getDouble(4));
                retVal.add(product);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Database access error!");
            e.printStackTrace();
        }

        return retVal;
    }

    // ----------------------------------------------------

    private User loadUser(String username)
    {
        try
        {
            String query = "SELECT * FROM Users WHERE username = '" + username+ "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next())
            {
                User user = new User();
                user.setUserID(resultSet.getInt(1));
                user.setUsername(resultSet.getString(2));
                user.setPassword(resultSet.getString(3));
                user.setDisplayName(resultSet.getString(4));
                user.setIsManager(resultSet.getBoolean(5));
                resultSet.close();
                statement.close();

                return user;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Database access error!");
            e.printStackTrace();
        }

        return null;
    }

    // ----------------------------------------------------

    private boolean createUser(UserInfo userInfo)
    {
        try
        {
            // ---------------------------------
            // Check user does not already exist

            if (loadUser(userInfo.getUsername()) != null)
            {
                return false;
            }

            // ---------------------------
            // Retrieve the highest userID

            String query = "select max(UserID) from Users";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            int maxUser = 0;
            if (resultSet.next())
            {
                maxUser = resultSet.getInt(1);
            }

            // ---------------
            // Create new user

            query = "insert into Users\n" +
                    "values(" + (maxUser + 1) +", \""+ userInfo.getUsername() + "\", \"" + userInfo.getPassword() + "\", \"\", 0)";
            statement = connection.createStatement();
            statement.execute(query);
        }
        catch (SQLException e)
        {
            System.out.println("Error creating new user.");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // ----------------------------------------------------

    public Product loadProduct(int id)
    {
        try
        {
            String query = "SELECT * FROM Products WHERE ProductID = " + id;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next())
            {
                Product product = new Product();
                product.setProductID(resultSet.getInt(1));
                product.setName(resultSet.getString(2));
                product.setPrice(resultSet.getDouble(3));
                product.setQuantity(resultSet.getDouble(4));
                resultSet.close();
                statement.close();

                return product;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Database access error!");
            e.printStackTrace();
        }

        return null;
    }

    // ----------------------------------------------------

    public boolean saveProduct(Product product)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Products WHERE ProductID = ?");
            statement.setInt(1, product.getProductID());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
            {
                statement = connection.prepareStatement("UPDATE Products SET Name = ?, Price = ?, Quantity = ? WHERE ProductID = ?");
                statement.setString(1, product.getName());
                statement.setDouble(2, product.getPrice());
                statement.setDouble(3, product.getQuantity());
                statement.setInt(4, product.getProductID());
            }
            else
            {
                statement = connection.prepareStatement("INSERT INTO Products VALUES (?, ?, ?, ?)");
                statement.setString(2, product.getName());
                statement.setDouble(3, product.getPrice());
                statement.setDouble(4, product.getQuantity());
                statement.setInt(1, product.getProductID());
            }

            statement.execute();
            resultSet.close();
            statement.close();

            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Database access error!");
            e.printStackTrace();
            return false;
        }
    }

    // ----------------------------------------------------

    public Customer loadCustomer(int id)
    {
        try
        {
            String query = "SELECT * FROM Customers WHERE CustomerID = " + id;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next())
            {
                Customer customer = new Customer();
                customer.setCustomerID(resultSet.getInt(1));
                customer.setUsername(resultSet.getString(2));
                customer.setPassword(resultSet.getString(3));
                customer.setFullName(resultSet.getString(4));
                customer.setEmail(resultSet.getString(5));
                customer.setPaymentMethod(resultSet.getString(6));
                customer.setAddress(resultSet.getString(7));
                customer.setPhoneNumber(resultSet.getString(8));
                resultSet.close();
                statement.close();

                return customer;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Database access error!");
            e.printStackTrace();
        }

        return null;
    }

    // ----------------------------------------------------

    public boolean saveCustomer(Customer customer)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Customers WHERE CustomerID = ?");
            statement.setInt(1, customer.getCustomerID());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
            {
                statement = connection.prepareStatement("UPDATE Customers SET Username = ?, Password = ?, FullName = ?, Email = ?, PaymentMethod = ?, Address = ?, PhoneNumber = ? WHERE CustomerID = ?");
                statement.setString(1, customer.getUsername());
                statement.setString(2, customer.getPassword());
                statement.setString(3, customer.getFullName());
                statement.setString(4, customer.getEmail());
                statement.setString(5, customer.getPaymentMethod());
                statement.setString(6, customer.getAddress());
                statement.setString(7, customer.getPhoneNumber());
                statement.setInt(8, customer.getCustomerID());
            }
            else
            {
                statement = connection.prepareStatement("INSERT INTO Customers VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                statement.setString(2, customer.getUsername());
                statement.setString(3, customer.getPassword());
                statement.setString(4, customer.getFullName());
                statement.setString(5, customer.getEmail());
                statement.setString(6, customer.getPaymentMethod());
                statement.setString(7, customer.getAddress());
                statement.setString(8, customer.getPhoneNumber());
                statement.setInt(1, customer.getCustomerID());
            }
            statement.execute();
            resultSet.close();
            statement.close();

            return true;

        }
        catch (SQLException e)
        {
            System.out.println("Database access error!");
            e.printStackTrace();
            return false;
        }
    }

    // ----------------------------------------------------

    public Order loadOrder(int id)
    {
        try
        {
            String query = "SELECT * FROM Orders WHERE OrderID = " + id;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next())
            {
                Order order = new Order();
                order.setOrderID(resultSet.getInt(1));
                order.setDate(resultSet.getString(2));
                order.setCustomerID(resultSet.getInt(3));
                order.setTotalCost(resultSet.getDouble(4));
                order.setTotalTax(resultSet.getDouble(5));
                resultSet.close();
                statement.close();

                return order;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Database access error!");
            e.printStackTrace();
        }

        return null;
    }

    // ----------------------------------------------------

    public boolean saveOrder(Order order)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Orders WHERE OrderID = ?");
            statement.setInt(1, order.getOrderID());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
            {
                statement = connection.prepareStatement("UPDATE Orders SET OrderDate = ?, CustomerID = ?, TotalCost = ?, TotalTax = ?  WHERE OrderID = ?");
                statement.setString(1, order.getDate());
                statement.setInt(2, order.getCustomerID());
                statement.setDouble(3, order.getTotalCost());
                statement.setDouble(4, order.getTotalTax());
                statement.setInt(5, order.getOrderID());
            }
            else
            {
                statement = connection.prepareStatement("INSERT INTO Orders VALUES (?, ?, ?, ?, ?)");
                statement.setString(2, order.getDate());
                statement.setInt(3, order.getCustomerID());
                statement.setDouble(4, order.getTotalCost());
                statement.setDouble(5, order.getTotalTax());
                statement.setInt(1, order.getOrderID());
            }
            statement.execute();
            resultSet.close();
            statement.close();

            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Database access error!");
            e.printStackTrace();

            return false;
        }
    }
}
