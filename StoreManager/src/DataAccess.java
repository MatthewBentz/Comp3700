public interface DataAccess {
    void connect();

    void saveProduct(ProductModel product);
    ProductModel loadProduct(int productID);

    OrderModel loadOrder(int orderId);
    void saveOrder(OrderModel order);

    CustomerModel loadCustomer(int customerID);
    void saveCustomer(CustomerModel customerModel);
 }
