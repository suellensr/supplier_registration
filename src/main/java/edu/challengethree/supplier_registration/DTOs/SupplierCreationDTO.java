package edu.challengethree.supplier_registration.DTOs;

import edu.challengethree.supplier_registration.model.enums.PersonType;
import edu.challengethree.supplier_registration.validation.ValidDocument;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@ValidDocument
public class SupplierCreationDTO {

    @NotEmpty
    private String supplierName;

    @NotEmpty
    private String contactName;

    @Email
    @NotEmpty
    private String contactEmail;

    @NotNull
    private PersonType personType;

    @NotEmpty
    private String documentNumber;

    @NotEmpty
    private List<String> phoneNumbers;

    @Valid
    @NotNull
    private AddressDTO addressDTO;

    @Size(min = 1, max = 50000)
    @NotEmpty
    private String activityDescription;

    public @NotEmpty String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(@NotEmpty String supplierName) {
        this.supplierName = supplierName;
    }

    public @NotEmpty String getContactName() {
        return contactName;
    }

    public void setContactName(@NotEmpty String contactName) {
        this.contactName = contactName;
    }

    public @NotEmpty String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(@NotEmpty String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public @NotNull PersonType getPersonType() {
        return personType;
    }

    public void setPersonType(@NotNull PersonType personType) {
        this.personType = personType;
    }

    public @NotEmpty String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(@NotEmpty String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public @NotEmpty List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(@NotEmpty List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public @Valid AddressDTO getAddressDTO() {
        return addressDTO;
    }

    public void setAddressDTO(@Valid AddressDTO addressDTO) {
        this.addressDTO = addressDTO;
    }

    public @NotEmpty String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(@NotEmpty String activityDescription) {
        this.activityDescription = activityDescription;
    }
}
