import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    // List to keep track of all connected clients
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket; // Represents the connection between the client and the server
    private BufferedReader bufferedReader; // Reads input from the client
    private BufferedWriter bufferedWriter; // Sends output to the client
    private String clientUsername; // Stores the client's username

    // Constructor to initialize the client handler and set up communication streams
    public ClientHandler(Socket socket) {
        try {
            this.socket = socket; // The socket represents the client-server connection
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // Output stream to send data
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Input stream to read data
            this.clientUsername = bufferedReader.readLine(); // Read the client's username sent during connection
            clientHandlers.add(this); // Add this client handler to the list of active clients
            broadcastMessage("KUNAKA'S SERVER: " + clientUsername + " a rejoindre le chat!\n Bienvenu(e)!"); // Broadcast the new client connection message
        } catch (IOException e) {
            // Handle any connection errors and close resources
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    // Run method to listen for messages from the client
    @Override
    public void run() {
        String messageFromClient;

        // Keep listening for messages from the client as long as the socket is connected
        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine(); // Read message from the client
                broadcastMessage(messageFromClient); // Broadcast the received message to all other clients
            } catch (IOException e) {
                // If an error occurs, close all resources and exit the loop
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    // Method to send a message to all other clients except the sender
    public void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                // Avoid sending the message back to the sender
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.bufferedWriter.write(messageToSend); // Write the message
                    clientHandler.bufferedWriter.newLine(); // Add a new line after the message
                    clientHandler.bufferedWriter.flush(); // Send the message to the client
                }
            } catch (IOException e) {
                // Handle any IOException and close the client connection
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    // Method to remove the client handler when the client disconnects
    public void removeClientHandler() {
        clientHandlers.remove(this); // Remove this client handler from the list
        broadcastMessage("KUNAKA'S SERVER: " + clientUsername + " a quitté le chat."); // Notify other clients
    }

    // Method to close all resources (socket, input/output streams) safely
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler(); // Remove the client from the list and broadcast their departure
        try {
            if (bufferedReader != null) {
                bufferedReader.close(); // Close the input stream
            }
            if (bufferedWriter != null) {
                bufferedWriter.close(); // Close the output stream
            }
            if (socket != null) {
                socket.close(); // Close the socket
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print stack trace for any errors during closing
        }
    }
}
