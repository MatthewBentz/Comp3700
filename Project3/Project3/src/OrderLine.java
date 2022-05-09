public class OrderLine
{
    private int orderID;
    private double cost;
    private int productID;
    private double quantity;

    public double getQuantity() {
        return quantity;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getCost() {
        return cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getOrderID() {
        return orderID;
    }
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getProductID() {
        return productID;
    }
    public void setProductID(int productID) {
        this.productID = productID;
    }

    @Override
    public String toString() {
        return "OrderLine{" +
                "productID=" + productID +
                ", orderID=" + orderID +
                ", quantity=" + quantity +
                ", cost=" + cost +
                '}';
    }
}
