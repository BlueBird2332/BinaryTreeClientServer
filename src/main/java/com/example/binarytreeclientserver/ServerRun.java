package com.example.binarytreeclientserver;

import com.example.binarytreeclientserver.Server.ClientHandler;
import com.example.binarytreeclientserver.ServerData.PortConnectionData;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server run class
 */
public class ServerRun {
    public static void main(String[] args) throws InterruptedException, IOException {

        var serverThread = getServerThread();
        serverThread.start();
    }

    /**
     * This function creates and returns a server thread
     * @return - server thread
     * @throws IOException
     */
    public static Thread getServerThread() throws IOException {
        ServerSocket socket = new ServerSocket(PortConnectionData.CONNECTION_PORT);
        return new Thread(() -> {
            while(true){
                Socket clientSocket;
                try {
                    clientSocket = socket.accept();
                    System.out.println("CLient Accepted");
                } catch (IOException e){
                    System.out.println("Client not connected");
                    e.printStackTrace();
                    continue;
                }
                new Thread(()->{
                    try{
                        new ClientHandler(clientSocket).startClientLoop();
                    } catch (Exception e){}
                }).start();
            }
        });
    }
}
