package com.banking.service;

import com.banking.entity.Credentials;
import com.banking.service.connection.ConnectionHandler;
import com.banking.service.connection.SocketResponse;

public class CredentialsService {
    private final ConnectionHandler connectionHandler;

    public CredentialsService() {
        this.connectionHandler = ConnectionHandler.getInstance();
    }

    public Credentials saveCredentials(Credentials credentials) {
        SocketResponse socketResponse = connectionHandler.sendSaveCredentialsRequest(credentials);

        if (socketResponse.isSuccessful()) {
            return (Credentials) socketResponse.getData();
        } else {
            System.out.printf("Error: Unable to save/update the bank account: %s\n",
                    socketResponse.getErrorMessage());
            return null;
        }
    }

    public void deleteCredentialsByAccountNumber(long accountNumber) {
        SocketResponse socketResponse = connectionHandler.sendDeleteCredentialsRequest(accountNumber);
        if (!socketResponse.isSuccessful()) {
            System.out.printf("Error: Unable to delete the bank account %d: %s\n",
                    accountNumber,
                    socketResponse.getErrorMessage());
        }
    }
}
