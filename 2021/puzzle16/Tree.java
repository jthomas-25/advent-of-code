import java.util.LinkedList;
import java.util.Collections;

class Tree<Type> {
    class Node {
        protected Type data;
        protected LinkedList<Node> children;

        protected Node(Type data) {
            this.data = data;
            children = null;
        }
    }

    protected Node root;
    private static final int PRINT_SPACE_WIDTH = 4;

    Tree() {
        root = null;
    }

    boolean isEmpty() {
        return root == null;
    }

    Node insertAt(Type value, Node node) {
        if (node == null) {
            root = new Node(value);
            return root;
        }
        if (node.data.equals(value)) {
            return node;
        }
        if (node.children == null) {
            node.children = new LinkedList<>();
        }
        for (Node child : node.children) {
            if (child.data.equals(value)) {
                return child;
            }
        }
        Node child = new Node(value);
        node.children.add(child);
        return child;
    }
    
    private Node searchAt(Type target, Node node) {
        if (node == null) {
            return null;
        }
        if (node.data.equals(target)) {
            return node;
        }
        if (node.children == null) {
            return null;
        }
        for (Node child : node.children) {
            Node match = searchAt(target, child);
            if (match != null) {
                return match;
            }
        }
        return null;
    }

    Type search(Type target) {
        Node node = searchAt(target, root);
        return (node != null) ? node.data : null;
    }

    boolean contains(Type value) {
        return search(value) != null;
    }
    
    private void preorderPrint(Node node, int depth, String spaces) {
        if (node != null) {
            if (depth > 0) {
                for (int i = 0; i < PRINT_SPACE_WIDTH; i++) {
                    spaces += " ";
                }
            }
            System.out.println(spaces + node.data + " ");
            if (node.children != null) {
                for (Node child : node.children) {
                    preorderPrint(child, depth+1, spaces);
                }
            }
        }
    }
    
    void print() {
        preorderPrint(root, 0, "");
    }
}

class PacketTree extends Tree<Packet> {

    private int versionSum(Node node) {
        if (node != null) {
            int sum = 0;
            sum += node.data.getVersion();
            if (node.children != null) {
                for (Node child : node.children) {
                    sum += versionSum(child);
                }
            }
            return sum;
        }
        return -1;
    }
    
    int versionSum() {
        return versionSum(root);
    }

    private long evaluate(Node node) {
        if (node != null) {
            LinkedList<Long> values;
            switch (node.data.getTypeId()) {
                case 0:
                    long sum = 0;
                    for (Node child : node.children) {
                        sum += evaluate(child);
                    }
                    return sum;
                case 1:
                    long product = 1;
                    for (Node child : node.children) {
                        product *= evaluate(child);
                    }
                    return product;
                case 2:
                    values = new LinkedList<>();
                    for (Node child : node.children) {
                        values.add(evaluate(child));
                    }
                    return Collections.min(values);
                case 3:
                    values = new LinkedList<>();
                    for (Node child : node.children) {
                        values.add(evaluate(child));
                    }
                    return Collections.max(values);
                case 4:
                    return node.data.getValue();
                case 5:
                    return (evaluate(node.children.get(0)) > evaluate(node.children.get(1))) ? 1 : 0;
                case 6:
                    return (evaluate(node.children.get(0)) < evaluate(node.children.get(1))) ? 1 : 0;
                case 7:
                    return (evaluate(node.children.get(0)) == evaluate(node.children.get(1))) ? 1 : 0;
            }
        }
        return -1;
    }

    long evaluate() {
        return evaluate(root);
    }
}
