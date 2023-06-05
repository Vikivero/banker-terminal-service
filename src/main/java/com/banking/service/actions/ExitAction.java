package com.banking.service.actions;

public class ExitAction implements Action {
    private static final String ACTION_NAME = "Exit";

    @Override
    public void executeAction() {
        System.out.println("Good bye!");
        System.exit(0);
    }

    @Override
    public void printGreetings() {
        System.out.printf(GREETING_MSG, ACTION_NAME);
    }
}
