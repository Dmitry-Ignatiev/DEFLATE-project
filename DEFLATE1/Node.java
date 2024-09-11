package DEFLATE1;
public class Node implements Comparable<Node> {
    char character; // For leaf nodes
    int frequency;
    Node left;
    Node right;
    int leftChildIndex = -1; // Default value when no left child
    int rightChildIndex = -1; // Default value when no right child

    // Constructor for leaf nodes
    public Node(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
    }

    // Constructor for internal nodes
    public Node(int frequency, Node left, Node right) {
        this.character = '\0'; // Not a leaf node
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.frequency, other.frequency);
    }
    public boolean isLeaf() {
        return this.left == null && this.right == null;
    }
    @Override
    public String toString() {
        return "Node{char=" + character + ", freq=" + frequency + '}';
    }
}