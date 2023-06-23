package com.example.binarytreeclientserver.ServerData;

import java.io.Serializable;
/**
 * This class is a class for sending messages from server to client
 */
public class SeToClMessage implements Serializable {

    public MessageType messageType;
    public String messageBody;

    public SeToClMessage(MessageType messageType, String messageBody ){
        this.messageType = messageType;
        this.messageBody = messageBody;
    }
    public enum MessageType{
        INSERT_COMPLETED,
        INSERT_ERROR,
        DELETE_COMPLETED,
        DELETE_ERROR,
        SEARCH_FOUND,
        SEARCH_NOT_FOUND,
        SEARCH_ERROR,
        PRINT_TREE,
    }
}
