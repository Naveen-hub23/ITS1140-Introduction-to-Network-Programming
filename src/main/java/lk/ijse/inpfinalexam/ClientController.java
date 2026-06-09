package lk.ijse.inpfinalexam;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientController {

    @FXML
    private TextArea clientTextArea;

    @FXML
    private TextField clientTextField;

    @FXML
    private Label clientUsername;

    @FXML
    private Label highestBid;

    @FXML
    private Label itemName;


    public void initialize(){

        new Thread(()->{

            try {
                Socket socket = new Socket("127.0.0.1",6000);

                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                DataInputStream dis = new DataInputStream(socket.getInputStream());

                Platform.runLater(()->{
                    clientTextField.appendText("Enter your name: ");

                    if(clientTextField.getText().equals("")){
                        clientUsername.setText("Enter your name: ");
                    }

                });



            }catch (Exception e){
            e.printStackTrace();
            }
        }).start();
    }
}

