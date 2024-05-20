package org.example.pillandcapsuleanalyser;

public class MyUnionFind{

    private int size;

    public int getSize() {
        return size;
    }

    private int[] parent;

    public int[] getParent() {
        return parent;
    }

    private int[] nodeSizes;


    public int[] getNodeSizes() {
        return nodeSizes;
    }

    public MyUnionFind(int size) {
        this.size = size;
        nodeSizes = new int[size];
        parent = new int[size];//parent and size for each node

        for (int i = 0; i < size; i++) {
            parent[i] = i; //self root
            nodeSizes[i] = 1; //each node is originally size 1
        }
    }

    public int find(int id) {
        if(parent[id] == id ) return id;
        else {
            return parent[id]=find(parent[id]);
        }
    }

    public void union(int p, int q) {
        //union by size
        int root1 = find(p);
        int root2 = find(q);

        if(root1 == root2) return;

        if(nodeSizes[root1] < nodeSizes[root2]) {
            nodeSizes[root2] += nodeSizes[root1];
            parent[root1]=root2;//new parent for p

        } else {
            nodeSizes[root1] += nodeSizes[root2];
            parent[root2]=root1; //new parent for q
        }
        size--;
    }
}
