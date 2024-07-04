// Modified from source: https://ianfinlayson.net/class/cpsc340/samples/Dijkstras.java

// this heap is used to keep track of the nodes used by
// Dijkstra's algorithm -- it stores the node index and the
// tentative cost
class NodeHeap {
    private class Node {
        public int index;
        public int tentative_cost;
        
        public Node(int index, int tentative_cost) {
            this.index = index;
            this.tentative_cost = tentative_cost;
        }
    }

    private Node[] array;
    private int count;

    // start with nothing
    public NodeHeap(int size) {
        array = new Node[size];
        count = 0;
    }

    // find the left child of a node
    public int left(int node) {
        return 2 * node  + 1;
    }

    // find the right child of a node
    public int right(int node) {
        return 2 * node  + 2; 
    }

    // find the parent of a node
    public int parent(int node) {
        return (node - 1) / 2;
    }

    // insert an item into the heap
    public void insert(int index, int tentative_cost) {
        // put item in next available slot
        array[count] = new Node(index, tentative_cost);

        // keep indices into node we inserted and its parent
        int node = count;
        int p = parent(node);

        // increment total number of items
        count++;

        // upheap until it's root, or parent is smaller (using cost)
        while ((node != 0) && (array[node].tentative_cost < array[p].tentative_cost)) {
            // swap values
            Node temp = array[node];
            array[node] = array[p];
            array[p] = temp;

            // update indices
            node = p;
            p = parent(node);
        }
    }

    // remove the top item -- returns the index of the node
    public int dequeue() {
        // set aside root
        int retValue = array[0].index;

        // move last value to the root
        array[0] = array[count - 1];
        count--;

        // find the children
        int node = 0;
        int l = left(node);
        int r = right(node);

        // while one child is smaller than us
        while ((l < count) && (array[l].tentative_cost < array[node].tentative_cost) ||
               (r < count) && (array[r].tentative_cost < array[node].tentative_cost)) {

            // find smallest child
            int min;
            if (l < count && r < count) {
                if (array[l].tentative_cost < array[r].tentative_cost) {
                    min = l;
                } else {
                    min = r;
                }
            } else {
                min = l;
            }

            // swap with the smallest child
            Node temp = array[node];
            array[node] = array[min];
            array[min] = temp;

            // update indices
            node = min;
            l = left(node);
            r = right(node);
        }
        
        // return orig. root
        return retValue;
    }

    // return the smallest item in the heap
    public int min() {
        return array[0].index;
    }

    public int getCount() {
        return count;
    }
}


class Dijkstras {
    // use Dijkstra's algorithm to find a shortest path
    static int shortestPath(Graph graph, Point startpoint, Point endpoint) {
        // get the point indices
        int start = startpoint.getIndex();
        int end = endpoint.getIndex();

        // the size of the graph
        int N = graph.getSize();

        // keep track of the tentative distances
        int[] tentative = new int[N];
        for (int i = 0; i < N; i++) {
            tentative[i] = Integer.MAX_VALUE;
        }
        tentative[start] = 0;

        // make the heap of nodes we are using
        NodeHeap nodes = new NodeHeap(N);

        // insert the start point with cost 0
        nodes.insert(start, 0);
        
        // while our heap of nodes isn't empty
        while (nodes.getCount() > 0) {
            // get the next node to consider
            int current = nodes.dequeue();

            // for each neighbor of the current node
            for (int neighbor : graph.getNeighbors(current)) {

                // calculate distance from start to neighbor through current
                int distance = tentative[current] + graph.getCost(current, neighbor);

                // if this is less than the what we have so far
                if (distance < tentative[neighbor]) {
                    // update the distance
                    tentative[neighbor] = distance;

                    // add it to the heap (it may already be there w/ a bigger cost)
                    nodes.insert(neighbor, distance);
                }
            }
        }

        // return the tentative cost to the final node (which is now official)
        return tentative[end];
    }
}
