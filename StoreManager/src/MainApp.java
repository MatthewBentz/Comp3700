import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        StoreManager.getInstance().getMainScreen().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        StoreManager.getInstance().getMainScreen().setVisible(true);
    }
}
