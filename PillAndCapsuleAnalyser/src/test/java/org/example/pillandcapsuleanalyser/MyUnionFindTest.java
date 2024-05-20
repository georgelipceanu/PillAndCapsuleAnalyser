package org.example.pillandcapsuleanalyser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyUnionFindTest {
    int width = 6;
    int height = 6;
    int[] imageArray = new int[width*height];//size 36

    MyUnionFind uf = new MyUnionFind(width*height); //size 36

    @BeforeEach
    void setup(){
        imageArray[2]=-1;
        imageArray[3]=-1;
        imageArray[9]=-1;
        imageArray[8]=-1;

        imageArray[25]=-1;
        imageArray[26]=-1;
        imageArray[33]=-1;
        imageArray[34]=-1;
    }


    @Test
    void unionChangesRootToCorrectRoot() {
        uf.union(2,3);
        uf.union(2,8);
        uf.union(8,9);

        uf.union(25,26);
        uf.union(25,33);
        uf.union(33,34);
        //union of pixels

        assertEquals(2,uf.getParent()[3]);
        assertEquals(2,uf.getParent()[8]);
        assertEquals(2,uf.getParent()[9]);


        assertEquals(25,uf.getParent()[26]);
        assertEquals(25,uf.getParent()[33]);
        assertEquals(25,uf.getParent()[34]);
    }

    @Test
    void unionChangesSize() {
        assertEquals(36,uf.getSize());

        uf.union(2,3);
        assertEquals(35,uf.getSize());

        uf.union(2,8);
        uf.union(8,9);

        assertEquals(33,uf.getSize());


    }

    @Test
    void unionChangesNodeSize() {
        assertEquals(1,uf.getNodeSizes()[2]);

        uf.union(2,3);
        assertEquals(2,uf.getNodeSizes()[2]);
        assertEquals(1,uf.getNodeSizes()[3]);

        uf.union(2,8);
        uf.union(8,9);

        assertEquals(4,uf.getNodeSizes()[2]);
        assertEquals(1,uf.getNodeSizes()[8]);
        assertEquals(1,uf.getNodeSizes()[9]);
    }

    @Test
    void findReturnsRoot() {
        assertEquals(2,uf.find(2));
        assertEquals(3,uf.find(3));
        assertEquals(8,uf.find(8));
        assertEquals(9,uf.find(9));

        uf.union(2,3);
        uf.union(2,8);
        uf.union(8,9);
        //union of pixels

        assertEquals(2,uf.find(3));
        assertEquals(2,uf.find(8));
        assertEquals(2,uf.find(9));

    }

    @Test
    void unionFindConstructorTest(){
        assertEquals(36,uf.getParent().length);
        assertEquals(36,uf.getNodeSizes().length);//testing if all arrays are instantiated correctly
        for (int i : uf.getParent()) assertEquals(i,uf.getParent()[i]);
        for (int i : uf.getNodeSizes()) assertEquals(1,uf.getParent()[i]);
    }
}