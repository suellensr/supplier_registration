package edu.challengethree.supplier_registration.DTOs;

import edu.challengethree.supplier_registration.model.enums.PersonType;
import edu.challengethree.supplier_registration.validation.ValidDocument;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@ValidDocument
public class SupplierCreationDTO {

    @NotNull
    private String supplierName;

    @NotNull
    private String contactName;

    @Email
    @NotNull
    private String contactEmail;

    @NotNull
    private PersonType personType;

    @NotNull
    private String documentNumber;

    @NotNull
    private List<String> phoneNumbers;

    @NotNull
    private AddressDTO addressDTO;

    @Size(min = 1, max = 50000)
    @NotNull
    private String activityDescription;

    public @NotNull String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(@NotNull String supplierName) {
        this.supplierName = supplierName;
    }

    public @NotNull String getContactName() {
        return contactName;
    }

    public void setContactName(@NotNull String contactName) {
        this.contactName = contactName;
    }

    public @NotNull String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(@NotNull String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public @NotNull PersonType getPersonType() {
        return personType;
    }

    public void setPersonType(@NotNull PersonType personType) {
        this.personType = personType;
    }

    public @NotNull String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(@NotNull String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public @NotNull List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(@NotNull List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public @NotNull AddressDTO getAddressDTO() {
        return addressDTO;
    }

    public void setAddressDTO(@NotNull AddressDTO addressDTO) {
        this.addressDTO = addressDTO;
    }

    public @NotNull String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(@NotNull String activityDescription) {
        this.activityDescription = activityDescription;
    }
}
