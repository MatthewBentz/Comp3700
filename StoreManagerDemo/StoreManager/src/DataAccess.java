public interface DataAccess {
    void connect();

    void saveProduct(ProductModel product);

    ProductModel loadProduct(int productID);
}
