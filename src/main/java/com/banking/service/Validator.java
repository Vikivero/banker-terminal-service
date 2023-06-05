package com.banking.service;

public class Validator {
    public boolean validateAccNumber(String accountNumber) {
        try {
            Long.parseLong(accountNumber);
        } catch (NumberFormatException nfe) {
            return false;
        }
        //could have been an if, but this will return a true/false value anyway
        return accountNumber.length() == 8;
    }

    public boolean validatePassword(String password) {
        //can't be an empty String
        return password.trim().length() > 0;
    }

    public boolean validateName(String enteredName) {
        return enteredName.trim().length() > 0;
    }

    public boolean validatePesel(String enteredPesel) {
        try {
            Long.parseLong(enteredPesel);
        } catch (NumberFormatException nfe) {
            return false;
        }
        //could have been an if, but this will return a true/false value anyway
        return enteredPesel.length() == 11;
    }
}
