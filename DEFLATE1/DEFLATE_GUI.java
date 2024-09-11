////package DEFLATE1;
////
////import java.awt.*;
////import java.awt.event.ActionEvent;
////import java.awt.event.ActionListener;
////import java.io.*;
////import java.nio.ByteBuffer;
////import java.nio.CharBuffer;
////import java.nio.charset.CharsetDecoder;
////import java.nio.charset.CoderResult;
////import java.nio.charset.StandardCharsets;
////import java.nio.file.Files;
////import java.util.zip.Deflater;
////
////import javax.swing.*;
////
////public class DEFLATE_GUI implements ActionListener {
////	private JLabel fileLabel, headerLabel, nameLabel;
////	private JFrame frame;
////	private JButton uploadButton, compressButton, decompressButton, downloadButton;
////	private JPanel mainPanel;
////	private JFileChooser fileChooser;
////	private File selectedFile;
////	private File compressedFile;
////	private static final int CHUNK_SIZE = 32768; // 32 kb
////	DEFLATE deflate;
////	File output;
////	public DEFLATE_GUI() {
////		frame = new JFrame("DEFLATE - by Dima");
////		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
////		frame.setSize(400, 300);
////		frame.setLayout(new BorderLayout());
////
////		// Create header panel
////		JPanel headerPanel = new JPanel();
////		headerPanel.setBackground(Color.LIGHT_GRAY);
////		headerPanel.setPreferredSize(new Dimension(frame.getWidth(), 50));
////		headerLabel = new JLabel("DEFLATE Compression Tool", JLabel.CENTER);
////		nameLabel = new JLabel("By Dmitry Ignatiev", JLabel.CENTER);
////		headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
////		nameLabel.setFont(new Font("Arial", Font.ITALIC, 12));
////		headerPanel.add(headerLabel);
////		headerPanel.add(nameLabel);
////
////		// Create the main panel with GridBagLayout
////		mainPanel = new JPanel();
////		mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
////		mainPanel.setLayout(new GridBagLayout());
////		GridBagConstraints gbc = new GridBagConstraints();
////		gbc.fill = GridBagConstraints.HORIZONTAL;
////
////		// Initialize components
////		uploadButton = new JButton("Upload File");
////		uploadButton.addActionListener(this);
////		fileLabel = new JLabel("No file selected");
////
////		compressButton = new JButton("Compress File");
////		compressButton.setEnabled(false);
////		compressButton.addActionListener(this);
////
////		decompressButton = new JButton("Decompress File");
////		decompressButton.setEnabled(false);
////		decompressButton.addActionListener(this);
////
////		downloadButton = new JButton("Download Compressed File");
////		downloadButton.setEnabled(false);
////		downloadButton.addActionListener(this);
////
////		// Add components to the GridBagLayout
////		gbc.gridx = 0;
////		gbc.gridy = 0;
////		gbc.gridwidth = GridBagConstraints.REMAINDER;
////		gbc.weightx = 1.0;
////		gbc.anchor = GridBagConstraints.CENTER;
////		mainPanel.add(uploadButton, gbc);
////		mainPanel.add(downloadButton, gbc);
////		gbc.gridy = 1;
////		gbc.weightx = 0.0;
////		gbc.fill = GridBagConstraints.NONE;
////		mainPanel.add(fileLabel, gbc);
////		gbc.gridy = 2;
////		mainPanel.add(compressButton, gbc);
////		gbc.gridy = 3;
////		mainPanel.add(decompressButton, gbc);
////
////		// Add panels to the frame
////		frame.add(headerPanel, BorderLayout.NORTH);
////		frame.add(mainPanel, BorderLayout.CENTER);
////
////		// Initialize file chooser
////		fileChooser = new JFileChooser();
////
////		// Show the frame
////		frame.setVisible(true);
////	}
////	private  byte[] compressFile(File file, DEFLATE deflate) throws IOException {
////
////		try (FileInputStream fis = new FileInputStream(file);
////				BufferedInputStream bis = new BufferedInputStream(fis)) {
////
////			ByteArrayOutputStream baos = new ByteArrayOutputStream();
////			byte[] buffer = new byte[1024]; // Buffer size of 1024 bytes
////			int bytesRead;
////
////			while ((bytesRead = bis.read(buffer)) != -1) {
////				baos.write(buffer, 0, bytesRead);
////			}
////
////			// Compress the byte array and return it
////
////			return deflate.compress(baos.toString());
////		}
////	}
////
////	@Override
////	public void actionPerformed(ActionEvent e) {
////		if (e.getSource() == uploadButton) {
////			int res = fileChooser.showOpenDialog(frame);
////			if (res == JFileChooser.APPROVE_OPTION) {
////				selectedFile = fileChooser.getSelectedFile();
////				fileLabel.setText("File: " + selectedFile.getPath());
////				compressButton.setEnabled(true);
////				decompressButton.setEnabled(false);
////				downloadButton.setEnabled(false);
////			}
////		} else if (e.getSource() == compressButton) {
////			if (selectedFile != null && selectedFile.exists()) {
////				try {
////					deflate = new DEFLATE();
////					compressedFile = new File("compressed_output.dat");
////					byte[] compressedData = compressFile(selectedFile, deflate);
////
////					try (FileOutputStream fos = new FileOutputStream(compressedFile)) {
////						fos.write(compressedData);
////						for (byte b:compressedData)
////						{System.out.print(b);}
////					}
////
////					fileLabel.setText("File compressed: " + compressedFile.getPath());
////					decompressButton.setEnabled(true);
////					downloadButton.setEnabled(true);
////				} catch (Exception e1) {
////					e1.printStackTrace();
////				}
////			} else {
////				fileLabel.setText("No file selected or file does not exist.");
////			}
////		}else if (e.getSource() == decompressButton) {
////			if (compressedFile != null && compressedFile.exists()) {
////				try {
////					// Decompress the file
////					String decompressedData = decompressFile(compressedFile, new DEFLATE());
////
////					// Limit the display of decompressed data to a certain length for the label
////					int maxDisplayLength = 100; // Adjust this as needed
////					String displayData = decompressedData.length() > maxDisplayLength 
////							? decompressedData.substring(0, maxDisplayLength) + "..." 
////									: decompressedData;
////
////					fileLabel.setText("File decompressed. Content preview: " + displayData);
////				} catch (Exception e1) {
////					e1.printStackTrace();
////					fileLabel.setText("An error occurred during decompression.");
////				}
////			} else {
////				fileLabel.setText("No compressed file available for decompression.");
////			}
////		}
////		else if (e.getSource() == downloadButton) {
////			if (compressedFile != null && compressedFile.exists()) {
////				JFileChooser saveChooser = new JFileChooser();
////				int res = saveChooser.showSaveDialog(frame);
////				if (res == JFileChooser.APPROVE_OPTION) {
////					File saveFile = saveChooser.getSelectedFile();
////					
////					try {
////						Files.copy(compressedFile.toPath(), saveFile.toPath());
////						fileLabel.setText("File saved to: " + saveFile.getPath());
////					} catch (IOException ex) {
////						ex.printStackTrace();
////						fileLabel.setText("Failed to save file.");
////					}
////				}
////			}
////		}
////	}
////	//    private byte[] compressFile(File file, DEFLATE deflate) throws IOException {
////	//        try (FileInputStream fis = new FileInputStream(file);
////	//             BufferedInputStream bis = new BufferedInputStream(fis)) {
////	//            
////	//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
////	//            byte[] buffer = new byte[1024]; // Buffer size of 1024 bytes
////	//            int bytesRead;
////	//
////	//            while ((bytesRead = bis.read(buffer)) != -1) {
////	//                // Write the read bytes into the ByteArrayOutputStream
////	//                baos.write(buffer, 0, bytesRead);
////	//            }
////	//            
////	//            // Now that baos contains the full byte content of the file, compress it
////	//            byte[] compressedBaos = deflate.compress(baos);
////	//            
////	//            // Convert the compressed ByteArrayOutputStream to a byte array and return it
////	//            return compressedBaos;
////	//        }
////	//    }
////
////	// Method to decompress the file (assumes the same format as compressFile)
////	private String decompressFile(File file, DEFLATE deflate) throws IOException {
////		try (FileInputStream fis = new FileInputStream(file);
////				BufferedInputStream bis = new BufferedInputStream(fis)) {
////
////			ByteArrayOutputStream baos = new ByteArrayOutputStream();
////			byte[] buffer = new byte[1024]; // Buffer size of 1024 bytes
////			int bytesRead;
////
////			while ((bytesRead = bis.read(buffer)) != -1) {
////				// Process the chunk of bytes
////				baos.write(buffer, 0, bytesRead);
////			}
////
////			// Assuming deflate.decompress accepts a byte array and returns decompressed data as a String
////			return deflate.decompress(baos.toByteArray());
////		}
////	}
////
////	//    public static String decompressFile(File file, DEFLATE deflate) throws Exception {
////	//        // Implement decompression logic
////	//        // For now, assuming a placeholder implementation
////	//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
////	//        try (InputStream inputStream = new FileInputStream(file)) {
////	//            byte[] buffer = new byte[CHUNK_SIZE];
////	//            int bytesRead;
////	//            while ((bytesRead = inputStream.read(buffer)) != -1) {
////	//                // Replace this with actual decompression logic
////	//                String chunk = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
////	//                String decompressedChunk = deflate.decompress(chunk.getBytes());
////	//                outputStream.write(decompressedChunk.getBytes(StandardCharsets.UTF_8));
////	//            }
////	//        }
////	//        return outputStream.toString(StandardCharsets.UTF_8);
////	//    }
////
////	public static void main(String[] args) {
////		new DEFLATE_GUI();
////	}
////}
package DEFLATE1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javax.swing.*;

