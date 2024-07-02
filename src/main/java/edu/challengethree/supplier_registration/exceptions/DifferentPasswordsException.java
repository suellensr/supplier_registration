package edu.challengethree.supplier_registration.exceptions;

public class DifferentPasswordsException extends RuntimeException {

    public DifferentPasswordsException(String message) {
        super(message);
    }
}