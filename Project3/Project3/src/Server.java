import java.awt.*;
import javax.swing.*;
import java.net.Socket;
import java.util.Base64;
import java.io.IOException;
import com.google.gson.Gson;
import java.net.ServerSocket;
import javax.crypto.SecretKey;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Server
{
    private JPanel mainPanel;
    private JTextArea messageTextArea;

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    private SecretKey secretKey;
    private byte[] initializationVector;

    // ----------------------------------------------------

    public Server()
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(12002);
            Socket socket = serverSocket.accept();

            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            String keyString = dataInputStream.readUTF();
            secretKey = KeyService.convertStringToSecretKeyto(keyString);

            String vectorString = dataInputStream.readUTF();
            initializationVector = Base64.getDecoder().decode(vectorString);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        Worker worker = new Worker();
        Thread workerThread = new Thread(worker);
        workerThread.start();
    }

    // ----------------------------------------------------

    private class Worker implements Runnable
    {
        @Override
        public void run()
        {
            while (true)
            {
                String decryptedText = null;

                // -----------------------------
                // Receive input from the client

                try
                {
                    String receive = dataInputStream.readUTF();
                    byte[] decode = Base64.getDecoder().decode(receive);
                    decryptedText = KeyService.do_AESDecryption(decode, secretKey, initializationVector);
                    messageTextArea.append("Received: " + decryptedText + "\n");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                // ----------------------------
                // Process output to the client

                Gson gson = new Gson();
                try
                {
                    Message replyMessage = DatabaseManager.getInstance().process(decryptedText);
                    String replyString = gson.toJson(replyMessage);
                    byte[] cipherText = KeyService.do_AESEncryption(replyString, secretKey, initializationVector);
                    String cipherTextString = Base64.getEncoder().encodeToString(cipherText);
                    dataOutputStream.writeUTF(cipherTextString);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    // ----------------------------------------------------

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Server");
        frame.setContentPane(new Server().mainPanel);
        frame.setMinimumSize(new Dimension(800, 400));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}