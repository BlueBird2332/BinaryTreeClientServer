package com.example.binarytreeclientserver.Server;

/**
 * This class represents a single node of the binary tree
 * @param <T> - type of data stored in a tree
 */
public class Node<T> {
    T data;
    Node<T> left, right;

    public Node(T data) {
        this.data = data;
        left = right = null;
    }
}
