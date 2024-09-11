package DEFLATE1;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DEFLATE {
	private LZ77EncoderDecoder lz77EncoderDecoder;
	private Huffman huffman;

	public DEFLATE() {
		this.lz77EncoderDecoder = new LZ77EncoderDecoder();
		this.huffman = new Huffman();

	}
	public byte[] compressFile(File file) throws IOException {
	    try (FileInputStream fis = new FileInputStream(file);
	         BufferedInputStream bis = new BufferedInputStream(fis);
	         ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

	        // Buffer for reading file data
	        byte[] buffer = new byte[1024];
	        int bytesRead;

	        // Read the file into the ByteArrayOutputStream
	        while ((bytesRead = bis.read(buffer)) != -1) {
	            baos.write(buffer, 0, bytesRead);
	        }

	        // Get the byte data from the ByteArrayOutputStream
	        byte[] fileData = baos.toByteArray();

	        // Step 1: LZ77 Compression
	        String fileContent = new String(fileData);
	        List<LZ77Token> tokens = lz77EncoderDecoder.encode(fileContent);

	        // Step 2: Build Huffman Tree and Generate Huffman Codes
	        Map<Character, Integer> frequencyMap = buildFrequencyMap(tokens);
	        int[] frequencyArray = buildFrequencyArray(frequencyMap);
	        huffman.initializeHuffmanCodes(fileContent);

	        // Step 3: Encode Tokens with Huffman Codes
	        List<Integer> tokenValues = convertTokensToListOfValues(tokens);
	        StringBuilder encodedString = new StringBuilder();

	        for (int value : tokenValues) {
	            // Convert the integer value to a string representation (could be a character or binary string)
	            String stringValue = String.valueOf((char) value);

	            // Use Huffman encoding to encode the string representation of the value
	            List<String> huffmanEncodedList = huffman.encode(stringValue);

	            // Debugging information
	            System.out.println("String Value: " + stringValue);
	            System.out.println("Huffman Encoded List: " + huffmanEncodedList);

	            // Check if encoding was successful
	            if (huffmanEncodedList == null || huffmanEncodedList.isEmpty()) {
	                System.err.println("Warning: Empty or null Huffman encoded list for value: " + stringValue);
	                // Use a placeholder or special sequence (if needed)
	                encodedString.append("0000"); // Example placeholder
	                continue;
	            }

	            // Convert the list of integers to a binary string
	            StringBuilder huffmanEncodedString = new StringBuilder();
	            for (String bit : huffmanEncodedList) {
	                huffmanEncodedString.append(bit);
	            }

	            // Append the Huffman encoded string to the final encoded string
	            encodedString.append(huffmanEncodedString);
	        }

	        // Step 4: Convert the Binary String to a Byte Array
	        return stringToByteArray(encodedString.toString());
	    }
	}
	private String convertLZ77TokensToBinaryString(List<LZ77Token> tokens) {
		// Convert LZ77 tokens to binary string
		StringBuilder binaryString = new StringBuilder();
		for (LZ77Token token : tokens) {
			// Convert each token to binary representation
			binaryString.append(token.toBinaryString());
		}
		return binaryString.toString();
	}

	private byte[] convertHuffmanEncodedToByteArray(String huffmanEncoded) {
		// Convert Huffman encoded string to byte array
		int byteCount = (int) Math.ceil(huffmanEncoded.length() / 8.0);
		byte[] bytes = new byte[byteCount];
		for (int i = 0; i < byteCount; i++) {
			int start = i * 8;
			int end = Math.min(start + 8, huffmanEncoded.length());
			String byteString = huffmanEncoded.substring(start, end);
			bytes[i] = (byte) Integer.parseInt(byteString, 2);
		}
		return bytes;
	}

	private String convertByteArrayToHuffmanEncoded(byte[] bytes) {
		// Convert byte array to Huffman encoded string
		StringBuilder huffmanEncoded = new StringBuilder();
		for (byte b : bytes) {
			String byteString = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
			huffmanEncoded.append(byteString);
		}
		return huffmanEncoded.toString();
	}

	private List<LZ77Token> convertBinaryStringToLZ77Tokens(String binaryString) {
		// Convert binary string to LZ77 tokens
		List<LZ77Token> tokens = new ArrayList<>();
		// Implementation depends on how tokens are represented in binary
		return tokens;
	}

	private static Map<Character, Integer> buildFrequencyMap(List<LZ77Token> tokens) {
		Map<Character, Integer> frequencyMap = new HashMap<>();
		for (LZ77Token token : tokens) {
			char symbol = token.getSym();
			frequencyMap.put(symbol, frequencyMap.getOrDefault(symbol, 0) + 1);
		}
		return frequencyMap;
	}

	private static int[] buildFrequencyArray(Map<Character, Integer> frequencyMap) {
		int[] frequencyArray = new int[256];
		for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
			char character = entry.getKey();
			int frequency = entry.getValue();
			frequencyArray[character] = frequency;
		}
		return frequencyArray;
	}

	private List<Integer> convertTokensToListOfValues(List<LZ77Token> tokens) {
		List<Integer> values = new ArrayList<>();
		for (LZ77Token token : tokens) {
			int tokenValue = (token.getPos() << 16) | (token.getLen() << 8) | (token.getSym() & 0xFF);
			values.add(tokenValue);
		}
		return values;
	}

	public byte[] compress(String inputData) {
		// Step 1: LZ77 Compression
		List<LZ77Token> tokens = lz77EncoderDecoder.encode(inputData);
		// Step 2: Build Huffman Tree and Generate Huffman Codes
		Map<Character, Integer> frequencyMap = buildFrequencyMap(tokens);
//		for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
//		    Character key = entry.getKey();
//		    Integer value = entry.getValue();
//		    
//		    // Do something with the key (Character) and value (Integer)
//		    System.out.println("Character: " + key + ", Frequency: " + value);
//		}
		int[] frequencyArray = buildFrequencyArray(frequencyMap);
//		for (int a : frequencyArray)
//			System.out.print(a);
		huffman.initializeHuffmanCodes(inputData);

		// Step 3: Encode Tokens with Huffman Codes
		List<Integer> tokenValues = convertTokensToListOfValues(tokens);
//		for (Integer i:tokenValues)
//		{
//			System.out.print("{"+i+"}");
//		}
		StringBuilder encodedString = new StringBuilder();
		for (int value : tokenValues) {
			List<String> code = huffman.encode(String.valueOf((char) value));
			System.out.print(code);
			encodedString.append(code);
			
		}

		// Step 4: Convert the Binary String to a Byte Array
		return stringToByteArray(encodedString.toString());
	}

