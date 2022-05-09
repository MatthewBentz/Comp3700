import java.sql.*;

public class SQLiteDataAdapter implements DataAccess {
    Connection conn = null;

    @Override
    public void connect() {
        try {
            String url = "jdbc:sqlite:store.db";
            Class.forName("org.sqlite.JDBC");

            conn = DriverManager.getConnection(url);

            if (conn == null)
                System.out.println("Cannot make the connection!");
            else
                System.out.println("Connection to SQLite has been established.");
                System.out.println("The connection object is " + conn);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void saveProduct(ProductModel product) {
        try {
            Statement stmt = conn.createStatement();

            if (loadProduct(product.productID) == null) {
                stmt.execute("INSERT INTO Product(productID, name, price, quantity) VALUES ("
                        + product.productID + ","
                        + '\'' + product.name + '\'' + ","
                        + product.price + ","
                        + product.quantity + ")"
                );
            }
            else {
                stmt.executeUpdate("UPDATE Product SET "
                        + "productID = " + product.productID + ","
                        + "name = " + '\'' + product.name + '\'' + ","
                        + "price = " + product.price + ","
                        + "quantity = " + product.quantity +
                        " WHERE productID = " + product.productID
                );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public ProductModel loadProduct(int productID) {
        ProductModel product = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Product WHERE ProductID = " + productID);
            if (rs.next()) {
                product = new ProductModel();
                product.productID = rs.getInt(1);
                product.name = rs.getString(2);
                product.price = rs.getDouble(3);
                product.quantity = rs.getDouble(4);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return product;
    }

    @Override
    public void saveOrder(OrderModel order) {
        try {
            Statement stmt = conn.createStatement();

            if (loadOrder(order.orderID) == null) {
                stmt.execute("INSERT INTO 'Orders'(OrderID, OrderDate, Customer, TotalCost, TotalTax) VALUES ("
                        + order.orderID + ","
                        + "date('" + order.orderDate + "'),"
                        + "'" + order.customerName + "',"
                        + order.totalCost + ","
                        + order.totalTax + ")"
                );
            }
            else {
                stmt.executeUpdate("UPDATE 'Orders' SET "
                        + "OrderID = " + order.orderID + ","
                        + "OrderDate = " + "date('" + order.orderDate + "'),"
                        + "Customer = " + "'" + order.customerName + "',"
                        + "TotalCost =" + order.totalCost + ","
                        + "TotalTax =" + order.totalTax
                        + " WHERE OrderID = " + order.orderID
                );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public OrderModel loadOrder(int orderId) {
        OrderModel order = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM 'Orders' WHERE OrderID = " + orderId);
            if (rs.next()) {
                order = new OrderModel();
                order.orderDate = rs.getString(2);
                order.customerName = rs.getString(3);
                order.totalCost = rs.getDouble(4);
                order.totalTax = rs.getDouble(5);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return order;
    }

    @Override
    public void saveCustomer(CustomerModel customer) {
        try {
            Statement stmt = conn.createStatement();

            if (loadCustomer(customer.customerID) == null) {
                stmt.execute("INSERT INTO 'Customers'(CustomerID, CustomerName, Address) VALUES ("
                        + customer.customerID + ","
                        + "'" + customer.customerName + "',"
                        + "'" + customer.address + "')"
                );
            }
            else {
                stmt.executeUpdate("UPDATE 'Customers' SET "
                        + "CustomerID = " + customer.customerID + ","
                        + "CustomerName = " + "'" + customer.customerName + "',"
                        + "Address = " + "'" + customer.address + "'"
                        + " WHERE CustomerID = " + customer.customerID
                );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public CustomerModel loadCustomer(int customerID) {
        CustomerModel customer = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Customers WHERE CustomerID = " + customerID);
            if (rs.next()) {
                customer = new CustomerModel();
                customer.customerName = rs.getString(2);
                customer.address = rs.getString(3);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return customer;
    }
}
