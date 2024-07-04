import java.util.LinkedList;

// weighted, directed graph
class Graph {
    private class Edge {
        public int to;
        public int cost;

        public Edge(int to, int cost) {
            this.to = to;
            this.cost = cost;
        }

        public boolean equals(Object other) {
            Edge otherEdge = (Edge) other;
            return to == otherEdge.to;
        }
    }
    
    private LinkedList<Edge>[] nodes;   // incidence list
    private int size;

    Graph(int size) {
        nodes = (LinkedList<Edge>[]) new LinkedList[size];
        this.size = size;
        for (int i = 0; i < size; i++) {
            nodes[i] = new LinkedList<Edge>();
        }
    }
    
    void insertEdge(int from, int to, int cost) {
        nodes[from].add(new Edge(to, cost));
    }
    
    void removeEdge(int from, int to) {
        nodes[from].remove(new Edge(to, -1));
    }

    boolean isEdge(int from, int to) {
        return getCost(from, to) < Integer.MAX_VALUE;
    }

    int getCost(int from, int to) {
        int index = nodes[from].indexOf(new Edge(to, -1));
        if (index == -1) {
            return Integer.MAX_VALUE;
        }
        return nodes[from].get(index).cost;
    }

    int getSize() {
        return size;
    }

    int[] getNeighbors(int from) {
        int[] neighbors = new int[nodes[from].size()];
        for (int i = 0; i < nodes[from].size(); i++) {
            neighbors[i] = nodes[from].get(i).to;
        }
        return neighbors;
    }
}
