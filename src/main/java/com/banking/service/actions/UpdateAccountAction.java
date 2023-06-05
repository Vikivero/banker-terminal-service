package com.banking.service.actions;

import com.banking.entity.BankAccount;
import com.banking.entity.Credentials;
import com.banking.service.BankAccountService;
import com.banking.service.CredentialsService;
import com.banking.service.Validator;
import com.banking.service.connection.ConnectionHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UpdateAccountAction implements Action {
    private static final String ACTION_NAME = "Edit the existing Account";
    private static final String GET_ACCOUNT_NUMBER_MESSAGE = "Please enter the account number you want to edit and press Enter:";

    private final BankAccountService bankAccountService;
    private final CredentialsService credentialsService;
    private final Validator validator;

    private final ConnectionHandler connectionHandler;

    public UpdateAccountAction() {
        bankAccountService = new BankAccountService();
        credentialsService = new CredentialsService();
        validator = new Validator();
        connectionHandler = ConnectionHandler.getInstance();
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
                System.out.printf("Account with number %d found.\n", targetAccountNumber);
                break;
            }
        }

        String appendix = ", or press Enter to proceed with current one:";
        System.out.printf("Please enter new First Name(current is %s)%s\n",
                !targetAccount.getFirstName().isBlank() ? "'" + targetAccount.getFirstName() + "'" : "empty",
                !targetAccount.getFirstName().isBlank() ? appendix : ":");
        String firstName = getNameFromUser(targetAccount.getFirstName());

        System.out.printf("Please enter new Last Name(current is %s)%s\n",
                !targetAccount.getLastName().isBlank() ? "'" + targetAccount.getLastName() + "'" : "empty",
                !targetAccount.getLastName().isBlank() ? appendix : ":");
        String lastName = getNameFromUser(targetAccount.getLastName());

        System.out.printf("Please enter new PESEL(current is %s)%s\n",
                targetAccount.getPesel() > 0 ? "'" + targetAccount.getPesel() + "'" : "empty",
                targetAccount.getPesel() > 0 ? appendix : ":");
        long pesel = getPeselFromUser(targetAccount.getPesel());

        targetAccount.setFirstName(firstName);
        targetAccount.setLastName(lastName);
        targetAccount.setPesel(pesel);

        bankAccountService.saveAccount(targetAccount);

        System.out.println("Account changes saved. Updating credentials...\n");
        String password = getPasswordFromUser();
        if (password != null) {
            credentialsService.saveCredentials(new Credentials(targetAccount.getAccountNumber(), password));
            System.out.println("Password update finished.");
        } else {
            System.out.println("Password has not been changed.");
        }

        returnToMainMenu(true);
    }

    @Override
    public void printGreetings() {
        System.out.printf(GREETING_MSG, ACTION_NAME);
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

    private String getPasswordFromUser() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("Please enter new password or press Enter to proceed with current one:");
            String enteredPassword = null;
            try {
                enteredPassword = br.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (enteredPassword.length() == 0) {
                return null;
            }
            if (!validator.validatePassword(enteredPassword)) {
                System.out.println("Please input a valid password.");
                continue;
            }
            return enteredPassword;
        }
    }

    private String getNameFromUser(String current) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String enteredName = null;
            try {
                enteredName = br.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (enteredName.length() == 0 && current != null && current.length() > 0) {
                enteredName = current;
            }

            if (validator.validateName(enteredName)) {
                System.out.printf("Input is valid: [%s]. Please wait...\n", enteredName);
                return enteredName;
            } else {
                System.out.println("Invalid input. Enter at least one non-space character:");
            }
        }
    }

    private long getPeselFromUser(long current) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String enteredPesel = null;
            try {
                enteredPesel = br.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (enteredPesel.length() == 0 && current > 0) {
                enteredPesel = String.valueOf(current);
            }

            if (validator.validatePesel(enteredPesel)) {
                System.out.printf("PESEL is valid: [%s]. Please wait...\n", enteredPesel);
                return Long.parseLong(enteredPesel);
            } else {
                System.out.println("Invalid PESEL. It should contain only 11 decimal digits:");
            }
        }
    }
}
