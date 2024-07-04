import java.util.ArrayList;

// unweighted, undirected graph
class Graph<Type> {
    private boolean[][] matrix;
    private Type[] values;
    private int size;

    Graph(int size) {
        matrix = new boolean[size][size];
        this.size = size;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = false;
            }
        }

        @SuppressWarnings("unchecked")
        Type[] values = (Type[]) new Object[size];
        this.values = values;
    }

    int lookup(Type value) {
        for (int i = 0; i < size; i++) {
            if (values[i] != null && values[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }

    void insertEdge(int from, int to) {
        matrix[from][to] = true;
        matrix[to][from] = true;
    }

    void insertEdge(Type from, Type to) {
        int fromIndex = lookup(from);
        int toIndex = lookup(to);
        insertEdge(fromIndex, toIndex);
    }

    void removeEdge(int from, int to) {
        matrix[from][to] = false;
        matrix[to][from] = false;
    }

    boolean isEdge(int from, int to) {
        return matrix[from][to];
    }

    boolean isEdge(Type from, Type to) {
        int fromIndex = lookup(from);
        int toIndex = lookup(to);
        return isEdge(fromIndex, toIndex);
    }

    void setValue(int node, Type value) {
        values[node] = value;
    }

    int getSize() {
        return size;
    }

    Type getValue(int index) {
        return values[index];
    }

    ArrayList<Type> getAdjacentNodes(Type value) {
        ArrayList<Type> nodes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (!values[i].equals(value) && isEdge(values[i], value)) {
                nodes.add(values[i]);
            }
        }
        return nodes;
    }
}
