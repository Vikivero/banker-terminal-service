package com.banking.service;

import com.banking.service.actions.*;

import javax.naming.OperationNotSupportedException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ActionHandler {

    public void executeAction(int action) {
        Action selectedAction = switch (action) {
            case 1 -> new CreateAccountAction();
            case 2 -> new UpdateAccountAction();
            case 3 -> new DeleteAccountAction();
            case 0 -> new ExitAction();
            default -> new UndefinedAction();
        };

        try {
            selectedAction.printGreetings();
            selectedAction.executeAction();
        } catch (OperationNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public int getActionFromUser() {
        String errorMsg = "Please input a number from 0 to 3: ";
        String delimiter = "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n";
        String header = "Banker Terminal v0.1\n";

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println(delimiter +
                    header +
                    delimiter +
                    "Input the number corresponding to action you want to perform: \n" +
                    "1: Create new Account\n" +
                    "2: Edit the existing Account data\n" +
                    "3: Delete the Account\n" +
                    "0: Exit\n" +
                    "------------------");
            int userInputAction = -1;
            try {
                userInputAction = Integer.parseInt(br.readLine());
            } catch (NumberFormatException nfe) {
                System.out.println(errorMsg);
                continue;
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (userInputAction < 0 || userInputAction > 4) {
                System.out.println(errorMsg);
                continue;
            }

            return userInputAction;
        }

    }
}
