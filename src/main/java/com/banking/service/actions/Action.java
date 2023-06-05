package com.banking.service.actions;

import com.banking.service.connection.SocketResponse;

import javax.naming.OperationNotSupportedException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public interface Action {
    String GREETING_MSG = " --- You have selected action [%s] ---\n";
    String ASK_RETURN_TO_MAIN_MSG = "Enter any characters to return to main menu:";
    String RETURN_TO_MAIN_MSG = "Returning to main menu...";
    void executeAction() throws OperationNotSupportedException;

    void printGreetings();

    default boolean tryToCompleteAction(SocketResponse response) {
        if (response != null && response.isSuccessful()) {
            returnToMainMenu(true);
            return true;
        } else {
            System.out.printf("Something went wrong: %s, try again (y/n)?\n", response.getErrorMessage());
            if(!getConfirmationFromUser()) {
                returnToMainMenu(false);
                return true;
            }
        }

        return false;
    }

    default void returnToMainMenu(boolean askUser) {
        if (askUser) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            try {
                System.out.println(ASK_RETURN_TO_MAIN_MSG);
                br.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(RETURN_TO_MAIN_MSG);
    }

    default boolean getConfirmationFromUser() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String enteredAnswer = "";
            try {
                enteredAnswer = br.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (enteredAnswer.equals("y") || enteredAnswer.equals("Y")) {
                return true;
            } else if (enteredAnswer.equals("n") || enteredAnswer.equals("N")) {
                return false;
            } else {
                System.out.println("Incorrect answer. Please enter 'y' or 'n':");
            }
        }
    }
}
