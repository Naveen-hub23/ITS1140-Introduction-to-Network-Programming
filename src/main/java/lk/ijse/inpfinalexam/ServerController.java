package lk.ijse.inpfinalexam;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerController {

    private static final CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

    private static String itemName = "Vintage Watch";
    private static double highestBid = 5000.0;
    private static String highestBidder = "nobody";

    public static void serverController() {

        try {
            Scanner sc = new Scanner(System.in);

            System.out.print("Enter Server Username: ");
            String serverUsername = sc.nextLine();

            ServerSocket serverSocket = new ServerSocket(6000);

            System.out.println("Auction Server Started - Port 6000");

            while (true) {

                Socket clientSocket = serverSocket.accept();
                System.out.println("Client Connected");

                DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
                dos.writeUTF(serverUsername);
                dos.flush();

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static synchronized void broadcast(String message) {

        for (ClientHandler client : clients) {
            try {
                DataOutputStream out = new DataOutputStream(client.socket.getOutputStream());
                out.writeUTF(message);
                out.flush();
            } catch (Exception e) {
                clients.remove(client);
            }
        }
    }

    private static synchronized void sendCurrentState(DataOutputStream out) throws Exception {

        out.writeUTF("ITEM:" + itemName);
        out.writeUTF("HIGHEST:" + highestBid);
    }

    private static synchronized boolean placeBid(double bid, String bidder) {

        if (bid > highestBid) {

            highestBid = bid;
            highestBidder = bidder;
            broadcast("BID:" + bidder + ":" + bid + ":" + itemName);
            broadcast("HIGHEST:" + highestBid);
            return true;
        }
        return false;
    }

    static class ClientHandler implements Runnable {

        private Socket socket;
        private String clientUsername;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            try {
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                clientUsername = dis.readUTF();
                sendCurrentState(dos);
                broadcast("JOIN:" + clientUsername);

                while (true) {

                    String msg = dis.readUTF();

                    try {
                        double bid = Double.parseDouble(msg.trim());

                        if (!placeBid(bid, clientUsername)) {
                            dos.writeUTF("BID_FAIL:Your bid must be higher than the current highest.");
                            dos.flush();
                        }
                    } catch (NumberFormatException ex) {
                        dos.writeUTF("ERROR:Invalid bid format");
                        dos.flush();
                    }
                }

            } catch (Exception e) {

                System.out.println("Client Disconnected");

            } finally {

                clients.remove(this);
                broadcast("LEAVE:" + (clientUsername != null ? clientUsername : "unknown"));

                try {
                    socket.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public static void main(String[] args) {
        serverController();
    }
}