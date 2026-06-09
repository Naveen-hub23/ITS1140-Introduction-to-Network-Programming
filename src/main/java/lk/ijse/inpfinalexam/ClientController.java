package lk.ijse.inpfinalexam;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private Socket socket;

    @FXML
    public void initialize() {

        new Thread(() -> {

            try {
                socket = new Socket("127.0.0.1", 6000);

                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                String username = dataInputStream.readUTF();

                Platform.runLater(() ->
                        clientUsername.setText(username)
                );

                while (true) {
                    String msg = dataInputStream.readUTF();

                    Platform.runLater(() ->
                            clientTextArea.appendText(msg + "\n")
                    );
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }

    @FXML
    public void clientBidSubmitOnAction(ActionEvent event) {

        try {

            String msg = clientTextField.getText().trim();

            if (msg.isEmpty()) return;

            dataOutputStream.writeUTF(msg);
            dataOutputStream.flush();

            clientTextField.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void clientDisconnectOnAction(ActionEvent event) {

        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Platform.exit();
            System.exit(0);
        }
    }
}