package com.example.binarytreeclientserver.ServerData;

import java.io.Serializable;

/**
 * This class is a class for sending messages from client to server
 */
public class ClToSeMessage implements Serializable {

    public MessageType messageType;
    public String messageBody;

    public ClToSeMessage(MessageType messageType, String messageBody ){
        this.messageType = messageType;
        this.messageBody = messageBody;
    }
    public enum MessageType{
        TYPE_OF_TREE,
        INSERT,
        PRINT,
        DELETE,
        SEARCH,
    }
}
