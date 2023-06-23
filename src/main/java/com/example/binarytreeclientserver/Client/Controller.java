package com.example.binarytreeclientserver.Client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import com.example.binarytreeclientserver.ServerData.*;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Main Controller handling FXML logic and process client - server connection
 * on the part of the client
 */
public class Controller {
    @FXML
    private TextField insertDataTextField;
    @FXML
    private TextField searchDataTextField;
    @FXML
    private TextField deleteDataTextField;

    @FXML
    private Text insertResponseText;
    @FXML
    private Text searchResponseText;
    @FXML
    private Text deleteResponseText;

    @FXML
    private TextArea treePreviewTextArea;

    private Socket connection;
    private ObjectOutputStream sendToServerStream;
    private ObjectInputStream receiveFromServer;

    /**
     * This function initializes the new client, initializes and establishes connection
     * with server and gets information about type of the tree
     * @throws IOException
     */
    @FXML
    public void initialize() throws IOException {

        String chosenTreeType = waitForUserToChooseTreeType();
        connection = new Socket(PortConnectionData.HOST, PortConnectionData.CONNECTION_PORT);
        sendToServerStream = new ObjectOutputStream(connection.getOutputStream());
        receiveFromServer = new ObjectInputStream(connection.getInputStream());

        getConnectionThread().start();
        sendToServerStream.writeObject(new ClToSeMessage(ClToSeMessage.MessageType.TYPE_OF_TREE, chosenTreeType));
    }
    @FXML
    public void handleInsert() throws IOException {
        sendToServerStream.writeObject(new ClToSeMessage(ClToSeMessage.MessageType.INSERT, insertDataTextField.getText()));
    }
    @FXML
    public void handleSearch() throws IOException {
        sendToServerStream.writeObject(new ClToSeMessage(ClToSeMessage.MessageType.SEARCH, searchDataTextField.getText()));
    }
    @FXML
    public void handleDelete() throws IOException {
        sendToServerStream.writeObject(new ClToSeMessage(ClToSeMessage.MessageType.DELETE, deleteDataTextField.getText()));
    }

    /**
     * This function retruns a client thread, that communicates with the server
     * @return client thread
     */
    public Thread getConnectionThread(){
        return new Thread(()->{
            while(true){
                try {
                    var message = (SeToClMessage) receiveFromServer.readObject();
                    Platform.runLater(()->handleServerMessage(message));
                } catch (EOFException e) {
                    System.out.println("Server not Found ERROR 404");
                    System.exit(-1);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * This function processes messages received from server
     * @param message - message from server to client
     */
    public void handleServerMessage(SeToClMessage message){
        if(message.messageType == SeToClMessage.MessageType.PRINT_TREE){
            treePreviewTextArea.setText(message.messageBody);
            return;
        }
        switch (message.messageType){
            case INSERT_COMPLETED -> {
                insertResponseText.setText("Element " + insertDataTextField.getText() + " Inserted");
                insertDataTextField.setText("");
            }
            case INSERT_ERROR -> {
                insertResponseText.setText("Unable to insert: " + insertDataTextField.getText());
                insertDataTextField.setText("");
            }
            case DELETE_COMPLETED -> {
                deleteResponseText.setText("Element " + insertDataTextField.getText() + " Deleted");
                deleteDataTextField.setText("");
            }
            case DELETE_ERROR -> {
                deleteResponseText.setText("Unable to delete: " + insertDataTextField.getText());
                deleteDataTextField.setText("");
            }
            case SEARCH_FOUND -> {
                searchResponseText.setText("Element " + insertDataTextField.getText() + " found");
                searchDataTextField.setText("");
            }
            case SEARCH_ERROR -> {
                searchResponseText.setText("Element " + insertDataTextField.getText() + " not found");
                searchDataTextField.setText("");
            }
            case SEARCH_NOT_FOUND-> {
                searchResponseText.setText("While searching for " + insertDataTextField.getText() + "an error occurred");
                searchDataTextField.setText("");
            }
        }
        try{
            sendMessageToServer(ClToSeMessage.MessageType.PRINT,"");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * this function sends messages to server
     * @param messageType - type of message
     * @param messageBody - contents of the message
     * @throws IOException
     */
    private void sendMessageToServer(ClToSeMessage.MessageType messageType, String messageBody) throws IOException {
        sendToServerStream.writeObject(new ClToSeMessage(messageType,messageBody));
    }

    private String waitForUserToChooseTreeType() {
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>();
        choiceDialog.getItems().setAll("INTEGER", "DOUBLE", "STRING");
        choiceDialog.setSelectedItem("INTEGER");
        choiceDialog.setTitle("CUSTOM CHOICE DIALOG");
        choiceDialog.setHeaderText("CHOOSE THE TREE TYPE:");
        choiceDialog.getDialogPane().setStyle("-fx-background-color: lightgray; -fx-border-color: black; -fx-border-width: 5px;");
        choiceDialog.getDialogPane().setGraphic(null);

        return choiceDialog.showAndWait().get();
    }



}
