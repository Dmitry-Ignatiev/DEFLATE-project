package DEFLATE1;

import java.util.*;

public class Huffman {
    public static final int MAX_BITS = 256;
    private String[] huffmanCodesArray;
    private Node huffmanTreeRoot;
        public Huffman() {
        this.huffmanCodesArray = new String[MAX_BITS];
        this.huffmanTreeRoot = null;
        
    }

    // Initialize Huffman codes based on input text
    public void initializeHuffmanCodes(String text) {
        int[] frequencyArray = calculateFrequencies(text);
        Node root = buildHuffmanTree(frequencyArray);
        this.huffmanTreeRoot = root;
        generateHuffmanCodes(root, "", this.huffmanCodesArray);
    }

    // Generate Huffman codes for each character
    // QuickSort implementation
    private void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }
    private int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

  
    // Generate Huffman codes for each character
    private void generateHuffmanCodes(Node node, String code, String[] huffmanCodesArray) {
        if (node==null) return;
        if (node.isLeaf()) {
            huffmanCodesArray[node.character] = code;
        } else {
            if (node.left != null) {
                generateHuffmanCodes(node.left, code + '0', huffmanCodesArray);
            }
            if (node.right != null) {
                generateHuffmanCodes(node.right, code + '1', huffmanCodesArray);
            }
        }
    }    // Calculate frequencies of characters in the text
    private int[] calculateFrequencies(String text) {
        int[] freq = new int[MAX_BITS];
        for (char c : text.toCharArray()) {
            freq[c & 0xFF]++;
        }
        return freq;
    }

    // Method to convert binary string to integer
    public int binaryStringToInt(String binaryString) {
        if (binaryString == null || binaryString.isEmpty()) {
            // Return a default value or handle the error appropriately
            return 0; // Or another value that makes sense for your use case
        }
        // Ensure the string contains only '0' and '1' characters
        if (!binaryString.matches("[01]+")) {
            throw new IllegalArgumentException("Invalid characters in binary string: " + binaryString);
        }
        return Integer.parseInt(binaryString, 2);
    }
    // Encode the text into a list of integers
    public List<String> encode(String text) {
        initializeHuffmanCodes(text); // Ensure Huffman codes are initialized
        List<String>  encodedList = new ArrayList<>();

        // Encode each character in the text
        for (char c : text.toCharArray()) {
            String code = huffmanCodesArray[c & 0xFF];//bitwise operation to turn it into integer
            if (code == null) {
                throw new IllegalStateException("Huffman code not initialized for character: " + c);
            }
            
            // Convert binary string to integer
            int encodedVal;
            String encodedRep;
            try {
                encodedRep = code;
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Invalid binary string for character: " + c + " with code: " + code, e);
            }
            
            encodedList.add(encodedRep);
        }

        return encodedList;
    }

    // Decode the binary string using Huffman codes
//    public String decodeText(String encodedText) {
//        StringBuilder decoded = new StringBuilder();
//        Node currentNode = this.huffmanTreeRoot;
//        for (char bit : encodedText.toCharArray()) {
//            if (currentNode == null) {
//                throw new IllegalStateException("Huffman tree root is null.");
//            }
//            currentNode = (bit == '0') ? currentNode.left : currentNode.right;
//            if (currentNode.isLeaf()) {
//                decoded.append(currentNode.character);
//                currentNode = this.huffmanTreeRoot; // Start from root again
//            }
//        }
//        return decoded.toString();
//    }

    public ArrayList<Character> decodeText(String encodedText) {
        ArrayList<Character> decoded = new ArrayList<>();
        Node currentNode = this.huffmanTreeRoot;

        for (char bit : encodedText.toCharArray()) {
            if (currentNode == null) {
                throw new IllegalStateException("Huffman tree root is null.");
            }
            currentNode = (bit == '0') ? currentNode.left : currentNode.right;

            if (currentNode.isLeaf()) {
                decoded.add(currentNode.character);
                currentNode = this.huffmanTreeRoot; // Start from root again
            }
        }

        return decoded;
    }
    // Build the Huffman tree based on character frequencies
    private Node buildHuffmanTree(int[] freq) {
        PriorityQueue<Node> pq = new PriorityQueue<>();

        // Create nodes for each character with non-zero frequency
        for (int i = 0; i < freq.length; i++) {
            if (freq[i] > 0) {
                pq.add(new Node((char) i, freq[i]));
            }
        }

        // Build the Huffman tree
        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            Node parent = new Node(left.frequency + right.frequency, left, right);
            pq.add(parent);
        }

        return pq.poll(); // Root node
    }

    // Print Huffman codes for each character
    public void printHuffmanCodes() {
        for (int i = 0; i < huffmanCodesArray.length; i++) {
            if (huffmanCodesArray[i] != null) {
                char character = (char) i;
                String code = huffmanCodesArray[i];
                System.out.println("Character: " + character + " Code: " + code);
            }
        }
    }

    // Main method for testing
    public static void main(String[] args) {
        Huffman huffman = new Huffman();

        // Example text to encode
        String text = "example";

        // Initialize Huffman codes and print them
        huffman.initializeHuffmanCodes(text);
        huffman.printHuffmanCodes();

        // Encode the text into a list of integers
        List<String> encodedList = huffman.encode(text);
        System.out.println("Encoded list: " + encodedList);

        // Convert encoded list to binary string for visualization
        StringBuilder encodedBinaryString = new StringBuilder();
        for (String value : encodedList) {
            encodedBinaryString.append(value.replace(' ', '0'));
        }
        System.out.println("Encoded text: " + encodedBinaryString.toString());

        // Decode the binary string back to text
        ArrayList<Character> decodedText = huffman.decodeText(encodedBinaryString.toString());
        System.out.println("Decoded text: " + decodedText);
    }
}
