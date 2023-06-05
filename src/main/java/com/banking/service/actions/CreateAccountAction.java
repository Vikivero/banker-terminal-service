package com.banking.service.actions;

import com.banking.entity.BankAccount;
import com.banking.service.BankAccountService;
import com.banking.service.Validator;
import com.banking.service.connection.ConnectionHandler;

public class CreateAccountAction implements Action {
    private static final String ACTION_NAME = "Create Account";
    BankAccountService bankAccountService;
    Validator validator;
    ConnectionHandler connectionHandler;

    public CreateAccountAction() {
        bankAccountService = new BankAccountService();
        validator = new Validator();
        connectionHandler = ConnectionHandler.getInstance();
    }

    @Override
    public void executeAction() {
        // Request user
        BankAccount bankAccount = bankAccountService.createNewAccount();
        if (bankAccount == null) {
            System.out.println("Something went wrong, bank account has not been created. Please contact the ServiceDesk.");
            returnToMainMenu(false);
            return;
        }

        System.out.printf("Created account with number:\n\t - %s\nNow you can edit account details.\n",
                bankAccount.getAccountNumber());
        returnToMainMenu(true);
    }

    @Override
    public void printGreetings() {
        System.out.printf(GREETING_MSG, ACTION_NAME);
    }
}
