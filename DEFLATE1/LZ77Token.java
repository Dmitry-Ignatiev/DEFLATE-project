package DEFLATE1;

/**
 * Represents a token used in the LZ77 compression algorithm.
 * A token consists of a position (offset), length, and a literal symbol.
 */
public class LZ77Token {
    private int pos;  // Offset position
    private int len;  // Length of the match
    private char sym; // Literal symbol

    /**
     * Constructs a new LZ77Token with the specified position, length, and symbol.
     *
     * @param pos the offset position in the dictionary
     * @param len the length of the match
     * @param sym the literal symbol to be added after the match
     */
    public LZ77Token(int pos, int len, char sym) {
        this.pos = pos;
        this.len = len;
        this.sym = sym;
    }
    public String toBinaryString() {
        // Convert pos and len to 8-bit binary strings
        String posBinary = String.format("%8s", Integer.toBinaryString(pos)).replace(' ', '0');
        String lenBinary = String.format("%8s", Integer.toBinaryString(len)).replace(' ', '0');
        
        // Convert sym to 8-bit binary string
        String symBinary = String.format("%8s", Integer.toBinaryString(sym)).replace(' ', '0');
        
        // Combine binary strings: pos + len + sym
        return posBinary + lenBinary + symBinary;
    }
    /**
     * Returns the offset position in the dictionary.
     *
     * @return the offset position
     */
    public int getPos() {
        return pos;
    }

    /**
     * Sets the offset position in the dictionary.
     *
     * @param pos the new offset position
     */
    public void setPos(int pos) {
        this.pos = pos;
    }

    /**
     * Returns the length of the match.
     *
     * @return the length of the match
     */
    public int getLen() {
        return len;
    }

    /**
     * Sets the length of the match.
     *
     * @param len the new length of the match
     */
    public void setLen(int len) {
        this.len = len;
    }

    /**
     * Returns the literal symbol to be added after the match.
     *
     * @return the literal symbol
     */
    public char getSym() {
        return sym;
    }

    /**
     * Sets the literal symbol to be added after the match.
     *
     * @param sym the new literal symbol
     */
    public void setSym(char sym) {
        this.sym = sym;
    }
}
