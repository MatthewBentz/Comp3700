import java.util.List;
import java.util.ArrayList;

public class Order
{
    private int orderID;
    private String date;
    private int customerID;
    private double totalTax;
    private double totalCost;

    private List<OrderLine> lines;
    public Order() {
        lines = new ArrayList<>();
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalCost() {
        return totalCost;
    }
    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getTotalTax() {
        return totalTax;
    }
    public void setTotalTax(double totalTax) {
        this.totalTax = totalTax;
    }

    public int getOrderID() {
        return orderID;
    }
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getCustomerID() {
        return customerID;
    }
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void addLine(OrderLine line) {
        lines.add(line);
    }
    public void removeLine(OrderLine line) {
        lines.remove(line);
    }

    public List<OrderLine> getLines() {
        return lines;
    }
}
