package com.banking.service.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketConnectionClient {
    private static final int SERVER_PORT = 11111;
    private static final String SERVER_HOST = "localhost";

    public SocketResponse makeRequest(SocketRequest request) {
        try {
            Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            try {
                // Send the SocketRequest object to the server
                objectOutputStream.writeObject(request);
                objectOutputStream.flush();
                System.out.println("Request sent to the server. ");

                // Receive the SocketResponse object from the server
                SocketResponse response = (SocketResponse) objectInputStream.readObject();
                System.out.printf("Server responded - request is successful: %s\n", response.isSuccessful());

                return response;
            } finally {
                try {
                    objectInputStream.close();
                    objectOutputStream.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            // Mask error cause from user for safety
            return new SocketResponse(false, "Connection Error");
        }
    }
}
