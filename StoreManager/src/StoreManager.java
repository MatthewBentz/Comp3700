public class StoreManager {

    private SQLiteDataAdapter dao;
    private static StoreManager instance = null;

    // ------------
    // View Objects
    private OrderView orderView = null;
    private MainScreen mainScreen = null;
    private ProductView productView = null;
    private CustomerView customerView = null;

    // ---------------------------
    // Create View Object Instance
    public OrderView getOrderView() { return orderView; }
    public MainScreen getMainScreen() {
        return mainScreen;
    }
    public ProductView getProductView() {
        return productView;
    }
    public CustomerView getCustomerView() { return customerView; }

    private StoreManager() { }

    public static StoreManager getInstance() {
        if (instance == null)
            instance = new StoreManager("SQLite");
        return instance;
    }

    public SQLiteDataAdapter getDataAccess() {
        return dao;
    }

    private StoreManager(String db) {
        if (db.equals("SQLite"))
            dao = new SQLiteDataAdapter();

        assert dao != null;
        dao.connect();

        // -----
        // Views
        orderView = new OrderView();
        productView = new ProductView();
        customerView = new CustomerView();

        // ------------------
        // Controller Objects
        OrderController orderController = new OrderController(orderView, dao);
        ProductController productController = new ProductController(productView, dao);
        CustomerController customerController = new CustomerController(customerView, dao);

        // ---------
        // Main Menu
        mainScreen = new MainScreen();
    }
}
