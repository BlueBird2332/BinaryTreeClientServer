package com.example.binarytreeclientserver.Server;

import com.example.binarytreeclientserver.ServerData.ClToSeMessage;
import com.example.binarytreeclientserver.ServerData.SeToClMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Function;

/**
 * this function processes conection data in a server - client connection on the part of the server
 */
public class ClientHandler {

    public ObjectOutputStream serverToClientStream;
    public ObjectInputStream clientToServerStream;
    public ClientHandler(Socket clientSocket) throws IOException {
        serverToClientStream = new ObjectOutputStream(clientSocket.getOutputStream());
        clientToServerStream = new ObjectInputStream(clientSocket.getInputStream());
    }

    public void startClientLoop() throws IOException, ClassNotFoundException {

        String treeType;
        while(true){
            var message = (ClToSeMessage) clientToServerStream.readObject();
            System.out.println(message);

            if(message.messageType != ClToSeMessage.MessageType.TYPE_OF_TREE ){
                continue;
            }
            treeType = message.messageBody;
            break;
        }
        switch (treeType){
            case "STRING"-> handleTreeLoop(messageData -> messageData);
            case "DOUBLE" -> handleTreeLoop(Double :: valueOf);
            case "INTEGER" -> handleTreeLoop(Integer :: valueOf);
            default -> throw new IllegalArgumentException("Unknown Tree Type");
        }
    }
    private <T extends Comparable<T>> void handleTreeLoop(Function<String, T> payloadParser)
            throws IOException, ClassNotFoundException {
        BinaryTree<T> bst = new BinaryTree<>();
        new InternalTreeLoopHandler<T>(bst, payloadParser).handleTreeLoop();
    }

    private class InternalTreeLoopHandler<T extends Comparable<T>> {
        private BinaryTree<T> bst;
        private final Function<String, T> payloadParser;

        public InternalTreeLoopHandler(BinaryTree<T> bst, Function<String, T> payloadParser) {
            this.bst = bst;
            this.payloadParser = payloadParser;
        }

        public void handleTreeLoop() throws IOException, ClassNotFoundException {
            while(true) {
                var message = (ClToSeMessage) clientToServerStream.readObject();

                System.out.println("Received message");
                System.out.println(message);

                switch(message.messageType) {
                    case INSERT -> handleInsert(message);
                    case SEARCH -> handleSearch(message);
                    case DELETE -> handleDelete(message);
                    case PRINT -> serverToClientStream.writeObject(
                            new SeToClMessage(
                                    SeToClMessage.MessageType.PRINT_TREE,
                                    bst.toString()
                            )
                    );
                }
            }
        }

        private void handleInsert(ClToSeMessage message) throws IOException {
            T insertedValue;
            try {
                insertedValue = payloadParser.apply(message.messageBody);
            } catch(Exception e) {
                serverToClientStream.writeObject(
                        new SeToClMessage(SeToClMessage.MessageType.INSERT_ERROR, "")
                );
                return;
            }

            bst.insert(insertedValue);
            serverToClientStream.writeObject(
                    new SeToClMessage(SeToClMessage.MessageType.INSERT_COMPLETED, "")
            );
        }

        private void handleSearch(ClToSeMessage message) throws IOException {
            T searchedValue;
            try {
                searchedValue = payloadParser.apply(message.messageBody);
            } catch(Exception e) {
                serverToClientStream.writeObject(
                        new SeToClMessage(SeToClMessage.MessageType.SEARCH_ERROR, "")
                );
                return;
            }

            boolean valueWasFound = bst.search(searchedValue);

            var responseMessageType = valueWasFound
                    ? SeToClMessage.MessageType.SEARCH_FOUND
                    : SeToClMessage.MessageType.SEARCH_NOT_FOUND;

            serverToClientStream.writeObject(
                    new SeToClMessage(responseMessageType, message.messageBody)
            );
        }

        private void handleDelete(ClToSeMessage message) throws IOException {
            T deletedValue;
            try {
                deletedValue = payloadParser.apply(message.messageBody);
            } catch(Exception e) {
                serverToClientStream.writeObject(
                        new SeToClMessage(SeToClMessage.MessageType.DELETE_ERROR, "")
                );
                return;
            }

            bst.delete(deletedValue);

            serverToClientStream.writeObject(
                    new SeToClMessage(SeToClMessage.MessageType.DELETE_COMPLETED, message.messageBody)
            );
        }
    }

}