public class DEFLATE_GUI implements ActionListener {
    private JLabel fileLabel, headerLabel, nameLabel;
    private JFrame frame;
    private JButton uploadButton, compressButton, decompressButton, downloadButton;
    private JPanel mainPanel;
    private JFileChooser fileChooser;
    private File selectedFile;
    private File compressedFile;
    private static final int CHUNK_SIZE = 32768; // 32 kb
    private DEFLATE deflate;

    public DEFLATE_GUI() {
        frame = new JFrame("DEFLATE - by Dima");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        // Create header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setPreferredSize(new Dimension(frame.getWidth(), 50));
        headerLabel = new JLabel("DEFLATE Compression Tool", JLabel.CENTER);
        nameLabel = new JLabel("By Dmitry Ignatiev", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        nameLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        headerPanel.add(headerLabel);
        headerPanel.add(nameLabel);

        // Create the main panel with GridBagLayout
        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Initialize components
        uploadButton = new JButton("Upload File");
        uploadButton.addActionListener(this);
        fileLabel = new JLabel("No file selected");

        compressButton = new JButton("Compress File");
        compressButton.setEnabled(false);
        compressButton.addActionListener(this);

        decompressButton = new JButton("Decompress File");
        decompressButton.setEnabled(false);
        decompressButton.addActionListener(this);

        downloadButton = new JButton("Download Compressed File");
        downloadButton.setEnabled(false);
        downloadButton.addActionListener(this);

        // Add components to the GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(uploadButton, gbc);
        mainPanel.add(downloadButton, gbc);
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(fileLabel, gbc);
        gbc.gridy = 2;
        mainPanel.add(compressButton, gbc);
        gbc.gridy = 3;
        mainPanel.add(decompressButton, gbc);

        // Add panels to the frame
        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(mainPanel, BorderLayout.CENTER);

        // Initialize file chooser and DEFLATE instance
        fileChooser = new JFileChooser();
        deflate = new DEFLATE();

        // Show the frame
        frame.setVisible(true);
    }

    private byte[] compressFile(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             StringWriter stringWriter = new StringWriter()) {

            char[] buffer = new char[CHUNK_SIZE];
            int charsRead;
            while ((charsRead = isr.read(buffer)) != -1) {
            	 String chunk = new String(buffer, 0, charsRead);
                 System.out.println("Read chunk: " + chunk); // Print the chunk

                 // Write the chunk to StringWriter
                 stringWriter.write(buffer, 0, charsRead);            
            }

            // Convert the accumulated string to bytes
            String fileContent = stringWriter.toString();
            byte[] inputBytes = fileContent.getBytes(StandardCharsets.UTF_8);

            // Compress the byte array and return it
            return deflate.compress(fileContent);
        }
    }

