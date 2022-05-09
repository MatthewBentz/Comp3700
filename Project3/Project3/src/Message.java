public class Message {

    public static final int FAIL = -1;
    public static final int SUCCESS = 0;

    // ------------
    // Product ID's

    public static final int LOAD_PRODUCT = 1;
    public static final int LOAD_PRODUCT_REPLY = 2;

    public static final int SAVE_PRODUCT = 3;

    // -------------
    // Customer ID's

    public static final int LOAD_CUSTOMER = 4;
    public static final int LOAD_CUSTOMER_REPLY = 5;

    public static final int SAVE_CUSTOMER = 6;

    // ----------
    // Order ID's

    public static final int SAVE_ORDER = 7;
    public static final int LOAD_ORDER = 8;
    public static final int LOAD_ORDER_REPLY = 9;

    // ----------
    // Login ID's

    public static final int LOGIN = 1000;
    public static final int LOGIN_REQUEST = 1001;
    public static final int LOGIN_RESPONSE_NO_USERNAME = 1002;
    public static final int LOGIN_RESPONSE_FAIL_PASSWORD = 1003;
    public static final int LOGIN_RESPONSE_SUCCESS = 1004;

    // -----------
    // SignUp ID's

    public static final int SIGNUP = 1005;
    public static final int SIGNUP_REQUEST = 1006;
    public static final int SIGNUP_RESPONSE = 1007;
    public static final int SIGNUP_RESPONSE_FAIL = 1008;
    public static final int SIGNUP_RESPONSE_SUCCESS = 1009;

    // -----------
    // Search ID's

    public static final int SEARCH_REQUEST = 1010;
    public static final int SEARCH_RESPONSE = 1011;

    // -----------------
    // Manage Order ID's

    public static final int DELETE_REQUEST = 1012;
    public static final int DELETE_RESPONSE_SUCCESS = 1013;
    public static final int LOAD_CUSTOMER_ORDERS = 1014;
    public static final int LOAD_CUSTOMER_ORDERS_REPLY = 1015;

    // ---------------
    // Order Line ID's

    public static final int LOAD_PRODUCT_TO_ADD = 10005;
    public static final int LOAD_PRODUCT_REPLY_TO_ADD=10006;
    public static final int SAVE_ORDER_LINE = 10007;

    private int id;
    private String content;

    public Message(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getContent() {
        return content;
    }
}
