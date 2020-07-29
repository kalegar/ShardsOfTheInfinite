package com.kalegar.soti.tile;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;
import com.kalegar.soti.pathfind.Node;

public class TileGraph implements IndexedGraph<Node> {

    private final Array<Tile> nodes = new Array<>();

    public final int width;
    public final int height;

    public TileGraph(int width, int height) {
        this.width = width;
        this.height = height;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Tile node = new Tile(x,y,width*y+x);
                nodes.add(node);
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Tile node   = (Tile) getNode(width*y+x);
                Tile up     = (Tile) getNode(width*(y+1)+x);
                Tile right  = (Tile) getNode(width*y+x+1);
                Tile down   = (Tile) getNode(width*(y+-1)+x);
                Tile left   = (Tile) getNode(width*y+x-1);
                if (up != null) {
                    node.createConnection(up,1);
                }
                if (right != null) {
                    node.createConnection(right,1);
                }
                if (down != null) {
                    node.createConnection(down,1);
                }
                if (left != null) {
                    left.createConnection(left,1);
                }
            }
        }
    }

    public Node getNode(int index) {
        if (index < 0 || index >= nodes.size) {
            return null;
        }
        return nodes.get(index);
    }

    @Override
    public int getIndex(Node node) {
        return node.getIndex();
    }

    @Override
    public int getNodeCount() {
        return nodes.size;
    }

    @Override
    public Array<Connection<Node>> getConnections(Node fromNode) {
        return fromNode.getConnections();
    }
}
