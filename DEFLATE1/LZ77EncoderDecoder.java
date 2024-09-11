package DEFLATE1;

import java.util.ArrayList;
import java.util.List;

public class LZ77EncoderDecoder {
    private static final int WINDOW_SIZE = 32768;
    private static final int DICT_SIZE = 256;
    private static final int BUFFER_SIZE = 32;

    private List<Character> _dict;
    private int _dictStart;

    public LZ77EncoderDecoder() {
        // Initialize dictionary with null characters
        _dict = new ArrayList<>(DICT_SIZE);
        for (int i = 0; i < DICT_SIZE; i++) {
            _dict.add((char) 0);
        }
        _dictStart = 0; // Initialize the starting index of the dictionary
    }

    private synchronized void slideToDictionary(char c) {
        _dict.set(_dictStart, c);
        _dictStart = (_dictStart + 1) % DICT_SIZE;
    }

    public List<LZ77Token> encode(String input) {
        List<LZ77Token> tokens = new ArrayList<>();
        int n = input.length();
        int position = 0;

        while (position < n) {
            int maxLength = 0;
            int bestOffset = 0;
            char nextSymbol = '\0';

            int searchStart = Math.max(0, position - WINDOW_SIZE);
            int searchEnd = position;
            int lookAheadEnd = Math.min(n, position + BUFFER_SIZE);

            for (int i = searchStart; i < searchEnd; i++) {
                int length = 0;
                while (position + length < lookAheadEnd && input.charAt(i + length) == input.charAt(position + length)) {
                    length++;
                }

                if (length > maxLength) {
                    maxLength = length;
                    bestOffset = position - i;
                    if (position + length < n) {
                        nextSymbol = input.charAt(position + length);
                    }
                }
            }

            if (maxLength == 0) {
                nextSymbol = input.charAt(position);
                tokens.add(new LZ77Token(0, 0, nextSymbol));
                slideToDictionary(nextSymbol);  // Update dictionary with literal
                position++;
            } else {
                tokens.add(new LZ77Token(bestOffset, maxLength, nextSymbol));
                // Add the substring and next symbol to the dictionary
                for (int i = 0; i < maxLength; i++) {
                    slideToDictionary(input.charAt(position + i));
                }
                slideToDictionary(nextSymbol);
                position += maxLength + 1;  // Move position by length of match + 1 symbol
            }
        }

        return tokens;
    }

    public String decodeLZ77(List<LZ77Token> tokens) {
        StringBuilder output = new StringBuilder();

        for (LZ77Token token : tokens) {
            if (token.getLen() == 0) {
                output.append(token.getSym());
                slideToDictionary(token.getSym());
            } else {
                int startIdx = (_dictStart + DICT_SIZE - token.getPos()) % DICT_SIZE;
                for (int i = 0; i < token.getLen(); i++) {
                    char symbol = _dict.get((startIdx + i) % DICT_SIZE);
                    output.append(symbol);
                    slideToDictionary(symbol);
                }
                output.append(token.getSym());
                slideToDictionary(token.getSym());
            }
        }

        return output.toString();
    }

    public static void main(String[] args) {
        LZ77EncoderDecoder lz77 = new LZ77EncoderDecoder();

        // Test case 1: Simple input
        String input1 = "ABABABA";
        List<LZ77Token> encoded1 = lz77.encode(input1);
        String decoded1 = lz77.decodeLZ77(encoded1);
        assert input1.equals(decoded1) : "Test case 1 failed";

        // Test case 2: Empty input
        String input2 = "";
        List<LZ77Token> encoded2 = lz77.encode(input2);
        String decoded2 = lz77.decodeLZ77(encoded2);
        assert input2.equals(decoded2) : "Test case 2 failed";

        // Test case 3: Input with no repetitions
        String input3 = "ABCDEF";
        List<LZ77Token> encoded3 = lz77.encode(input3);
        String decoded3 = lz77.decodeLZ77(encoded3);
        assert input3.equals(decoded3) : "Test case 3 failed";

        // Test case 4: Longer input with patterns
        String input4 = "AABACABA";
        List<LZ77Token> encoded4 = lz77.encode(input4);
        String decoded4 = lz77.decodeLZ77(encoded4);
        assert input4.equals(decoded4) : "Test case 4 failed";

        System.out.println("All test cases passed!");
    }
}
