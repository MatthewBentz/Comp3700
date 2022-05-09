public class StoreManager {

    private static StoreManager instance = null;

    private SQLiteDataAdapter dao;

    private ProductView productView = null;

    public ProductView getProductView() {
        return productView;
    }

    private ProductController productController = null;

    public static StoreManager getInstance() {
        if (instance == null)
            instance = new StoreManager("SQLite");
        return instance;
    }

    public SQLiteDataAdapter getDataAccess() {
        return dao;
    }

    private StoreManager(String db) {
        // do some initialization here!!!
        if (db.equals("SQLite"))
            dao = new SQLiteDataAdapter();

        dao.connect();
        productView = new ProductView();
        productController = new ProductController(productView, dao);
    }






}
