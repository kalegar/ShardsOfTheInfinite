package com.kalegar.soti.pathfind;

import com.badlogic.gdx.ai.pfa.Connection;
import com.kalegar.soti.pathfind.Node;

public class NodeConnection implements Connection<Node> {

    private Node from;
    private Node to;
    private float cost;

    public NodeConnection(Node from, Node to, float cost) {
        this.from = from;
        this.to = to;
        this.cost = cost;
    }

    @Override
    public float getCost() {
        return cost;
    }

    @Override
    public Node getFromNode() {
        return from;
    }

    @Override
    public Node getToNode() {
        return to;
    }
}
