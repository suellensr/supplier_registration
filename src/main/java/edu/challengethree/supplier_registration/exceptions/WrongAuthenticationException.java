package edu.challengethree.supplier_registration.exceptions;

public class WrongAuthenticationException extends RuntimeException {

    public WrongAuthenticationException(String message) {
        super(message);
    }
}
