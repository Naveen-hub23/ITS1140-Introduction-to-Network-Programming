package lk.ijse.inpfinalexam;

import java.net.ServerSocket;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerController {

    private static final CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public static int clientCount = 0;

    public void serverController(){

        try{
            ServerSocket serverSocket = new ServerSocket(6000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }




}
