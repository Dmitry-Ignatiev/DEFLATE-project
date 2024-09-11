//public class FileCompressor {
//
//    public byte[] compressFile(File file, DEFLATE deflate) throws IOException {
//        // Try-with-resources statement ensures that the file input streams are closed automatically
//        try (FileInputStream fis = new FileInputStream(file);
//             BufferedInputStream bis = new BufferedInputStream(fis);
//             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
//
//            // Create a buffer to hold chunks of file data
//            byte[] buffer = new byte[1024]; // Buffer size of 1024 bytes
//            int bytesRead; // This will store the number of bytes read from the file
//
//            // Read the file in chunks into the buffer, and write each chunk to the ByteArrayOutputStream
//            while ((bytesRead = bis.read(buffer)) != -1) {
//                baos.write(buffer, 0, bytesRead);
//            }
//
//            // Now that the ByteArrayOutputStream contains the full content of the file, compress it
//            byte[] compressedData = deflate.compress(baos.toByteArray());
//
//            // Return the compressed byte array
//            return compressedData;
//        }
//    }