//	private byte[] stringToByteArray(String binaryString) {
//	    // Remove non-binary characters
//	    binaryString = binaryString.replaceAll("[^01]", "");
//	    
//	    // Ensure the length is a multiple of 8
//	    if (binaryString.length() % 8 != 0) {
//	        throw new IllegalArgumentException("Binary string length must be a multiple of 8");
//	    }
//
//	    int length = binaryString.length();
//	    byte[] bytes = new byte[length / 8];
//	    
//	    for (int i = 0; i < length; i += 8) {
//	        String byteString = binaryString.substring(i, i + 8);
//	        bytes[i / 8] = (byte) Integer.parseInt(byteString, 2);
//	    }
//	    
//	    return bytes;
//	}
//	private byte[] stringToByteArray(String binaryString) {
//	    // Remove non-binary characters
//	    binaryString = binaryString.replaceAll("[^01]", "");
//
//	    // Ensure the length is a multiple of 8
//	    if (binaryString.length() % 8 != 0) {
//	        throw new IllegalArgumentException("Binary string length must be a multiple of 8. Provided length: " + binaryString.length());
//	    }
//
//	    int length = binaryString.length();
//	    byte[] bytes = new byte[length / 8];
//
//	    for (int i = 0; i < length; i += 8) {
//	        String byteString = binaryString.substring(i, i + 8);
//
//	        // Validate that the byteString contains only '0' or '1'
//	        if (!byteString.matches("[01]+")) {
//	            throw new IllegalArgumentException("Invalid binary string segment: " + byteString);
//	        }
//
//	        // Convert the binary string segment to a byte
//	        try {
//	            bytes[i / 8] = (byte) Integer.parseInt(byteString, 2);
//	        } catch (NumberFormatException e) {
//	            throw new IllegalArgumentException("Failed to parse binary string segment: " + byteString, e);
//	        }
//	    }
//
//	    return bytes;
//	}
	public String buildBinaryString(String binaryString) {
	    StringBuilder binaryStringBuilder = new StringBuilder();

	    // Loop over the binary string in segments of 8 bits (1 byte)
	    for (int i = 0; i < binaryString.length(); i += 8) {
	        String byteSegment = binaryString.substring(i, i + 8); // Extract a segment of 8 bits
	        binaryStringBuilder.append(byteSegment);
	    }

	    String finalBinaryString = binaryStringBuilder.toString();
	    System.out.println("Binary string: " + finalBinaryString);  // Log the constructed string
	    return finalBinaryString;
	}
	public byte[] stringToByteArray(String binaryString) {
	    // Log the initial input binary string
	    System.out.println("Initial binary string: " + binaryString);

	    // Remove non-binary characters
	    binaryString = binaryString.replaceAll("[^01]", "");
	    
	    // Log the sanitized binary string
	    System.out.println("Sanitized binary string: " + binaryString);

	    // Ensure the length is a multiple of 8
	    if (binaryString.length() % 8 != 0) {
	        throw new IllegalArgumentException("Binary string length must be a multiple of 8. Provided length: " + binaryString.length());
	    }

	    int length = binaryString.length();
	    byte[] bytes = new byte[length / 8];

	    // Process the binary string in chunks of 8 bits
	    for (int i = 0; i < length; i += 8) {
	        String byteString = binaryString.substring(i, i + 8);
	        System.out.println("Processing byte segment: " + byteString);

	        try {
	            // Convert binary string segment to byte
	            bytes[i / 8] = (byte) Integer.parseInt(byteString, 2);
	        } catch (NumberFormatException e) {
	            throw new IllegalArgumentException("Failed to parse binary string segment: " + byteString, e);
	        }
	    }

	    // Log the final byte array
	    System.out.println("Final byte array: " + Arrays.toString(bytes));
	    return bytes;
	}


	public String decompress(byte[] compressedData) {
		BitSet bitSet = BitSet.valueOf(compressedData);
		String encodedText = bitSetToString(bitSet);

		// Decode Huffman Encoded Data to Token Values
		List<String> decodedValues = decodeHuffman(encodedText);

		// Convert integer values to LZ77 tokens
		List<LZ77Token> lz77Tokens = new ArrayList<>();
		for (String value : decodedValues) {
		    // Convert the String to an integer
		    int intValue = Integer.parseInt(value);

		    // Extract the position, length, and symbol using bitwise operations
		    int pos = (intValue >> 16) & 0xFFFF;
		    int len = (intValue >> 8) & 0xFF;
		    char sym = (char) (intValue & 0xFF);

		    // Add the new LZ77Token to the list
		    lz77Tokens.add(new LZ77Token(pos, len, sym));
		}
		// LZ77 decode
		return lz77EncoderDecoder.decodeLZ77(lz77Tokens);
	}

	private static BitSet stringToBitSet(String binaryString) {
		BitSet bitSet = new BitSet(binaryString.length());
		for (int i = 0; i < binaryString.length(); i++) {
			if (binaryString.charAt(i) == '1') {
				bitSet.set(i);
			}
		}
		return bitSet;
	}

	private static String bitSetToString(BitSet bitSet) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bitSet.length(); i++) {
			sb.append(bitSet.get(i) ? '1' : '0');
		}
		return sb.toString();
	}

	private ArrayList<String> decodeHuffman(String encodedText) {
		return new ArrayList<>(huffman.encode(encodedText)); // Implement Huffman decoding logic here
	}

    public static double calculateEntropy(String data) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        int totalSymbols = data.length();

        // Count frequencies of each symbol
        for (char c : data.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }

        double entropy = 0.0;

        // Calculate entropy
        for (int frequency : frequencyMap.values()) {
            double probability = (double) frequency / totalSymbols;
            entropy -= probability * Math.log(probability) / Math.log(2); // log base 2
        }

        return entropy;
    }
    public static void main(String[] args) {
        String testBinaryString = "0101100110011001100110011001100110001111011101101110011100011000";
        DEFLATE deflate = new DEFLATE(); // Assuming DEFLATE is your class
        byte[] result = deflate.stringToByteArray(testBinaryString);

        // Print each byte and its corresponding binary representation for verification
        for (byte b : result) {
            System.out.println("Byte value: " + b + " (Binary: " + String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0') + ")");
        }
    }
}