    private String decompressFile(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[CHUNK_SIZE];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

            // Decompress the byte array and return it as a String
            return deflate.decompress(baos.toByteArray());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == uploadButton) {
            int res = fileChooser.showOpenDialog(frame);
            if (res == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                fileLabel.setText("File: " + selectedFile.getPath());
                compressButton.setEnabled(true);
                decompressButton.setEnabled(false);
                downloadButton.setEnabled(false);
            }
        } else if (e.getSource() == compressButton) {
            if (selectedFile != null && selectedFile.exists()) {
                try {
                    compressedFile = new File("compressed_output.dat");
                    byte[] compressedData = compressFile(selectedFile);

                    try (FileOutputStream fos = new FileOutputStream(compressedFile)) {
                        fos.write(compressedData);
                    }

                    fileLabel.setText("File compressed: " + compressedFile.getPath());
                    decompressButton.setEnabled(true);
                    downloadButton.setEnabled(true);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    fileLabel.setText("An error occurred during compression.");
                }
            } else {
                fileLabel.setText("No file selected or file does not exist.");
            }
        } else if (e.getSource() == decompressButton) {
            if (compressedFile != null && compressedFile.exists()) {
                try {
                    // Decompress the file
                    String decompressedData = decompressFile(compressedFile);

                    // Limit the display of decompressed data to a certain length for the label
                    int maxDisplayLength = 100; // Adjust this as needed
                    String displayData = decompressedData.length() > maxDisplayLength 
                            ? decompressedData.substring(0, maxDisplayLength) + "..." 
                            : decompressedData;

                    fileLabel.setText("File decompressed. Content preview: " + displayData);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    fileLabel.setText("An error occurred during decompression.");
                }
            } else {
                fileLabel.setText("No compressed file available for decompression.");
            }
        } else if (e.getSource() == downloadButton) {
            if (compressedFile != null && compressedFile.exists()) {
                JFileChooser saveChooser = new JFileChooser();
                int res = saveChooser.showSaveDialog(frame);
                if (res == JFileChooser.APPROVE_OPTION) {
                    File saveFile = saveChooser.getSelectedFile();

                    try {
                        Files.copy(compressedFile.toPath(), saveFile.toPath());
                        fileLabel.setText("File saved to: " + saveFile.getPath());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        fileLabel.setText("Failed to save file.");
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DEFLATE_GUI::new);
    }
}
