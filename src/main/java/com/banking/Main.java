package com.banking;

import com.banking.service.ActionHandler;

public class Main {
    public static void main(String[] args) {
        ActionHandler actionHandler = new ActionHandler();

        // Main Loop
        while (true) {
            int action = actionHandler.getActionFromUser();

            actionHandler.executeAction(action);
        }
    }
}
