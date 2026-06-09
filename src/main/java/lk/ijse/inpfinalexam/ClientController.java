package lk.ijse.inpfinalexam;

import javafx.application.Platform;
import javafx.event.ActionEvent;
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

                String serverUsername = dataInputStream.readUTF();
                Platform.runLater(() -> {
                    clientUsername.setText(serverUsername);
                });

                dataOutputStream.writeUTF("Client");
                dataOutputStream.flush();

                while (true) {
                    String msg = dataInputStream.readUTF();
                    Platform.runLater(() -> {
                        processServerMessage(msg);
                    });
                }

            } catch (Exception e) {
                Platform.runLater(() -> {
                    clientTextArea.appendText("Connection lost.\n");
                });
            }
        }).start();
    }

    private void processServerMessage(String msg) {

        String[] parts = msg.split(":", 2);
        String type = parts[0];
        String data = parts.length > 1 ? parts[1] : "";

        if (type.equals("ITEM")) {
            itemName.setText(data);

        } else if (type.equals("HIGHEST")) {
            highestBid.setText("Rs." + data);

        } else if (type.equals("BID")) {

            String[] bidParts = data.split(":");

            if (bidParts.length >= 3) {
                String bidder = bidParts[0];
                String amount = bidParts[1];
                String item = bidParts[2];
                clientTextArea.appendText(bidder + " bid Rs." + amount + " on " + item + "\n");
            }

        } else if (type.equals("JOIN")) {
            clientTextArea.appendText(data + " joined.\n");

        } else if (type.equals("LEAVE")) {
            clientTextArea.appendText(data + " left.\n");

        } else if (type.equals("BID_FAIL")) {
            clientTextArea.appendText(data + "\n");

        } else if (type.equals("ERROR")) {
            clientTextArea.appendText(data + "\n");

        } else {
            clientTextArea.appendText(msg + "\n");
        }
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
            clientTextArea.appendText("Failed to send bid.\n");
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
        }
    }
}
