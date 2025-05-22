package org.example.demochat.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import javax.net.ServerSocketFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerController implements Initializable {
    ServerSocket serverSocket;
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    FileOutputStream fileOutputStream;

    @FXML
    private Button btnDownload;

    @FXML
    private Button btnSend;

    @FXML
    private ImageView imageView;

    @FXML
    private Text lblServerTitle;

    @FXML
    private TextArea messageArea;

    @FXML
    private TextField txtMessage;

    @FXML
    void btnDownloadOnAction(ActionEvent event) {
        Image image = imageView.getImage();

        File file = new File("D:\\INP\\demoChat\\image.png");
    }

    @FXML
    void btnSendOnAction(ActionEvent event) throws IOException{
        String newMessage = txtMessage.getText();
        messageArea.appendText("Server:" + newMessage +"\n");

        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF(newMessage);
        dataOutputStream.flush();

        txtMessage.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
            ServerSocketFactory serverSocketFactory = ServerSocketFactory.getDefault();

            try {
                serverSocket = serverSocketFactory.createServerSocket(5000);
                socket = serverSocket.accept();
                System.out.println("Client Connected");

                dataInputStream = new DataInputStream(socket.getInputStream());
                while (true){
                    String message = dataInputStream.readUTF();

                    if (message.equals("IMAGE")){
                        int length = dataInputStream.readInt();
                        byte[] imageByte = new byte[length];
                        dataInputStream.readFully(imageByte, 0, length);
                        ByteArrayInputStream bais = new ByteArrayInputStream(imageByte);
                        Image image = new Image(bais);
                        imageView.setImage(image);


                        messageArea.appendText("Client : " + "image received" + "\n");
                    }else {
                        if (!message.isEmpty()) {
                            messageArea.appendText("Client : " + message + "\n");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }
}
