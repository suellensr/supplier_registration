package edu.challengethree.supplier_registration.services.impl;

import edu.challengethree.supplier_registration.DTOs.SupplierCreationDTO;
import edu.challengethree.supplier_registration.DTOs.SupplierDTO;
import edu.challengethree.supplier_registration.DTOs.SupplierSimplifiedDTO;
import edu.challengethree.supplier_registration.exceptions.ResourceNotFoundException;
import edu.challengethree.supplier_registration.exceptions.SupplierAlreadyRegisteredException;
import edu.challengethree.supplier_registration.model.entities.Supplier;
import edu.challengethree.supplier_registration.model.enums.PersonType;
import edu.challengethree.supplier_registration.repositories.SupplierRepository;
import edu.challengethree.supplier_registration.services.interfaces.SupplierService;
import edu.challengethree.supplier_registration.services.utils.AddressMapper;
import edu.challengethree.supplier_registration.services.utils.SupplierMapper;
import edu.challengethree.supplier_registration.validation.DocumentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public SupplierDTO createSupplier(SupplierCreationDTO supplierCreationDTO, String userId) {
        String documentNumber = supplierCreationDTO.getDocumentNumber().replaceAll("\\D", "");
        PersonType personType = supplierCreationDTO.getPersonType();

        DocumentValidator.validateDocument(documentNumber, personType);

        Optional<Supplier> existingSupplier = supplierRepository.findByDocumentNumber(documentNumber);
        if (existingSupplier.isPresent()) {
            throw new SupplierAlreadyRegisteredException("Este fornecedor já está cadastrado.");
        }

        Supplier supplier = SupplierMapper.supplierCreationDTOToSupplier(supplierCreationDTO, userId);
        supplierRepository.save(supplier);

        return SupplierMapper.supplierToSupplierDTO(supplier);
    }

    @Override
    public SupplierDTO updateSupplier(String id, String userId, SupplierCreationDTO supplierCreationDTO) throws ResourceNotFoundException {
        Supplier existingSupplier = supplierRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier with id " + id + " not found."));

        String documentNumber = supplierCreationDTO.getDocumentNumber().replaceAll("\\D", "");
        PersonType personType = supplierCreationDTO.getPersonType();

        DocumentValidator.validateDocument(documentNumber, personType);

        existingSupplier.setSupplierName(supplierCreationDTO.getSupplierName());
        existingSupplier.setContactName(supplierCreationDTO.getContactName());
        existingSupplier.setContactEmail(supplierCreationDTO.getContactEmail());
        existingSupplier.setPersonType(supplierCreationDTO.getPersonType());
        existingSupplier.setDocumentNumber(supplierCreationDTO.getDocumentNumber());
        existingSupplier.setPhoneNumbers(supplierCreationDTO.getPhoneNumbers());
        existingSupplier.setAddress(AddressMapper.addressDTOToAddress(supplierCreationDTO.getAddressDTO()));
        existingSupplier.setActivityDescription(supplierCreationDTO.getActivityDescription());

        supplierRepository.save(existingSupplier);

        return SupplierMapper.supplierToSupplierDTO(existingSupplier);
    }

    @Override
    public List<SupplierSimplifiedDTO> getAllSuppliers(String userId) {
        List<Supplier> allSuplier = supplierRepository.findAllByUserId(userId);
        List<SupplierSimplifiedDTO> allSuppliersDTO = new ArrayList<>();
        for (Supplier supplier : allSuplier) {
            allSuppliersDTO.add(SupplierMapper.supplierToSupplierSimplifiedDTO(supplier));
        }
        return allSuppliersDTO;
    }

    @Override
    public SupplierDTO getSupplierById(String id, String userId) throws ResourceNotFoundException {
        Supplier existingSupplier = supplierRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier with id " + id + " not found."));

        return SupplierMapper.supplierToSupplierDTO(existingSupplier);
    }

    @Override
    public String deleteSupplier(String id, String userId) throws ResourceNotFoundException {
        Supplier existingSupplier = supplierRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier with id " + id + " not found."));

        supplierRepository.deleteById(id);
        return "Supplier with id "  + id + " has been deleted successfully.";
    }

    @Override
    public SupplierDTO addPhone(String id, String userId, String phoneNumber) throws ResourceNotFoundException {
        Supplier existingSupplier = supplierRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier with id " + id + " not found."));

        List<String> phoneNumbers = existingSupplier.getPhoneNumbers();
        if (phoneNumbers == null) {
            phoneNumbers = new ArrayList<>();
        }
        phoneNumbers.add(phoneNumber);
        existingSupplier.setPhoneNumbers(phoneNumbers);

        supplierRepository.save(existingSupplier);

        return SupplierMapper.supplierToSupplierDTO(existingSupplier);
    }

    @Override
    public SupplierDTO removePhone(String id, String userId, String phoneNumber) throws ResourceNotFoundException {
        Supplier existingSupplier = supplierRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier with id " + id + " not found."));

        List<String> phoneNumbers = existingSupplier.getPhoneNumbers();
        if (phoneNumbers != null) {
            phoneNumbers.remove(phoneNumber);
            existingSupplier.setPhoneNumbers(phoneNumbers);
        }

        supplierRepository.save(existingSupplier);

        return SupplierMapper.supplierToSupplierDTO(existingSupplier);
    }
}
