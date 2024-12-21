import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    private ServerSocket serverSocket; // This will listen to client communication requests and connect with them

    // Constructor to initialize the server socket
    public ChatServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    // Method to keep the server running and accepting client connections
    public void startServer() {
        try {
            // Continuously accept new client connections until the server is closed
            while (!serverSocket.isClosed()) {
                // Wait for a client to connect
                Socket socket = serverSocket.accept();
                System.out.println("Un nouveau client a rejoindre le chat!"); // Output message when a client connects

                // Create a new client handler to manage the client's communication
                ClientHandler clientHandler = new ClientHandler(socket);

                // Start a new thread to handle the client communication
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            // Handle any IOExceptions (such as if the server socket is closed unexpectedly)
            e.printStackTrace();
        }
    }

    // Method to safely shut down the server socket if an error occurs or when stopping the server
    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close(); // Close the server socket to stop listening for new connections
            }
        } catch (IOException e) {
            // Print the stack trace if an exception occurs while closing the socket
            e.printStackTrace();
        }
    }

    // Main method to initialize and start the server
    public static void main(String[] args) throws IOException {
        // Create a ServerSocket bound to port 1234 (clients will connect to this port)
        ServerSocket serverSocket = new ServerSocket(1234);
        // Create an instance of ChatServer with the server socket
        ChatServer chatServer = new ChatServer(serverSocket);
        // Start the server to listen for incoming client connections
        chatServer.startServer();
    }
}
