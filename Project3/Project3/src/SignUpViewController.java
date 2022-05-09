import javax.swing.*;
import com.google.gson.Gson;

public class SignUpViewController
{
    // ------------------------
    // UI Text Fields & Buttons

    private JButton signUpButton;
    private JTextField usernameTF;
    private JTextField passwordTF;

    private JPanel mainPanel;

    public SignUpViewController(Client client)
    {
        signUpButton.addActionListener(e ->
        {
            String username = usernameTF.getText();
            String password = passwordTF.getText();
            UserInfo userInfo = new UserInfo(username, password);

            Gson gson = new Gson();
            Message signUpMessage = new Message(Message.SIGNUP_REQUEST, gson.toJson(userInfo));
            client.sendMessage(signUpMessage);
        });
    }

    public JPanel getMainPanel() { return mainPanel; }
}
