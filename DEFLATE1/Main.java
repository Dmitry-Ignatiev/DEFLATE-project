package DEFLATE1;

import java.util.ArrayList;
import java.util.List;



import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Create an instance of DEFLATE
        DEFLATE deflate = new DEFLATE();
        
        // Example input string for testing
        String input = "ABABABABABABABAB";

        // Compress the input string
        byte[] compressedData = deflate.compress(input);
        
        // Print the compressed data (for example, in hex format)
        System.out.println("Compressed Data (Hex):");
        for (byte b : compressedData) {
            System.out.printf("%02X ", b);
        }
        System.out.println();

        // Decompress the data
        String decompressedString = deflate.decompress(compressedData);

        // Print the decompressed string
        System.out.println("\nDecompressed String:");
        System.out.println(decompressedString);

        // Check if the decompressed string matches the original input
        if (input.equals(decompressedString)) {
            System.out.println("\nTest Successful: The decompressed string matches the original input.");
        } else {
            System.out.println("\nTest Failed: The decompressed string does not match the original input.");
        }
    }
}