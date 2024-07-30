package edu.challengethree.supplier_registration.exceptions;

public class SupplierAlreadyRegisteredException  extends RuntimeException {

    public SupplierAlreadyRegisteredException(String message) {
        super(message);
    }
}
