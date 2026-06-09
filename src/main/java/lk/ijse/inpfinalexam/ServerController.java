package lk.ijse.inpfinalexam;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerController {

    private static final CopyOnWriteArrayList<ClientHandler> clients =
            new CopyOnWriteArrayList<>();

    public static void serverController() {

        try {

            Scanner sc = new Scanner(System.in);

            System.out.print("Enter Server Username: ");
            String username = sc.nextLine();

            ServerSocket serverSocket = new ServerSocket(6000);

            System.out.println("Auction Server Started - Port 6000");

            while (true) {

                Socket clientSocket = serverSocket.accept();

                System.out.println("Client Connected");

                DataOutputStream dos =
                        new DataOutputStream(clientSocket.getOutputStream());


                dos.writeUTF(username);
                dos.flush();

                ClientHandler clientHandler =
                        new ClientHandler(clientSocket);

                clients.add(clientHandler);

                new Thread(clientHandler).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {

        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            try {

                DataInputStream dis =
                        new DataInputStream(socket.getInputStream());

                while (true) {

                    String msg = dis.readUTF();

                    System.out.println("Client: " + msg);
                }

            } catch (Exception e) {

                System.out.println("Client Disconnected");
            }
        }
    }

    public static void main(String[] args) {
        serverController();
    }
}