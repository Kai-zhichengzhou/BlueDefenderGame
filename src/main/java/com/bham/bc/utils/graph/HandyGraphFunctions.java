package com.bham.bc.utils.graph;

import com.bham.bc.utils.graph.algrithem.Floodfill;
import com.bham.bc.utils.graph.edge.GraphEdge;
import com.bham.bc.utils.graph.node.GraphNode;
import com.bham.bc.utils.graph.node.NavNode;
import com.bham.bc.utils.graph.node.Vector2D;

import java.util.ArrayList;

import static com.bham.bc.utils.graph.node.Vector2D.Vec2DDistance;

public class HandyGraphFunctions {



    /**
     * @return true if x,y is a valid position in the map
     */
    public static boolean ValidNeighbour(int x, int y, int NumCellsX, int NumCellsY) {
        return !((x < 0) || (x >= NumCellsX) || (y < 0) || (y >= NumCellsY));
    }

    /**
     * 111
     *  use to add he eight neighboring edges of a graph node that
     *  is positioned in a grid layout
     */
    public static <graph_type extends SparseGraph> void GraphHelper_AddAllNeighboursToGridNode(graph_type graph,
                                                                                               int row,
                                                                                               int col,
                                                                                               int NumCellsX,
                                                                                               int NumCellsY) {

        for (int i = -1; i < 2; ++i) {
            for (int j = -1; j < 2; ++j) {
                int nodeX = col + j;
                int nodeY = row + i;

                //skip if equal to this node
                if (!(i+j==1)) {                 ///changajhsdkjahadskjsdhkdshaksdahkjdsjkdshkjasdhkj!!
                    continue;
                }

                //check to see if this is a valid neighbour
                if (ValidNeighbour(nodeX, nodeY, NumCellsX, NumCellsY)) {
                    //calculate the distance to this node
                    Vector2D PosNode = graph.GetNode(row * NumCellsX + col).Pos();
                    Vector2D PosNeighbour = graph.GetNode(nodeY * NumCellsX + nodeX).Pos();

                    double dist = PosNode.Distance(PosNeighbour);

                    //this neighbour is okay so it can be added
                    GraphEdge NewEdge = new GraphEdge(row * NumCellsX + col,
                            nodeY * NumCellsX + nodeX,
                            dist);
                    graph.AddEdge(NewEdge);

                    //if graph is not a diagraph then an edge needs to be added going
                    //in the other direction
                    if (!graph.isDigraph()) {
                        NewEdge = new GraphEdge(nodeY * NumCellsX + nodeX,
                                row * NumCellsX + col,
                                dist);
                        graph.AddEdge(NewEdge);
                    }
                }
            }
        }
    }

    /**
     * creates a graph based on a grid layout. This function requires the
     * dimensions of the environment and the number of cells required horizontally
     * and vertically
     */
    public static <graph_type extends SparseGraph> void GraphHelper_CreateGrid(graph_type graph,
                                                                               int cySize,  //窗口长款
                                                                               int cxSize,
                                                                               int NumCellsY,       //建立数量
                                                                               int NumCellsX) {

        graph.SparseGraph(NumCellsX,NumCellsY,cxSize/NumCellsX,cySize/NumCellsY);
        //need some temporaries to help calculate each node center
        double CellWidth = (double) cySize / (double) NumCellsX;
        double CellHeight = (double) cxSize / (double) NumCellsY;

        double midX = CellWidth / 2;
        double midY = CellHeight / 2;


        //first create all the nodes
        for (int row = 0; row < NumCellsY; ++row) {
            for (int col = 0; col < NumCellsX; ++col) {
                graph.AddNode(new NavNode(graph.GetNextFreeNodeIndex(),
                        new Vector2D(midX + (col * CellWidth),
                                midY + (row * CellHeight))));

            }
        }
        //now to calculate the edges. (A position in a 2d array [x][y] is the
        //same as [y*NumCellsX + x] in a 1d array). Each cell has up to eight
        //neighbours.
        for (int row = 0; row < NumCellsY; ++row) {
            for (int col = 0; col < NumCellsX; ++col) {
                GraphHelper_AddAllNeighboursToGridNode(graph, row, col, NumCellsX, NumCellsY);
            }
        }
    }

    /**
     *
     * @param sg the Sparse graph on the mao
     * @param gn the node which tricking the player
     * @return Sparse graph by using SparseGraph
     */
    public SparseGraph FLoodFill(SparseGraph sg, GraphNode gn){
        Floodfill fl = new Floodfill(gn);

        return fl.stratFLood(sg);
    }



    /**
     * Given a cost value and an index to a valid node this function examines
     * all a node's edges, calculates their length, and multiplies
     * the value with the weight. Useful for setting terrain costs.
     */
    public static <graph_type extends SparseGraph>
    void WeightNavGraphNodeEdges(graph_type graph, int node, double weight) {
        //make sure the node is present
        assert (node < graph.NumNodes());

        //set the cost for each edge
        graph_type.EdgeIterator ConstEdgeItr = new graph_type.EdgeIterator(graph, node);
        for (GraphEdge pE = ConstEdgeItr.begin();
             !ConstEdgeItr.end();
             pE = ConstEdgeItr.next()) {
            //calculate the distance between nodes
            double dist = Vec2DDistance(graph.GetNode(pE.From()).Pos(),
                    graph.GetNode(pE.To()).Pos());

            //set the cost of this edge
            graph.SetEdgeCost(pE.From(), pE.To(), dist * weight);

            //if not a digraph, set the cost of the parallel edge to be the same
            if (!graph.isDigraph()) {
                graph.SetEdgeCost(pE.To(), pE.From(), dist * weight);
            }
        }
    }





}
