package com.banking.service.actions;

import com.banking.entity.BankAccount;
import com.banking.entity.Credentials;
import com.banking.service.BankAccountService;
import com.banking.service.CredentialsService;
import com.banking.service.Validator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DeleteAccountAction implements Action {
    private static final String ACTION_NAME = "Delete Account";
    private static final String GET_ACCOUNT_NUMBER_MESSAGE = "Please enter the account number you want to delete and press Enter:";
    private final BankAccountService bankAccountService;
    private final CredentialsService credentialsService;
    private final Validator validator;

    public DeleteAccountAction() {
        credentialsService = new CredentialsService();
        bankAccountService = new BankAccountService();
        validator = new Validator();
    }

    @Override
    public void executeAction() {
        // Request user
        BankAccount targetAccount;
        while (true) {
            long targetAccountNumber = getAccountNumberFromUser();

            targetAccount = bankAccountService.getByAccountNumber(targetAccountNumber);
            if (targetAccount == null) {
                System.out.println("""
                        Something went wrong, your bank account can not be found.\s
                        Make sure you've entered the correct number of existing account.
                        Try again (y/n)?:""");
                if(!getConfirmationFromUser()) {
                    returnToMainMenu(false);
                    break;
                }
            } else {
                System.out.println("Account found.");
                break;
            }
        }

        System.out.println("Account can't be restored after deletion, proceed? (y/n):");
        if (getConfirmationFromUser()) {
            assert targetAccount != null;
            credentialsService.deleteCredentialsByAccountNumber(targetAccount.getAccountNumber());
            bankAccountService.deleteAccount(targetAccount);

            System.out.println("Account deletion complete.");
        } else {
            System.out.println("Account deletion canceled.");
        }

        returnToMainMenu(true);
    }

    private long getAccountNumberFromUser() {
        System.out.println(GET_ACCOUNT_NUMBER_MESSAGE);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                String enteredAccountNumber = br.readLine();

                if (validator.validateAccNumber(enteredAccountNumber)) {
                    long accNumber = Long.parseLong(enteredAccountNumber);
                    System.out.printf("Entered account number: %d. Please wait...\n", accNumber);
                    return accNumber;
                } else {
                    System.out.println("Invalid account number. Please enter the correct one:");
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void printGreetings() {
        System.out.printf(GREETING_MSG, ACTION_NAME);
    }
}
