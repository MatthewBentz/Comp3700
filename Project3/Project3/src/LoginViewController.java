import javax.swing.*;
import com.google.gson.Gson;

public class LoginViewController
{
    // ------------------------
    // UI Text Fields & Buttons

    private JButton loginButton;
    private JButton signUpButton;
    private JTextField usernameTF;
    private JPasswordField passwordTF;

    private JPanel mainPanel;

    public LoginViewController(Client client)
    {
        loginButton.addActionListener(e ->
        {
            String username = usernameTF.getText();
            String password = new String(passwordTF.getPassword());

            UserInfo userInfo = new UserInfo(username, password);
            Gson gson = new Gson();
            Message loginMessage = new Message(Message.LOGIN_REQUEST, gson.toJson(userInfo));
            client.sendMessage(loginMessage);
        });

        signUpButton.addActionListener(e ->
        {
            Message signUpMessage = new Message(Message.SIGNUP, "");
            client.sendMessage(signUpMessage);
        });
    }

    public JPanel getMainPanel() { return mainPanel; }
}
