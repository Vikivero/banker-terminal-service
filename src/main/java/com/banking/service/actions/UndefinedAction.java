package com.banking.service.actions;

import javax.naming.OperationNotSupportedException;

public class UndefinedAction implements Action {

    @Override
    public void executeAction() throws OperationNotSupportedException {
        System.out.println("Action is not supported. Please, contact your administrator!");
    }

    @Override
    public void printGreetings() {
        System.out.println("Error: Unknown action. Please restart the terminal and select the correct action.");
    }
}
