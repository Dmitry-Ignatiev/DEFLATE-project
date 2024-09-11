package DEFLATE1;

import java.util.Arrays;

public class SlidingWindow<T> {
    private static final int SEARCH_BUFFER_SIZE = 32768; // 32 KB
    private static final int LOOK_AHEAD_BUFFER_SIZE = 16384; // 16 KB

    private final int searchBufferSize;
    private final int lookAheadBufferSize;
    private T[] searchBuffer;
    private T[] lookAheadBuffer;
    private int searchIndex = 0;
    private int lookAheadIndex = 0;

    @SuppressWarnings("unchecked")
    public SlidingWindow() {
        this.searchBufferSize = SEARCH_BUFFER_SIZE;
        this.lookAheadBufferSize = LOOK_AHEAD_BUFFER_SIZE;
        this.searchBuffer = (T[]) new Object[searchBufferSize];
        this.lookAheadBuffer = (T[]) new Object[lookAheadBufferSize];
    }

    // Add element to the look-ahead buffer
    public void addToLookAhead(T element) {
        if (lookAheadIndex >= lookAheadBufferSize) {
            slideRight();  // Slide the window when look-ahead buffer is full
        }
        lookAheadBuffer[lookAheadIndex++] = element;
    }

    // Slide the window to the right
    public void slideRight() {
        if (lookAheadIndex > 0) {
            // Move the first element from the look-ahead buffer to the search buffer
            T element = lookAheadBuffer[0];

            // Shift all elements in look-ahead buffer to the left
            for (int i = 1; i < lookAheadIndex; i++) {
                lookAheadBuffer[i - 1] = lookAheadBuffer[i];
            }
            lookAheadIndex--;

            // Add the moved element to the search buffer
            if (searchIndex >= searchBufferSize) {
                // Shift search buffer to the left if it's full
                for (int i = 1; i < searchBufferSize; i++) {
                    searchBuffer[i - 1] = searchBuffer[i];
                }
                searchIndex--;
            }
            searchBuffer[searchIndex++] = element;
        }
    }

    // Get search Buffer
    public T[] getSearchBuffer() {
        return Arrays.copyOf(searchBuffer, searchIndex);
    }
    //get LookAheadBuffer
    public T[] getLookAheadBuffer() {
        return Arrays.copyOf(lookAheadBuffer, lookAheadIndex);
    }
}
