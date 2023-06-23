package com.example.binarytreeclientserver.Server;

/**
 * This class represents a binary search tree
 * @param <T> - type of data stored in a tree
 */
public class BinaryTree<T extends Comparable<T>> {
    private Node<T> root;

    /**
     * Creates new binary tree with empty root
     */
    public BinaryTree() {
        this.root = null;
    }

    /**
     * inserts data into a tree
     * @param data - data to be inserted
     */
    public void insert(T data) {
        root = insert(root, data);
    }

    /**
     * searches whether an item is in a tree
     * @param data - value to be searched for
     * @return if data was found
     */
    public boolean search(T data) {
        return search(root, data);
    }

    /**
     * deletes an item from a binary tree
     * @param data - value of the item to be deleted
     */
    public void delete(T data) {
        root = delete(root, data);
    }

    public void printTree() {
        System.out.println(this);
    }

    private boolean search(Node<T> root, T data){
        if(root ==null){
            return false;
        }
        if(data.compareTo(root.data) == 0){
            return true;
        }
        if(data.compareTo(root.data) < 0){
            return search(root.left, data);
        }
        return search(root.right, data);
    }

    private Node insert(Node<T> node, T data) {

        if (node == null) {
            return new Node<>(data);
        }
        if (data.compareTo(node.data) < 0) {
            node.left = insert(node.left, data);
        } else if (data.compareTo(node.data) > 0) {
            node.right = insert(node.right, data);
        }
        return node;
    }

    @Override
    public String toString() {
        if (root == null)
            return "Tree is empty.";
        else
            return getTreeString(root, 0);
    }
    private T minValue(Node<T> root) {
        T minValue = root.data;
        while(root.left != null) {
            minValue = root.left.data;
            root = root.left;
        }
        return minValue;
    }
    private Node<T> delete(Node<T> root,T data){

        if(root == null){
            return root;
        }

        if(data.compareTo(root.data) < 0){
            root.left = delete(root.left, data);
        } else if(data.compareTo(root.data) > 0){
            root.right = delete(root.right, data);
        } else {
            if(root.left == null){
                return root.right;
            } else if (root.left == null) {
                return root.left;
            }
            root.data = minValue(root.right);
            root.right = delete(root.right, root.data);

        }
        return root;
    }

    private String getTreeString(Node<T> root, int level) {
        if (root == null)
            return "";

        String myString = new String();

        myString += getTreeString(root.right, level + 1);
        for(int i = 0; i < level; i++){
            myString += "     ";
        }
        myString += root.data + "\n";

        myString += getTreeString(root.left, level +1);

        return myString;

    }
}














