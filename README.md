# Chat-en-r-seau-multiclients_JAVA-Final-Project
A simple chat application built in Java that allows users to send and receive real-time text messages in a group chat. The application includes both client and server components, supporting multiple clients connected to a single server. The user interface is designed using Java Swing, providing an intuitive and easy-to-use experience.

Features
The application supports real-time text messaging, allowing users to send and receive messages instantly. It also supports multi-client usage, where multiple users can connect to a single server and chat in a group setting. The interface is user-friendly and designed with Java Swing, providing a clean and simple environment for users. The server component handles incoming client connections, processes messages, and broadcasts them to all connected clients.

Components
The application consists of three main components: the Client, the Server, and the ClientHandler. The Client is the user interface where individuals can send and receive messages. The Server handles incoming connections, manages multiple clients, and broadcasts messages. The ClientHandler is responsible for managing individual client connections, reading incoming messages from each client, and forwarding them to others.

Requirements
To run the chat application, you will need Java 8 or higher installed. You can use any text editor or Integrated Development Environment (IDE) such as IntelliJ IDEA or Eclipse to compile and run the code.

Setup
To set up the project, start by cloning the repository to your local machine:

git clone https://github.com/your-username/chat-application-java.git

Once you have the repository, you need to compile and run both the server and client applications.

1. Compile and Run the Server
The server listens for incoming connections on port 1234. To compile and run the server, navigate to the folder containing the ChatServer.java file and execute the following commands:


javac ChatServer.java
java ChatServer

2. Compile and Run the Client
The client connects to the server at localhost on port 1234. To run the client, compile and execute the following commands:

javac Client.java
java Client
When you run the client, a prompt will appear asking you for a username. Once entered, the client will connect to the server and join the chat.

Usage
Once a client connects to the server, they can start sending messages to the group chat. Messages are broadcasted to all other clients connected to the server. The client interface provides a text field for input and a "Send" button. Users can also press Enter to send their messages.

For example, if Client 1 sends the message "Hello, everyone!", it will be broadcasted to all other clients in the chat.

File Descriptions
Client.java: This file contains the client-side application logic. It handles the graphical user interface, message sending, and receiving.
ChatServer.java: This file contains the server-side application. The server listens for incoming connections, accepts clients, and handles broadcasting of messages.
ClientHandler.java: The client handler manages individual client connections, listens for incoming messages from clients, and broadcasts them to other connected clients.

Code Explanation
The server setup is handled in the ChatServer class, where a ServerSocket is created to listen for incoming client connections. When a connection is accepted, a new ClientHandler is created to handle communication with that particular client.

The ClientHandler class manages each client’s communication. It reads messages sent by a client and broadcasts them to all other clients. The method broadcastMessage is used to send a message to all connected clients, except the sender.

The GUI for the client is implemented in the Client class using Java Swing. This provides the input field for the user to type messages and a display area for showing incoming messages. The application listens for messages and updates the chat interface in real-time.

Contributing
Feel free to fork this repository and make improvements to the project. If you encounter any issues or would like to suggest new features, you can create a pull request or open an issue on GitHub.
