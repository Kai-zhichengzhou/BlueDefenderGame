package com.bham.bc.entity.graph.edge;

import java.io.OutputStream;
import java.io.PrintStream;

import static com.bham.bc.entity.graph.NodeTypeEnum.invalid_node_index;

public class GraphEdge {

    //An edge connects two nodes. Valid node indices are always positive.
    protected int m_iFrom;
    protected int m_iTo;
    //the cost of traversing the edge
    protected double m_dCost;

    final public static int normal = 0;
    final public static int shoot = 1;

    public void setBehavior(int behavior) {
        this.behavior = behavior;
    }

    public int getBehavior() {
        return behavior;
    }

    protected int behavior;

    //ctors
    public GraphEdge(int from, int to, double cost) {
        m_dCost = cost;
        m_iFrom = from;
        m_iTo = to;
        behavior = normal;
    }

    public GraphEdge(int from, int to) {
        m_dCost = 1.0;
        m_iFrom = from;
        m_iTo = to;
        behavior = normal;
    }

    public GraphEdge() {
        m_dCost = 16.0;                                      // should be the value between two points
        m_iFrom =invalid_node_index;                        // invalid_node_index;
        m_iTo = invalid_node_index ;                        //invalid_node_index;
        behavior = normal;
    }

    public int From() {
        return m_iFrom;
    }

    public void SetFrom(int NewIndex) {
        m_iFrom = NewIndex;
    }

    public int To() {
        return m_iTo;
    }

    public void SetTo(int NewIndex) {
        m_iTo = NewIndex;
    }

    public double Cost() {
        return m_dCost;
    }

    public void SetCost(double NewCost) {
        m_dCost = NewCost;
    }

    //these two operators are required
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof GraphEdge)) {
            return false;
        }
        GraphEdge rhs = (GraphEdge) o;
        return rhs.m_iFrom == this.m_iFrom
                && rhs.m_iTo == this.m_iTo
                && rhs.m_dCost == this.m_dCost;
    }

    //for reading and writing to streams.
    public OutputStream print(OutputStream os) {
        PrintStream ps = new PrintStream(os);
        ps.print("m_iFrom: ");
        ps.print(m_iFrom);
        ps.print(" m_iTo: ");
        ps.print(m_iTo);
        ps.print(" m_dCost: ");
        ps.print(m_dCost);
        ps.println();
        return os;
    }
}
