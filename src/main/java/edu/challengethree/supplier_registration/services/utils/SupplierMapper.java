package edu.challengethree.supplier_registration.services.utils;

import edu.challengethree.supplier_registration.DTOs.SupplierCreationDTO;
import edu.challengethree.supplier_registration.DTOs.SupplierDTO;
import edu.challengethree.supplier_registration.DTOs.SupplierSimplifiedDTO;
import edu.challengethree.supplier_registration.model.entities.Supplier;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {

    public static Supplier supplierCreationDTOToSupplier(SupplierCreationDTO supplierCreationDTO, String userId) {
        Supplier supplier = new Supplier();
        supplier.setUserId(userId);
        supplier.setSupplierName(supplierCreationDTO.getSupplierName());
        supplier.setContactName(supplierCreationDTO.getContactName());
        supplier.setContactEmail(supplierCreationDTO.getContactEmail());
        supplier.setPersonType(supplierCreationDTO.getPersonType());
        supplier.setDocumentNumber(supplierCreationDTO.getDocumentNumber());
        supplier.setPhoneNumbers(supplierCreationDTO.getPhoneNumbers());
        supplier.setAddress(AddressMapper.addressDTOToAddress(supplierCreationDTO.getAddressDTO()));
        supplier.setActivityDescription(supplierCreationDTO.getActivityDescription());

        return supplier;
    }

    public static SupplierDTO supplierToSupplierDTO(Supplier supplier) {
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setId(supplier.getId());
        supplierDTO.setUserId(supplier.getUserId());
        supplierDTO.setSupplierName(supplier.getSupplierName());
        supplierDTO.setContactName(supplier.getContactName());
        supplierDTO.setContactEmail(supplier.getContactEmail());
        supplierDTO.setPersonType(supplier.getPersonType());
        supplierDTO.setDocumentNumber(supplier.getDocumentNumber());
        supplierDTO.setPhoneNumbers(supplier.getPhoneNumbers());
        supplierDTO.setAddress(supplier.getAddress());
        supplierDTO.setActivityDescription(supplier.getActivityDescription());

        return supplierDTO;
    }

    public static SupplierSimplifiedDTO supplierToSupplierSimplifiedDTO(Supplier supplier) {
        SupplierSimplifiedDTO supplierSimplifiedDTO = new SupplierSimplifiedDTO();
        supplierSimplifiedDTO.setId(supplier.getId());
        supplierSimplifiedDTO.setSupplierName(supplier.getSupplierName());
        supplierSimplifiedDTO.setContactName(supplier.getContactName());
        supplierSimplifiedDTO.setContactEmail(supplier.getContactEmail());
        supplierSimplifiedDTO.setDocumentNumber(supplier.getDocumentNumber());

        return supplierSimplifiedDTO;
    }
}
