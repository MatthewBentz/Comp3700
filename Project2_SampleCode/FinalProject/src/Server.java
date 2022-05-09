import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private JPanel mainPanel;
    private JTextArea messageTextArea;

    private ServerSocket serverSocket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    private Worker worker;

    public Server() {
        try {
            serverSocket = new ServerSocket(12002);
            Socket socket = serverSocket.accept();

            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        worker = new Worker();
        Thread workerThread = new Thread(worker);
        workerThread.start();
    }

    private class Worker implements Runnable {

        @Override
        public void run() {
            while (true) {
                String requestString = null;
                try {
                    requestString = dataInputStream.readUTF();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                messageTextArea.append("Received: " + requestString + "\n");

                Message replyMessage = DatabaseManager.getInstance().process(requestString);

                Gson gson = new Gson();

                String replyString = gson.toJson(replyMessage);

                try {
                    dataOutputStream.writeUTF(replyString);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Server");
        frame.setContentPane(new Server().mainPanel);
        frame.setMinimumSize(new Dimension(800, 400));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
