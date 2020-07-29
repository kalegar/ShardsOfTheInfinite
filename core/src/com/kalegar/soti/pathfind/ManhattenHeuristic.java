package com.kalegar.soti.pathfind;

import com.badlogic.gdx.ai.pfa.Heuristic;
import com.kalegar.soti.util.Utils;

public class ManhattenHeuristic implements Heuristic<Node> {

    private static ManhattenHeuristic instance;

    private ManhattenHeuristic() {

    }

    public static ManhattenHeuristic getInstance() {
        if (instance == null) {
            instance = new ManhattenHeuristic();
        }
        return instance;
    }

    @Override
    public float estimate(Node node, Node endNode) {
        return Utils.manhattenDistance(node.x,node.y,endNode.x,endNode.y);
    }
}
