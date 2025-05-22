package org.example.demochat.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    Socket socket = new Socket("localhost", 5000);
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    FileInputStream fileInputStream;

    File file;

    Boolean isImage = false;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnSend;

    @FXML
    private Text lblClientTitle;

    @FXML
    private TextArea messageArea;

    @FXML
    private TextField txtMessage;

    public ClientController() throws IOException {
        System.out.println("Starting Client...");
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            String fileName = file.getName();
            if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".gif") || fileName.endsWith(".bmp")) {
                isImage = true;
            }
        }else {
            new Alert(Alert.AlertType.ERROR, "No file selected").show();
        }
    }

    @FXML
    void btnSendOnAction(ActionEvent event) throws IOException{
        if (isImage) {
            sendImage();
        }
        String newMessage = txtMessage.getText();
        messageArea.appendText("Client : " + newMessage + "\n");

        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF(newMessage);
        dataOutputStream.flush();

        txtMessage.clear();
    }

    void sendImage() throws IOException{
        System.out.println(file.toPath());
        if (file != null) {
            byte[] imageByte = Files.readAllBytes(file.toPath());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("IMAGE");
            dataOutputStream.writeInt(imageByte.length);
            dataOutputStream.write(imageByte);
            dataOutputStream.flush();
        }else {
            new Alert(Alert.AlertType.ERROR, "No file selected").show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
                while (true){
                    String message = dataInputStream.readUTF();
                    messageArea.appendText("Server : " + message +  "\n");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
