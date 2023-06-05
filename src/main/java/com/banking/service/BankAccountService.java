package com.banking.service;

import com.banking.entity.BankAccount;
import com.banking.service.connection.ConnectionHandler;
import com.banking.service.connection.SocketResponse;

public class BankAccountService {
    private final ConnectionHandler connectionHandler;

    public BankAccountService() {
        this.connectionHandler = ConnectionHandler.getInstance();
    }

    public BankAccount getByAccountNumber(long accountNumber) {
        SocketResponse socketResponse = connectionHandler.sendGetAccountRequest(accountNumber);

        if (socketResponse.isSuccessful()) {
            return (BankAccount) socketResponse.getData();
        } else {
            System.out.printf("Error: Unable to get the bank account by number %d: %s\n",
                    accountNumber,
                    socketResponse.getErrorMessage());
            return null;
        }
    }

    public BankAccount createNewAccount() {
        return saveAccount(new BankAccount(0));
    }

    public BankAccount saveAccount(BankAccount bankAccount) {
        SocketResponse socketResponse = connectionHandler.sendSaveAccountRequest(bankAccount);

        if (socketResponse.isSuccessful()) {
            return (BankAccount) socketResponse.getData();
        } else {
            System.out.printf("Error: Unable to save/update the bank account: %s\n",
                    socketResponse.getErrorMessage());
            return null;
        }
    }

    public void deleteAccount(BankAccount bankAccount) {
        SocketResponse socketResponse = connectionHandler.sendDeleteAccountRequest(bankAccount);
        if (!socketResponse.isSuccessful()) {
            System.out.printf("Error: Unable to delete the bank account %d: %s\n",
                    bankAccount.getAccountNumber(),
                    socketResponse.getErrorMessage());
        }
    }
}
