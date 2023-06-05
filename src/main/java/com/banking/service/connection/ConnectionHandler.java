package com.banking.service.connection;

import com.banking.entity.BankAccount;
import com.banking.entity.Credentials;

public class ConnectionHandler {
    private static final ConnectionHandler instance = new ConnectionHandler();
    private final SocketConnectionClient client;

    public static ConnectionHandler getInstance() {
        return instance;
    }

    private ConnectionHandler() {
        client = new SocketConnectionClient();
    }

    public SocketResponse sendGetAccountRequest(long accountNumber) {
        SocketRequest sr = new SocketRequest(accountNumber, SocketRequest.RequestType.GETACCOUNT);

        return client.makeRequest(sr);
    }

    public SocketResponse sendSaveAccountRequest(BankAccount bankAccount) {
        SocketRequest sr = new SocketRequest(bankAccount.getAccountNumber(), SocketRequest.RequestType.SAVEACCOUNT);
        sr.getBody().put("account", bankAccount);

        return client.makeRequest(sr);
    }

    public SocketResponse sendDeleteAccountRequest(BankAccount bankAccount) {
        SocketRequest sr = new SocketRequest(bankAccount.getAccountNumber(), SocketRequest.RequestType.DELETEACCOUNT);

        return client.makeRequest(sr);
    }

    public SocketResponse sendSaveCredentialsRequest(Credentials credentials) {
        SocketRequest sr = new SocketRequest(credentials.getAccountNumber(), SocketRequest.RequestType.SAVECREDENTIALS);
        sr.getBody().put("password", credentials.getPassword());

        return client.makeRequest(sr);
    }

    public SocketResponse sendDeleteCredentialsRequest(long accountNumber) {
        SocketRequest sr = new SocketRequest(accountNumber, SocketRequest.RequestType.DELETECREDENTIALS);

        return client.makeRequest(sr);
    }
}
