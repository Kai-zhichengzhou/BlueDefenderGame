package com.bham.bc.entity.graph.algrithem;

import com.bham.bc.entity.graph.SparseGraph;
import com.bham.bc.entity.graph.node.GraphNode;

import java.util.ArrayList;
import java.util.HashSet;

public class Floodfill {


    private ArrayList<GraphNode> startNodes = new ArrayList<GraphNode>();
    private HashSet<GraphNode> registerset = new HashSet<GraphNode>();

    private GraphNode startpoint;

    public Floodfill(GraphNode gh){
        this.startpoint = gh;
        startNodes.add(startpoint);
        registerset.add(gh);
    }

    public SparseGraph stratFLood(SparseGraph graph){

        for(int i = 0; i<startNodes.size();i++){
            GraphNode n1 = startNodes.get(i);
            ArrayList<GraphNode> as1 = pickupNodes(graph.getAroundNodes(n1));
            registerset.addAll(as1);
            startNodes.addAll(as1);
        }
        graph.fitting(registerset);
        return graph;
    }

    private ArrayList<GraphNode> pickupNodes(ArrayList<GraphNode> aa1){
        ArrayList<GraphNode> temp1 = new ArrayList<GraphNode>();
        for(GraphNode g1: aa1){
            if(g1.isValid() && !registerset.contains(g1))

                temp1.add(g1);
        }

        return temp1;
    }

}