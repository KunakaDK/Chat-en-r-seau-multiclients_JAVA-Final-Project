import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client {

    // Socket and streams for communication
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    // GUI components
    private JFrame frame;
    private JTextArea messageArea;
    private JTextField messageField;
    private JButton sendButton;

    // Constructor to initialize the client
    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;

            // Initialize the GUI for the client
            initializeGUI();

            // Send username to the server immediately after connection
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            // Handle initialization failure by closing resources
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    // Method to set up the GUI
    private void initializeGUI() {
        frame = new JFrame("Chat Client - " + username); // Frame title includes username
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500); // Set window size

        // Create a text area to display chat messages (read-only)
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea); // Add scroll functionality

        // Create a text field and button for sending messages
        messageField = new JTextField();
        sendButton = new JButton("Send");

        // Layout to organize input field and button
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(messageField, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.EAST);

        // Add components to the frame
        frame.add(scrollPane, BorderLayout.CENTER); // Message display area
        frame.add(panel, BorderLayout.SOUTH);      // Input field and button

        // Add action listener for the "Send" button
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Enable pressing Enter in the text field to send a message
        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        frame.setVisible(true); // Make the GUI visible
    }

    // Method to send a message to the server
    public void sendMessage() {
        try {
            String messageToSend = messageField.getText(); // Get text from input field
            if (!messageToSend.isEmpty()) {
                // Send the message with username prefix
                bufferedWriter.write(username + ": " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                // Display the sent message in the client's message area
                messageArea.append("YOU: " + messageToSend + "\n");
                messageField.setText(""); // Clear the input field
            }
        } catch (IOException e) {
            // Handle message send failure by closing resources
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    // Method to continuously listen for messages from the server
    public void listenForMessage() {
        new Thread(new Runnable() {
            public void run() {
                String msgFromGroupChat;

                // Keep listening while the socket is connected
                while (socket.isConnected()) {
                    try {
                        msgFromGroupChat = bufferedReader.readLine(); // Read messages from the server
                        if (msgFromGroupChat != null) {
                            messageArea.append(msgFromGroupChat + "\n"); // Display incoming messages
                        }
                    } catch (IOException e) {
                        // Handle failure by closing resources
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start(); // Start the thread for listening
    }

    // Method to close all resources and the GUI
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
            frame.dispose(); // Close the GUI
        } catch (IOException e) {
            e.printStackTrace(); // Print error stack trace
        }
    }

    // Main method to run the client application
    public static void main(String[] args) throws IOException {
        // Prompt the user for a username
        String username = JOptionPane.showInputDialog("Entrez votre nom (username):");
        if (username != null && !username.trim().isEmpty()) {
            // Connect to the server
            Socket socket = new Socket("localhost", 1234);
            Client client = new Client(socket, username); // Create a new client instance
            client.listenForMessage(); // Start listening for incoming messages
        } else {
            // Handle empty username input
            System.out.println("Username cannot be empty.");
        }
    }
}
