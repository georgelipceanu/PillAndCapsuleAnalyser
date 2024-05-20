package org.example.pillandcapsuleanalyser.models;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class PillDetails {
    private String name;
    private int capsuleNumber=-1;
    private int size=-1;
    private int root=-1;
    private Color color;

    private ArrayList<Integer> indices = new ArrayList<>();

    public PillDetails(String name, int capsuleNumber, int size, int root, Color color){
        setName(name);
        setRoot(root);
        setCapsuleNumber(capsuleNumber);
        setSize(size);
        setColor(color);
    }

    public int getCapsuleNumber() {
        return capsuleNumber;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public int getRoot() {
        return root;
    }

    public Color getColor() {
        return color;
    }

    public ArrayList<Integer> getIndices() {
        return indices;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapsuleNumber(int capsuleNumber) {
        this.capsuleNumber = capsuleNumber;
    }

    public void setSize(int estimateSize) {
        this.size = estimateSize;
    }

    public void setRoot(int root) {
        this.root = root;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        String str="PillDetails{" +
                "name='" + name + '\'' +
                ", capsuleNumber=" + capsuleNumber +
                ", size=" + size +
                ", root=" + root +
                '}' + '\n';
        for (Integer i : indices){
            str+= " " + i;
        }

        return str;
    }
}
