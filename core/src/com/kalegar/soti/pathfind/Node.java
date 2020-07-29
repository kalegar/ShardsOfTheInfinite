package com.kalegar.soti.pathfind;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;

public class Node {

    public final int x;

    public final int y;

    private int index;

    private Object data;

    protected Array<Connection<Node>> connections;

    public Node(int x, int y, int index, int connectionCapacity) {
        this.x = x;
        this.y = y;
        this.index = index;
        this.connections = new Array<>(connectionCapacity);
    }

    public int getIndex() {
        return index;
    }

    public Array<Connection<Node>> getConnections() {
        return connections;
    }

    public void createConnection(Node node, int cost) {
        NodeConnection connection = new NodeConnection(this, node,cost);
        connections.add(connection);
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
